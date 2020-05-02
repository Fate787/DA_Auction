package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.event.*;
import org.bukkit.*;
import org.bukkit.event.*;
import play.dahp.us.auctions.api.messages.*;
import play.dahp.us.auctions.api.*;

public class BidCommand extends AuctionSubCommand
{
    public BidCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.bid", new String[] { "bid", "b" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final MessageHandler handler = this.plugin.getManager().getMessageHandler();
        final Auction auction = this.plugin.getManager().getCurrentAuction();
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can place bids on auctions");
        }
        else if (args.length < 2 && !this.plugin.getConfig().getBoolean("auctionSettings.canBidAutomatically", true)) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.bidSyntax"));
        }
        else if (auction == null) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.noCurrentAuction"));
        }
        else if (auction.getType() == AuctionType.SEALED && args.length < 2) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.bidSyntax"));
        }
        else {
            final Player player = (Player)sender;
            double bid;
            try {
                bid = ((args.length < 2) ? (auction.hasBids() ? (auction.getTopBid() + auction.getBidIncrement()) : auction.getStartPrice()) : Double.parseDouble(args[1]));
            }
            catch (NumberFormatException ex) {
                handler.sendMessage(sender, this.plugin.getMessage("messages.error.invalidNumberEntered"));
                return true;
            }
            if (!player.hasPermission("auctions.bypass.general.disabledworld") && this.plugin.isWorldDisabled(player.getWorld())) {
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantUsePluginInWorld"));
            }
            else if (handler.isIgnoring((CommandSender)player)) {
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.currentlyIgnoring"));
            }
            else if (auction.getOwner().equals(player.getUniqueId())) {
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.bidOnOwnAuction"));
            }
            else {
                final AuctionPreBidEvent event = new AuctionPreBidEvent(auction, player, bid);
                Bukkit.getPluginManager().callEvent((Event)event);
                if (event.isCancelled()) {
                    return true;
                }
                auction.placeBid(player, bid);
            }
        }
        return true;
    }
}
