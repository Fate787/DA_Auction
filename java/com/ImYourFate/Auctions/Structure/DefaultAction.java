package play.dahp.us.auctions.structure;

import play.dahp.us.auctions.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.*;
import play.dahp.us.auctions.api.module.*;
import play.dahp.us.auctionss.api.*;
import play.dahp.us.auctions.api.messages.*;
import play.dahp.us.auctions.api.event.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import play.dahp.us.auctions.api.reward.*;
import java.util.*;

public class DefaultAuction extends AbstractAuction
{
    public DefaultAuction(final AuctionPlugin plugin, final AuctionType type) {
        this.plugin = plugin;
        this.type = type;
    }
    
    @Override
    public void placeBid(final Player player, final double bid) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        if (bid < (this.hasBids() ? (this.getTopBid() + this.getBidIncrement()) : this.getStartPrice())) {
            this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.bidTooLow"));
        }
        else if (this.plugin.getEconomy().getBalance((OfflinePlayer)player) < bid) {
            this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.insufficientBalance"));
        }
        else if (player.getUniqueId().equals(this.getTopBidder())) {
            this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.alreadyTopBidder"));
        }
        else {
            if (this.getTopBidder() != null) {
                final OfflinePlayer oldPlayer = Bukkit.getOfflinePlayer(this.getTopBidder());
                this.plugin.getEconomy().depositPlayer(oldPlayer, this.getTopBid());
            }
            this.hasBidBeenPlaced = true;
            this.winningBid = bid;
            this.topBidderName = player.getName();
            this.topBidderUUID = player.getUniqueId();
            this.plugin.getEconomy().withdrawPlayer((OfflinePlayer)player, bid);
            this.broadcastBid();
            final String message = this.plugin.getMessage("messages.bid").replace("[bid]", this.plugin.formatDouble(bid));
            this.plugin.getMessageHandler().sendMessage((CommandSender)player, message);
            for (final AuctionModule module : this.modules) {
                if (module.canTrigger()) {
                    module.trigger();
                }
            }
        }
    }
    
    @Override
    protected void startMessages() {
        final MessageHandler handler = this.plugin.getManager().getMessageHandler();
        handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.start"), this, false);
        handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.price"), this, false);
        handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.increment"), this, false);
        if (this.getAutowin() != -1.0) {
            handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.autowin"), this, false);
        }
    }
    
    @Override
    public void impound() {
        this.timerTask.cancel();
        this.timerTask = null;
        this.runNextAuctionTimer();
        this.returnMoneyToAll();
        this.plugin.getManager().setCurrentAuction(null);
    }
    
    @Override
    public void cancel() {
        final Player owner = Bukkit.getPlayer(this.ownerUUID);
        this.timerTask.cancel();
        this.timerTask = null;
        if (this.plugin.isEnabled()) {
            this.runNextAuctionTimer();
        }
        this.returnMoneyToAll();
        final MessageHandler handler = this.plugin.getManager().getMessageHandler();
        handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.cancelled"), this, false);
        if (owner == null) {
            this.plugin.getLogger().info("Saving items of offline player " + this.getOwnerName() + " (uuid: " + this.getOwner() + ")");
            this.plugin.saveOfflinePlayer(this.getOwner(), this.getReward());
        }
        else {
            this.getReward().giveItem(owner);
            handler.sendMessage((CommandSender)owner, this.plugin.getMessage("messages.ownerItemReturn"));
        }
        this.plugin.getManager().setCurrentAuction(null);
    }
    
    @Override
    public void end(final boolean broadcast) {
        final MessageHandler handler = this.plugin.getManager().getMessageHandler();
        final Player owner = Bukkit.getPlayer(this.getOwner());
        final AuctionEndEvent event = new AuctionEndEvent(this);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (this.timerTask != null) {
            this.timerTask.cancel();
            this.timerTask = null;
        }
        if (this.plugin.isEnabled()) {
            this.runNextAuctionTimer();
        }
        if (this.getTopBidder() != null) {
            final Player winner = Bukkit.getPlayer(this.getTopBidder());
            if (broadcast && (this.autowin == -1.0 || this.getTopBid() < this.getAutowin())) {
                handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.end"), this, false);
            }
            if (this.getTopBid() > 0.0) {
                final double winnings = this.getTopBid() - this.getTaxAmount();
                this.plugin.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(this.getOwner()), winnings);
                if (owner != null) {
                    if (this.getTax() > 0.0) {
                        handler.sendMessage((CommandSender)owner, this.plugin.getMessage("messages.auctionFormattable.endTax"), this);
                    }
                    handler.sendMessage((CommandSender)owner, this.plugin.getMessage("messages.auctionFormattable.endNotifyOwner"), this);
                }
            }
            if (winner == null) {
                this.plugin.getLogger().info("Saving items of offline player " + this.getTopBidderName() + " (uuid: " + this.getTopBidder() + ")");
                this.plugin.saveOfflinePlayer(this.getTopBidder(), this.getReward());
            }
            else {
                this.getReward().giveItem(winner);
                handler.sendMessage((CommandSender)winner, this.plugin.getMessage("messages.auctionFormattable.winner"), this);
            }
        }
        else {
            if (broadcast) {
                handler.broadcast(this.plugin.getMessage("messages.auctionFormattable.endNoBid"), this, false);
            }
            if (owner != null) {
                handler.sendMessage((CommandSender)owner, this.plugin.getMessage("messages.ownerItemReturn"));
                this.getReward().giveItem(owner);
            }
            else {
                this.plugin.getLogger().info("Saving items of offline player " + this.getOwnerName() + " (uuid: " + this.getOwner() + ")");
                this.plugin.saveOfflinePlayer(this.getOwner(), this.getReward());
            }
        }
        this.plugin.getManager().setCurrentAuction(null);
    }
    
    @Override
    public void returnMoneyToAll() {
        if (this.getTopBidder() != null) {
            final OfflinePlayer topBidder = Bukkit.getOfflinePlayer(this.getTopBidder());
            this.plugin.getEconomy().depositPlayer(topBidder, this.getTopBid());
        }
    }
    
    @Override
    public void broadcastBid() {
        this.plugin.getMessageHandler().broadcast(this.plugin.getMessage("messages.auctionFormattable.bid"), this, true);
    }
    
    @Override
    public void runNextAuctionTimer() {
        if (this.plugin.isEnabled()) {
            this.plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    DefaultAuction.this.plugin.getManager().setCanStartNewAuction(true);
                    if (DefaultAuction.this.plugin.getManager().getCurrentAuction() == null) {
                        DefaultAuction.this.plugin.getManager().startNextAuction();
                    }
                }
            }, this.plugin.getConfig().getLong("auctionSettings.delayBetween", 5L) * 20L);
        }
    }
    
    public abstract static class DefaultAuctionBuilder implements Auction.Builder
    {
        protected AuctionPlugin plugin;
        protected double increment;
        protected int time;
        protected Reward reward;
        protected double bid;
        protected double autowin;
        protected UUID ownerId;
        protected String ownerName;
        
        public DefaultAuctionBuilder(final AuctionPlugin plugin) {
            this.increment = -1.0;
            this.time = -1;
            this.bid = -1.0;
            this.autowin = -1.0;
            this.plugin = plugin;
        }
        
        @Override
        public Auction.Builder bidIncrement(final double increment) {
            this.increment = increment;
            return this;
        }
        
        @Override
        public Auction.Builder owner(final Player owner) {
            this.ownerId = owner.getUniqueId();
            this.ownerName = owner.getName();
            return this;
        }
        
        @Override
        public Auction.Builder time(final int time) {
            this.time = time;
            return this;
        }
        
        @Override
        public Auction.Builder reward(final Reward reward) {
            this.reward = reward;
            return this;
        }
        
        @Override
        public Auction.Builder topBid(final double bid) {
            this.bid = bid;
            return this;
        }
        
        @Override
        public Auction.Builder autowin(final double autowin) {
            this.autowin = autowin;
            return this;
        }
        
        protected void defaults() {
            if (this.reward == null) {
                throw new IllegalStateException("reward cannot be null");
            }
            if (this.bid == -1.0) {
                throw new IllegalStateException("bid hasn't been set");
            }
            if (this.increment == -1.0) {
                this.increment = this.plugin.getConfig().getInt("auctionSettings.defaultBidIncrement", 50);
            }
            if (this.time == -1) {
                this.time = this.plugin.getConfig().getInt("auctionSettings.startTime", 30);
            }
        }
    }
}
