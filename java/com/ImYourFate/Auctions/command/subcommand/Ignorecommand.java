package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.messages.*;

public class IgnoreCommand extends AuctionSubCommand
{
    public IgnoreCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.ignore", new String[] { "ignore" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can ignore the plugin");
        }
        else {
            final MessageHandler handler = this.plugin.getManager().getMessageHandler();
            final Player player = (Player)sender;
            if (handler.isIgnoring((CommandSender)player)) {
                handler.removeIgnoring((CommandSender)player);
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.noLongerIgnoring"));
            }
            else {
                handler.addIgnoring((CommandSender)player);
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.nowIgnoring"));
            }
        }
        return false;
    }
}
