package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;

public class ReloadCommand extends AuctionSubCommand
{
    public ReloadCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.reload", new String[] { "reload", "r", "rel" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        this.plugin.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.pluginReloaded"));
        this.plugin.reloadConfig();
        this.plugin.loadConfig();
        return false;
    }
}
