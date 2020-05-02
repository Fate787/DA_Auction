package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.*;
import play.dahp.us.auctions.api.messages.*;

public class CancelCommand extends AuctionSubCommand
{
    public CancelCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.cancel", new String[] { "cancel", "c", "can" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final AuctionManager manager = this.plugin.getManager();
        final MessageHandler handler = manager.getMessageHandler();
        if (manager.getCurrentAuction() == null) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.noCurrentAuction"));
        }
        else if (sender instanceof Player && manager.getMessageHandler().isIgnoring(sender)) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.currentlyIgnoring"));
        }
        else if (manager.getCurrentAuction().getTimeLeft() < this.plugin.getConfig().getInt("auctionSettings.mustCancelBefore", 15) && !sender.hasPermission("auctions.bypass.cancel.timer")) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.cantCancelNow"));
        }
        else if (this.plugin.getConfig().getInt("auctionSettings.mustCancelAfter", -1) != -1 && manager.getCurrentAuction().getTimeLeft() > this.plugin.getConfig().getInt("auctionSettings.mustCancelAfter", -1) && !sender.hasPermission("auctions.bypass.cancel.timer")) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.cantCancelNow"));
        }
        else if (sender instanceof Player && !manager.getCurrentAuction().getOwner().equals(((Player)sender).getUniqueId()) && !sender.hasPermission("auctions.bypass.cancel.otherauctions")) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.notYourAuction"));
        }
        else {
            manager.getCurrentAuction().cancel();
        }
        return false;
    }
}
