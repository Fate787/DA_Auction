package play.dahp.us.auctions.structure.module;

import play.dahp.us.auctions.api.module.*;
import play.dahp.us.auctions.*;
import play.dahp.us.auctions.api.*;
import play.dahp.us.auctions.api.event.*;
import org.bukkit.*;
import org.bukkit.event.*;
import com.sainttx.auctions.util.*;

public class AntiSnipeModule implements AuctionModule
{
    private AuctionPlugin plugin;
    private Auction auction;
    private int snipeCount;
    
    public AntiSnipeModule(final AuctionPlugin plugin, final Auction auction) {
        if (auction == null) {
            throw new IllegalArgumentException("auction cannot be null");
        }
        this.plugin = plugin;
        this.auction = auction;
    }
    
    @Override
    public boolean canTrigger() {
        return this.plugin.getConfig().getBoolean("auctionSettings.antiSnipe.enable", true) && this.auction.getTimeLeft() <= this.plugin.getConfig().getInt("auctionSettings.antiSnipe.timeThreshold", 3) && this.snipeCount < this.plugin.getConfig().getInt("auctionSettings.antiSnipe.maxPerAuction", 3) && (this.auction.getAutowin() == -1.0 || this.auction.getTopBid() < this.auction.getAutowin());
    }
    
    @Override
    public void trigger() {
        final int secondsToAdd = this.plugin.getConfig().getInt("auctionSettings.antiSnipe.addSeconds", 5);
        final AuctionAddTimeEvent event = new AuctionAddTimeEvent(this.auction, secondsToAdd);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return;
        }
        ++this.snipeCount;
        this.auction.setTimeLeft(this.auction.getTimeLeft() + event.getSecondsToAdd());
        final String message = this.plugin.getMessage("messages.auctionFormattable.antiSnipeAdd").replace("[snipetime]", TimeUtil.getFormattedTime(event.getSecondsToAdd(), this.plugin.getConfig().getBoolean("general.shortenedTimeFormat", false)));
        this.plugin.getManager().getMessageHandler().broadcast(message, this.auction, false);
    }
}
