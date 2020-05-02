package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.messages.*;

public class SpamCommand extends AuctionSubCommand
{
    public SpamCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.spam", new String[] { "spam", "spammy", "hidespam", "togglespam" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can toggle spammy auction messages");
        }
        else if (!(this.plugin.getMessageHandler() instanceof MessageHandlerAddon.SpammyMessagePreventer)) {
            this.plugin.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.error.cantHideSpam"));
        }
        else {
            final MessageHandlerAddon.SpammyMessagePreventer preventer = (MessageHandlerAddon.SpammyMessagePreventer)this.plugin.getMessageHandler();
            final Player player = (Player)sender;
            if (!preventer.isIgnoringSpam(player.getUniqueId())) {
                preventer.addIgnoringSpam(player.getUniqueId());
                this.plugin.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.nowHidingSpam"));
            }
            else {
                preventer.removeIgnoringSpam(player.getUniqueId());
                this.plugin.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.noLongerHidingSpam"));
            }
        }
        return false;
    }
}
