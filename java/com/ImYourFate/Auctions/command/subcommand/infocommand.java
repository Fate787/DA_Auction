package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;

public class InfoCommand extends AuctionSubCommand
{
    public InfoCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.info", new String[] { "info", "i" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (this.plugin.getManager().getCurrentAuction() == null) {
            this.plugin.getManager().getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.error.noCurrentAuction"));
        }
        else {
            this.plugin.getManager().getMessageHandler().sendAuctionInformation(sender, this.plugin.getManager().getCurrentAuction());
        }
        return false;
    }
}
