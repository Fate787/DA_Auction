package play.dahp.us.auctions.structure.module;

import play.dahp.us.auctions.api.module.*;
import play.dahp.us.auctions.*;
import play.dahp.us.auctions.api.*;

public class AutoWinModule implements AuctionModule
{
    private AuctionPlugin plugin;
    private Auction auction;
    private double trigger;
    
    public AutoWinModule(final AuctionPlugin plugin, final Auction auction, final double trigger) {
        if (auction == null) {
            throw new IllegalArgumentException("auction cannot be null");
        }
        this.plugin = plugin;
        this.auction = auction;
        this.trigger = trigger;
    }
    
    @Override
    public boolean canTrigger() {
        return this.auction.getTopBid() >= this.trigger;
    }
    
    @Override
    public void trigger() {
        this.plugin.getMessageHandler().broadcast(this.plugin.getMessage("messages.auctionFormattable.endByAutowin"), this.auction, false);
        this.auction.end(true);
    }
}
