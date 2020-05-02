package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;

public class ToggleCommand extends AuctionSubCommand
{
    public ToggleCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.toggle", new String[] { "toggle", "t" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        this.plugin.getManager().setAuctioningDisabled(!this.plugin.getManager().isAuctioningDisabled());
        final String message = this.plugin.getManager().isAuctioningDisabled() ? "messages.auctionsDisabled" : "messages.auctionsEnabled";
        this.plugin.getManager().getMessageHandler().broadcast(this.plugin.getMessage(message), false);
        return false;
    }
}
