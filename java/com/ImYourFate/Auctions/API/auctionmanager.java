package play.dahp.us.auctions.api;

import play.dahp.us.auctions.api.messages.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public interface AuctionManager
{
    Auction getCurrentAuction();
    
    void setCurrentAuction(final Auction p0);
    
    boolean canStartNewAuction();
    
    void setCanStartNewAuction(final boolean p0);
    
    Queue<Auction> getQueue();
    
    MessageHandler getMessageHandler();
    
    void setMessageHandler(final MessageHandler p0);
    
    void addMessageGroup(final MessageRecipientGroup p0);
    
    Collection<MessageRecipientGroup> getMessageGroups();
    
    void addAuctionToQueue(final Auction p0);
    
    int getQueuePosition(final Player p0);
    
    boolean hasAuctionInQueue(final Player p0);
    
    boolean hasActiveAuction(final Player p0);
    
    boolean isAuctioningDisabled();
    
    void setAuctioningDisabled(final boolean p0);
    
    void startNextAuction();
    
    boolean isBannedMaterial(final Material p0);
}
