package play.dahp.us.auctions.structure.messages.group;

import play.dahp.us.auctions.api.messages.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.dthielke.herochat.*;
import java.util.*;

public class HerochatGroup implements MessageRecipientGroup
{
    private AuctionPlugin plugin;
    
    public HerochatGroup(final AuctionPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public Iterable<? extends CommandSender> getRecipients() {
        return (Iterable<? extends CommandSender>)(this.isHerochatEnabled() ? this.getChannelPlayers(this.plugin.getMessage("integration.herochat.channel")) : new HashSet<Player>());
    }
    
    public boolean isHerochatEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Herochat");
    }
    
    public boolean isValidChannel(final String channel) {
        final Channel ch = Herochat.getChannelManager().getChannel(channel);
        return ch != null;
    }
    
    public Set<Player> getChannelPlayers(final String channel) {
        final Set<Player> players = new HashSet<Player>();
        if (!this.isValidChannel(channel)) {
            this.plugin.getLogger().info("\"" + channel + "\" is not a valid channel, sending message to nobody.");
            return players;
        }
        final Channel ch = Herochat.getChannelManager().getChannel(channel);
        final Set<Chatter> members = (Set<Chatter>)ch.getMembers();
        for (final Chatter c : members) {
            players.add(c.getPlayer());
        }
        return players;
    }
}
