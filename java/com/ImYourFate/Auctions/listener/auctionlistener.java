package play.dahp.us.auctions.listener;

import play.dahp.us.auctions.*;
import play.dahp.us.auctions.api.event.*;
import org.bukkit.command.*;
import play.dahp.us.auctions.api.*;
import java.util.*;
import org.bukkit.event.*;

public class AuctionListener implements Listener
{
    private AuctionPlugin plugin;
    
    public AuctionListener(final AuctionPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onAuctionEnd(final AuctionEndEvent event) {
        if (!this.plugin.getConfig().getBoolean("auctionSettings.commandsAfterAuction.enable", false)) {
            return;
        }
        final Auction auction = event.getAuction();
        if (this.plugin.getConfig().getBoolean("auctionSettings.commandsAfterAuction.onlyIfSold", true) && auction.getTopBidder() == null) {
            return;
        }
        if (!this.plugin.getConfig().isList("auctionSettings.commandsAfterAuction.commands")) {
            return;
        }
        final List<String> commands = (List<String>)this.plugin.getConfig().getStringList("auctionSettings.commandsAfterAuction.commands");
        final String winner = (auction.getTopBidderName() == null) ? "[winner]" : auction.getTopBidderName();
        for (String command : commands) {
            command = command.replace("[owner]", auction.getOwnerName());
            command = command.replace("[winner]", winner);
            this.plugin.getServer().dispatchCommand((CommandSender)this.plugin.getServer().getConsoleSender(), command);
        }
    }
}
