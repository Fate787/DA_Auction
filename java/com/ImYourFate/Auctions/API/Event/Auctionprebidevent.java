package play.dahp.us.auctions.api.event;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.*;

public class AuctionPreBidEvent extends AuctionEvent implements Cancellable
{
    private static final HandlerList handlers;
    private final Player who;
    private final double amount;
    private boolean cancelled;
    
    public AuctionPreBidEvent(final Auction auction, final Player who, final double amount) {
        super(auction);
        this.who = who;
        this.amount = amount;
    }
    
    public Player getPlayer() {
        return this.who;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public HandlerList getHandlers() {
        return AuctionPreBidEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return AuctionPreBidEvent.handlers;
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
