package play.dahp.us.auctions.command;

import org.bukkit.command.*;
import play.dahp.us.auctions.*;
import java.util.*;

public abstract class AuctionSubCommand implements CommandExecutor
{
    protected final AuctionPlugin plugin;
    private Set<String> aliases;
    private String permission;
    
    public AuctionSubCommand(final AuctionPlugin plugin, final String permission, final String... aliases) {
        this.aliases = new HashSet<String>();
        this.plugin = plugin;
        this.permission = permission;
        this.aliases.addAll(Arrays.asList(aliases));
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public boolean canTrigger(final String alias) {
        return this.aliases.contains(alias.toLowerCase());
    }
}
