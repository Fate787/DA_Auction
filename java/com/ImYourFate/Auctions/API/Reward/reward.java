package play.dahp.us.auctions.api.reward;

import org.bukkit.configuration.serialization.*;
import org.bukkit.entity.*;

public interface Reward extends ConfigurationSerializable
{
    void giveItem(final Player p0);
    
    String getName();
    
    int getAmount();
}
