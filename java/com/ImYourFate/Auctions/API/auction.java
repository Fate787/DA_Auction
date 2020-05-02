package play.dahp.us.auctions.api;

import play.dahp.us.auctions.api.reward.*;
import org.bukkit.entity.*;
import java.util.*;
import cplay.dahp.us.auctions.api.module.*;

public interface Auction
{
    UUID getOwner();
    
    String getOwnerName();
    
    boolean hasBids();
    
    boolean hasEnded();
    
    UUID getTopBidder();
    
    String getTopBidderName();
    
    Reward getReward();
    
    AuctionType getType();
    
    int getTimeLeft();
    
    void setTimeLeft(final int p0);
    
    void start();
    
    void impound();
    
    void cancel();
    
    void end(final boolean p0);
    
    double getBidIncrement();
    
    double getAutowin();
    
    double getTax();
    
    double getTaxAmount();
    
    double getTopBid();
    
    double getStartPrice();
    
    void placeBid(final Player p0, final double p1);
    
    Collection<AuctionModule> getModules();
    
    void addModule(final AuctionModule p0);
    
    boolean removeModule(final AuctionModule p0);
    
    public interface Builder
    {
        Auction build();
        
        Builder owner(final Player p0);
        
        Builder bidIncrement(final double p0);
        
        Builder time(final int p0);
        
        Builder reward(final Reward p0);
        
        Builder topBid(final double p0);
        
        Builder autowin(final double p0);
    }
    
    public interface Timer extends Runnable
    {
    }
}
