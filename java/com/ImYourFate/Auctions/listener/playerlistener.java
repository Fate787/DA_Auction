package play.dahp.us.auctions.listener;

import play.dahp.us.auctions.*;
import java.util.*;
import play.dahp.us.auctions.api.reward.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import play.dahp.us.auctions.api.*;

public class PlayerListener implements Listener
{
    private AuctionPlugin plugin;
    
    public PlayerListener(final AuctionPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Reward reward = this.plugin.getOfflineReward(player.getUniqueId());
        if (reward != null) {
            this.plugin.getServer().getScheduler().runTaskLater((Plugin)this.plugin, (Runnable)new Runnable() {
                @Override
                public void run() {
                    final Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        PlayerListener.this.plugin.getLogger().info("Giving back saved items of offline player " + player.getName() + " (uuid: " + player.getUniqueId() + ")");
                        PlayerListener.this.plugin.getMessageHandler().sendMessage((CommandSender)player, PlayerListener.this.plugin.getMessage("messages.savedItemReturn"));
                        reward.giveItem(player);
                        PlayerListener.this.plugin.removeOfflineReward(player.getUniqueId());
                    }
                }
            }, this.plugin.getConfig().getLong("general.offlineRewardTickDelay"));
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage().split(" ")[0];
        if (!player.hasPermission("auctions.bypass.general.blockedcommands") && this.plugin.getConfig().isList("general.blockedCommands") && this.plugin.getConfig().getStringList("general.blockedCommands").contains(command.toLowerCase())) {
            if (this.plugin.getConfig().getBoolean("general.blockCommands.ifAuctioning", false) && this.plugin.getManager().hasActiveAuction(player)) {
                event.setCancelled(true);
                this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantUseCommandWhileAuctioning"));
            }
            else if (this.plugin.getConfig().getBoolean("general.blockCommands.ifQueued", false) && this.plugin.getManager().hasAuctionInQueue(player)) {
                event.setCancelled(true);
                this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantUseCommandWhileQueued"));
            }
            else if (this.plugin.getConfig().getBoolean("general.blockCommands.ifTopBidder", false) && this.plugin.getManager().getCurrentAuction() != null && player.getUniqueId().equals(this.plugin.getManager().getCurrentAuction().getTopBidder())) {
                event.setCancelled(true);
                this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantUseCommandWhileTopBidder"));
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final World target = event.getTo().getWorld();
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && !player.hasPermission("auctions.bypass.general.disabledworld") && this.plugin.isWorldDisabled(target)) {
            if (this.plugin.getManager().hasActiveAuction(player) || this.plugin.getManager().hasAuctionInQueue(player)) {
                event.setCancelled(true);
                this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantTeleportToDisabledWorld"));
            }
            else {
                final Auction auction = this.plugin.getManager().getCurrentAuction();
                if (auction != null && player.getUniqueId().equals(auction.getTopBidder())) {
                    event.setCancelled(true);
                    this.plugin.getMessageHandler().sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantTeleportToDisabledWorld"));
                }
            }
        }
    }
}
