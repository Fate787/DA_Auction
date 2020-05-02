package play.dahp.us.auctions.api.event;

import org.bukkit.event.*;
import play.dahp.us.auctions.api.*;

public abstract class AuctionEvent extends Event
{
    protected Auction auction;
    
    public AuctionEvent(final Auction auction) {
        this.auction = auction;
    }
    
    public final Auction getAuction() {
        return this.auction;
    }
}
