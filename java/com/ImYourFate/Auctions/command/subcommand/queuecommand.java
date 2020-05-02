package cplay.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import play.dahp.us.auctions.api.*;
import java.util.*;

public class QueueCommand extends AuctionSubCommand
{
    public QueueCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.queue", new String[] { "queue", "q" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final AuctionManager manager = this.plugin.getManager();
        final Queue<Auction> queue = manager.getQueue();
        if (queue.isEmpty()) {
            manager.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.error.noAuctionsInQueue"));
        }
        else {
            int queuePosition = 1;
            manager.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.queueInfoHeader"));
            for (final Auction auction : queue) {
                final String message = this.plugin.getMessage("messages.auctionFormattable.queueInfoLine").replace("[queuepos]", Integer.toString(queuePosition));
                manager.getMessageHandler().sendMessage(sender, message, auction);
                ++queuePosition;
            }
        }
        return true;
    }
}
