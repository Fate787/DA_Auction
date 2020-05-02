package play.dahp.us.auctions.api.messages;

import org.bukkit.command.*;

public interface MessageRecipientGroup
{
    Iterable<? extends CommandSender> getRecipients();
}
