package play.dahp.us.auctions.api;

import play.dahp.us.auctions.api.messages.*;
import play.dahp.us.auctions.*;
import play.dahp.us.auctions.structure.auction.*;

public class Auctions
{
    private static AuctionManager manager;
    
    public static void setManager(final AuctionManager manager) {
        Auctions.manager = manager;
    }
    
    public static AuctionManager getManager() {
        return Auctions.manager;
    }
    
    public static MessageHandler getMessageHandler() {
        return Auctions.manager.getMessageHandler();
    }
    
    public static Auction.Builder getAuctionBuilder(final AuctionPlugin plugin, final AuctionType type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        switch (type) {
            case STANDARD: {
                return new StandardAuction.StandardAuctionBuilder(plugin);
            }
            case SEALED: {
                return new SealedAuction.SealedAuctionBuilder(plugin);
            }
            default: {
                return null;
            }
        }
    }
}
