package play.dahp.us.auctions.structure.auction;

import play.dahp.us.auctions.structure.*;
import play.dahp.us.auctions.*;
import play.dahp.us.auctions.api.reward.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import play.dahp.us.auctions.api.module.*;
import java.util.*;
import org.bukkit.*;
import play.dahp.us.auctions.api.*;

public class SealedAuction extends DefaultAuction {

    private Map<UUID, Double> currentBids;
    private Map<UUID, Integer> amountOfBids;

    private SealedAuction(final AuctionPlugin plugin, final UUID ownerUUID, final String ownerName, final double topBid,
                    final Reward reward, final double autowin, final double bidIncrement, final int timeLeft) {
        super(plugin, AuctionType.SEALED);
        this.currentBids = new HashMap<UUID, Double>();
        this.amountOfBids = new HashMap<UUID, Integer>();
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.winningBid = topBid;
        this.startPrice = topBid;
        this.reward = reward;
        this.autowin = autowin;
        this.bidIncrement = bidIncrement;
        this.timeLeft = timeLeft;
    }

    @Override
    public void placeBid(final Player player, final double bid) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        if (bid < this.startPrice) {
            this.plugin.getMessageHandler().sendMessage((CommandSender) player, this.plugin.getMessage("messages.error.bidTooLow"));
        } else if (this.amountOfBids.containsKey(player.getUniqueId()) && this.amountOfBids.get(player.getUniqueId()) >= this.plugin
                        .getConfig().getInt("auctionSettings.sealedAuctions.maxBidsPerPlayer", 1)) {
            this.plugin.getMessageHandler().sendMessage((CommandSender) player,
                            this.plugin.getMessage("messages.error.sealedAuctionsMaxBidsReached"));
        } else {
            double raiseAmount = bid;
            final double previousBid = this.currentBids.containsKey(player.getUniqueId()) ? this.currentBids.get(player.getUniqueId())
                            : 0.0;
            if (previousBid > 0.0) {
                if (bid < previousBid + this.getBidIncrement()) {
                    this.plugin.getMessageHandler().sendMessage((CommandSender) player, this.plugin.getMessage("messages.error.bidTooLow"));
                    return;
                }
                if (bid <= previousBid) {
                    this.plugin.getMessageHandler().sendMessage((CommandSender) player,
                                    this.plugin.getMessage("messages.error.sealedAuctionHaveHigherBid"));
                    return;
                }
                raiseAmount -= previousBid;
            }
            if (this.plugin.getEconomy().getBalance((OfflinePlayer) player) < raiseAmount) {
                this.plugin.getMessageHandler().sendMessage((CommandSender) player,
                                this.plugin.getMessage("messages.error.insufficientBalance"));
            } else {
                this.currentBids.put(player.getUniqueId(), bid);
                this.amountOfBids.put(player.getUniqueId(),
                                this.amountOfBids.containsKey(player.getUniqueId()) ? (this.amountOfBids.get(player.getUniqueId()) + 1)
                                                : 1);
                if (bid > this.winningBid) {
                    this.winningBid = bid;
                    this.topBidderName = player.getName();
                    this.topBidderUUID = player.getUniqueId();
                }
                if (previousBid == 0.0) {
                    final String message = this.plugin.getMessage("messages.auctionFormattable.sealedAuction.bid").replace("[bid]",
                                    this.plugin.formatDouble(bid));
                    this.plugin.getMessageHandler().sendMessage((CommandSender) player, message);
                } else {
                    final String message = this.plugin.getMessage("messages.auctionFormattable.sealedAuction.raise")
                                    .replace("[bid]", this.plugin.formatDouble(bid))
                                    .replace("[previous]", this.plugin.formatDouble(previousBid));
                    this.plugin.getMessageHandler().sendMessage((CommandSender) player, message);
                }
                this.plugin.getEconomy().withdrawPlayer((OfflinePlayer) player, raiseAmount);
                for (final AuctionModule module : this.modules) {
                    if (module.canTrigger()) {
                        module.trigger();
                    }
                }
            }
        }
    }

    @Override
    public void end(final boolean broadcast) {
        super.end(broadcast);
        if (this.getTopBidder() != null) {
            for (final Map.Entry<UUID, Double> bidder : this.currentBids.entrySet()) {
                if (!bidder.getKey().equals(this.getTopBidder())) {
                    final OfflinePlayer player = Bukkit.getOfflinePlayer((UUID) bidder.getKey());
                    final double bid = bidder.getValue();
                    this.plugin.getEconomy().depositPlayer(player, bid);
                }
            }
        }
    }

    @Override
    public void broadcastBid() {}

    @Override
    protected void startMessages() {
        super.startMessages();
        this.plugin.getMessageHandler().broadcast(this.plugin.getMessage("messages.notifySealedAuction"), this, false);
    }

    @Override
    public void returnMoneyToAll() {
        for (final Map.Entry<UUID, Double> bidder : this.currentBids.entrySet()) {
            final OfflinePlayer player = Bukkit.getOfflinePlayer((UUID) bidder.getKey());
            final double bid = bidder.getValue();
            this.plugin.getEconomy().depositPlayer(player, bid);
        }
    }

    public static class SealedAuctionBuilder extends DefaultAuctionBuilder {

        public SealedAuctionBuilder(final AuctionPlugin plugin) {
            super(plugin);
        }

        @Override
        public Auction build() {
            super.defaults();
            return new SealedAuction(this.plugin, this.ownerId, this.ownerName, this.bid, this.reward, this.autowin, this.increment, this.time);
        }
    }
}
