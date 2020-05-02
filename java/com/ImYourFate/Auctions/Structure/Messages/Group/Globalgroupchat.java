package play.dahp.us.auctions.structure.messages.group;

import play.dahp.us.auctions.api.messages.*;
import org.bukkit.command.*;
import org.bukkit.*;

public class GlobalChatGroup implements MessageRecipientGroup
{
    @Override
    public Iterable<? extends CommandSender> getRecipients() {
        return (Iterable<? extends CommandSender>)Bukkit.getOnlinePlayers();
    }
}
