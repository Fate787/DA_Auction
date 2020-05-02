package play.dahp.us.auctions.command;

import play.dahp.us.auctions.*;
import play.dahp.us.auctions.command.subcommand.*;
import org.bukkit.event.server.*;
import org.bukkit.event.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.*;

public class AuctionCommandHandler implements CommandExecutor, Listener
{
    private AuctionPlugin plugin;
    private Set<AuctionSubCommand> commands;
    
    public AuctionCommandHandler(final AuctionPlugin plugin) {
        this.commands = new HashSet<AuctionSubCommand>();
        this.plugin = plugin;
        this.addCommand(new BidCommand(plugin));
        this.addCommand(new CancelCommand(plugin));
        this.addCommand(new EndCommand(plugin));
        this.addCommand(new IgnoreCommand(plugin));
        this.addCommand(new ImpoundCommand(plugin));
        this.addCommand(new InfoCommand(plugin));
        this.addCommand(new ReloadCommand(plugin));
        this.addCommand(new SpamCommand(plugin));
        this.addCommand(new StartCommand(plugin));
        this.addCommand(new ToggleCommand(plugin));
        this.addCommand(new QueueCommand(plugin));
    }
    
    public void addCommand(final AuctionSubCommand command) {
        this.commands.add(command);
    }
    
    @EventHandler
    public void onPluginDisable(final PluginDisableEvent event) {
        if (this.plugin.equals((Object)event.getPlugin())) {
            this.commands.clear();
        }
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
        if (args.length == 0 && !command.getName().equalsIgnoreCase("bid")) {
            this.sendMenu(sender);
        }
        else {
            String sub;
            if (command.getName().equalsIgnoreCase("bid")) {
                sub = "bid";
                args = this.concat(new String[] { "auction" }, args);
            }
            else {
                sub = args[0];
            }
            for (final AuctionSubCommand cmd : this.commands) {
                if (cmd.canTrigger(sub)) {
                    if (!sender.hasPermission("auctions.command.*") && !sender.hasPermission(cmd.getPermission())) {
                        this.plugin.getMessageHandler().sendMessage(sender, this.plugin.getMessage("messages.error.insufficientPermissions"));
                    }
                    else {
                        cmd.onCommand(sender, command, label, args);
                    }
                    return true;
                }
            }
            this.sendMenu(sender);
        }
        return true;
    }
    
    private <T> T[] concat(final T[] first, final T[] second) {
        final T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    
    public void sendMenu(final CommandSender sender) {
        for (final String message : this.plugin.getConfig().getStringList("messages.helpMenu")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
