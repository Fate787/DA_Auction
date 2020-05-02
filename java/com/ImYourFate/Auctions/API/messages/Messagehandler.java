package play.dahp.us.auctions.api.messages;

import play.dahp.us.auctions.api.*;
import org.bukkit.command.*;

public interface MessageHandler
{
    void broadcast(final String p0, final boolean p1);
    
    void broadcast(final String p0, final Auction p1, final boolean p2);
    
    void sendMessage(final CommandSender p0, final String p1);
    
    void sendMessage(final CommandSender p0, final String p1, final Auction p2);
    
    void sendAuctionInformation(final CommandSender p0, final Auction p1);
    
    boolean isIgnoring(final CommandSender p0);
    
    void addIgnoring(final CommandSender p0);
    
    boolean removeIgnoring(final CommandSender p0);
    
    public interface MessageFormatter
    {
        String format(final String p0);
        
        String format(final String p0, final Auction p1);
    }
}
