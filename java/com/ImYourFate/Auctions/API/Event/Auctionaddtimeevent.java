package play.dahp.us.auctions.api.event;

import org.bukkit.event.*;
import play.dahp.us.auctions.api.*;

public class AuctionAddTimeEvent extends AuctionEvent implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancelled;
    private int secondsToAdd;
    
    public AuctionAddTimeEvent(final Auction auction, final int secondsToAdd) {
        super(auction);
        this.secondsToAdd = secondsToAdd;
    }
    
    public int getSecondsToAdd() {
        return this.secondsToAdd;
    }
    
    public void setSecondsToAdd(final int secondsToAdd) {
        this.secondsToAdd = secondsToAdd;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
    
    public HandlerList getHandlers() {
        return AuctionAddTimeEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return AuctionAddTimeEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
