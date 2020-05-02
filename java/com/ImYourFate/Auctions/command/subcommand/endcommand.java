package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.*;

public class EndCommand extends AuctionSubCommand
{
    public EndCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.end", new String[] { "end", "e" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final AuctionManager manager = this.plugin.getManager();
        if (manager.getCurrentAuction() == null) {
            manager.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.error.noCurrentAuction"));
        }
        else if (!sender.hasPermission("auctions.bypass.end.otherauctions") && sender instanceof Player && !manager.getCurrentAuction().getOwner().equals(((Player)sender).getUniqueId())) {
            manager.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.error.notYourAuction"));
        }
        else {
            manager.getCurrentAuction().end(true);
            manager.setCurrentAuction(null);
        }
        return false;
    }
}
