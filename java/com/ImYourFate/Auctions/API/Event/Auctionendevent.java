package play.dahp.us.auctions.api.event;

import org.bukkit.event.*;
import play.dahp.us.auctions.api.*;

public class AuctionEndEvent extends AuctionEvent
{
    private static final HandlerList handlers;
    
    public AuctionEndEvent(final Auction auction) {
        super(auction);
    }
    
    public HandlerList getHandlers() {
        return AuctionEndEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return AuctionEndEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
