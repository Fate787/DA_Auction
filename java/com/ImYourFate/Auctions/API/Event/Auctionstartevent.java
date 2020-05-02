package play.dahp.us.auctions.api.event;

import org.bukkit.event.*;
import play.dahp.us.auctions.api.*;

public class AuctionStartEvent extends AuctionEvent
{
    private static final HandlerList handlers;
    
    public AuctionStartEvent(final Auction auction) {
        super(auction);
    }
    
    public HandlerList getHandlers() {
        return AuctionStartEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return AuctionStartEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
