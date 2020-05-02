package play.dahp.us.auctions.api.messages;

import java.util.*;

public interface MessageHandlerAddon
{
    public interface SpammyMessagePreventer extends MessageHandlerAddon
    {
        void addIgnoringSpam(final UUID p0);
        
        void removeIgnoringSpam(final UUID p0);
        
        boolean isIgnoringSpam(final UUID p0);
    }
}
