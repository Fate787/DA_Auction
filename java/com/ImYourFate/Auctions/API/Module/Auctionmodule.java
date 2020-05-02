package play.dahp.us.auctions.api.module;

public interface AuctionModule
{
    boolean canTrigger();
    
    void trigger();
}
