package play.dahp.us.auctions.api.event;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.*;

public class AuctionCreateEvent extends AuctionEvent implements Cancellable
{
    private static final HandlerList handlers;
    private final Player who;
    private boolean cancelled;
    
    public AuctionCreateEvent(final Auction auction, final Player who) {
        super(auction);
        this.who = who;
    }
    
    public Player getPlayer() {
        return this.who;
    }
    
    public HandlerList getHandlers() {
        return AuctionCreateEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return AuctionCreateEvent.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    static {
        handlers = new HandlerList();
    }
}
