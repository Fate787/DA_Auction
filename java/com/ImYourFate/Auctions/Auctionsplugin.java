package play.dahp.us.auctions;

import play.dahp.us.auctions.api.AuctionManager;
import play.dahp.us.auctions.api.Auctions;
import play.dahp.us.auctions.api.messages.MessageHandler;
import play.dahp.us.auctions.api.messages.MessageHandlerType;
import play.dahp.us.auctions.api.reward.ItemReward;
import play.dahp.us.auctions.api.reward.Reward;
import play.dahp.us.auctions.command.AuctionCommandHandler;
import play.dahp.us.auctions.hook.PlaceholderAPIHook;
import play.dahp.us.auctions.listener.AuctionListener;
import play.dahp.us.auctions.listener.PlayerListener;
import play.dahp.us.auctions.structure.messages.group.GlobalChatGroup;
import play.dahp.us.auctions.structure.messages.group.HerochatGroup;
import play.dahp.us.auctions.structure.messages.handler.ActionBarMessageHandler;
import play.dahp.us.auctions.structure.messages.handler.TextualMessageHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class AuctionPlugin extends JavaPlugin {

    private AuctionManager manager;
    private Economy economy;
    private YamlConfiguration itemsFile;
    private final File offlineFile;
    private YamlConfiguration offlineConfiguration;
    private Map<UUID, Reward> offlineRewardCache;

    public AuctionPlugin() {
        this.offlineFile = new File(this.getDataFolder(), "offline.yml");
        this.offlineRewardCache = new HashMap<UUID, Reward>();
    }

    public void onEnable() {
        this.saveDefaultConfig();

        Bukkit.getScheduler().runTask((Plugin) this, (Runnable) new Runnable() {

            @Override
            public void run() {
                try {
                    AuctionPlugin.this.economy = AuctionPlugin.this.getServer().getServicesManager().getRegistration(Economy.class)
                                    .getProvider();
                } catch (Throwable t) {
                    AuctionPlugin.this.getLogger().log(Level.SEVERE, "failed to find an economy provider, disabling...");
                    AuctionPlugin.this.getServer().getPluginManager().disablePlugin((Plugin) AuctionPlugin.this);
                }
            }
        });
        
        Auctions.setManager(this.manager = new AuctionManagerImpl(this));
        if (this.getConfig().getBoolean("integration.herochat.enable")) {
            this.manager.addMessageGroup(new HerochatGroup(this));
            this.getLogger().info("Added Herochat recipient group to the list of broadcast listeners");
        }
        
        if (this.getConfig().getBoolean("chatSettings.groups.global")) {
            this.manager.addMessageGroup(new GlobalChatGroup());
            this.getLogger().info("Added global chat recipient group to the list of broadcast listeners");
        }
        
        if (this.canRegisterPlaceholders()) {
            PlaceholderAPIHook.registerPlaceHolders(this);
            this.getLogger().info("Successfully registered PlaceholderAPI placeholders");
        } else {
            this.getLogger().info("PlaceholderAPI was not found, chat hooks have NOT been registered");
        }
        
        try {
            final MessageHandlerType type = MessageHandlerType.valueOf(this.getMessage("chatSettings.handler"));
            switch (type) {
                case ACTION_BAR: {
                    try {
                        this.manager.setMessageHandler(new ActionBarMessageHandler(this));
                        this.getLogger().info("Message handler has been set to ACTION_BAR");
                    } catch (IllegalStateException e) {
                        this.getLogger().info(
                                        "Message handler type ACTION_BAR is unavailable for this Minecraft version. Defaulting to TEXT based message handling");
                    }
                    break;
                }
                default: {
                    this.manager.setMessageHandler(new TextualMessageHandler(this));
                    this.getLogger().info("Message handler has been set to TEXT");
                    break;
                }
            }
        } catch (Throwable throwable) {
            this.getLogger().info(
                            "Failed to find a valid message handler, please make sure that your valuefor 'chatSettings.handler' is a valid message handler type");
            this.getServer().getPluginManager().disablePlugin((Plugin) this);
            return;
        }

        this.getServer().getPluginManager().registerEvents((Listener) new PlayerListener(this), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new AuctionListener(this), (Plugin) this);
        
        this.loadConfig();
        this.loadOfflineRewards();
        
        final AuctionCommandHandler handler = new AuctionCommandHandler(this);
        
        this.getCommand("auction").setExecutor((CommandExecutor) handler);
        this.getCommand("sealedauction").setExecutor((CommandExecutor) handler);
        this.getCommand("bid").setExecutor((CommandExecutor) handler);
        this.getServer().getPluginManager().registerEvents((Listener) handler, (Plugin) this);
    }

    private boolean canRegisterPlaceholders() {
        try {
            return Class.forName("me.clip.placeholderapi.PlaceholderAPI") != null;
        } catch (Throwable throwable) {
            return false;
        }
    }

    public void onDisable() {
        ((AuctionManagerImpl) this.manager).disable();
        try {
            if (!this.offlineFile.exists()) {
                this.offlineFile.getParentFile().mkdirs();
                this.offlineFile.createNewFile();
            }
            this.offlineConfiguration.save(this.offlineFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Auctions.setManager(null);
    }

    public AuctionManager getManager() {
        return this.manager;
    }

    public MessageHandler getMessageHandler() {
        return this.manager.getMessageHandler();
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public boolean isBroadcastTime(final int time) {
        return this.getConfig().isList("general.broadcastTimes")
                        && this.getConfig().getStringList("general.broadcastTimes").contains(Integer.toString(time));
    }

    public boolean isWorldDisabled(final World world) {
        return this.getConfig().isList("general.disabledWorlds")
                        && this.getConfig().getStringList("general.disabledWorlds").contains(world.getName());
    }

    public String getMessage(final String path) {
        if (!this.getConfig().isString(path)) {
            return path;
        }
        return this.getConfig().getString(path);
    }

    public String getItemName(final ItemStack item) {
        final short durability = (short) ((item.getType().getMaxDurability() > 0) ? 0 : item.getDurability());
        final String search = item.getType().toString() + "." + durability;
        final String ret = this.itemsFile.getString(search);
        return (ret == null) ? this.getMaterialName(item.getType()) : ret;
    }

    private String getMaterialName(final Material material) {
        final String[] split = material.toString().toLowerCase().split("_");
        final StringBuilder builder = new StringBuilder();
        for (final String str : split) {
            builder.append(str.substring(0, 1).toUpperCase() + str.substring(1) + " ");
        }
        return builder.toString().trim();
    }

    public void saveOfflinePlayer(final UUID uuid, final Reward reward) {
        this.offlineConfiguration.set(uuid.toString(), (Object) reward);
        this.offlineRewardCache.put(uuid, reward);
        try {
            this.offlineConfiguration.save(this.offlineFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Reward getOfflineReward(final UUID uuid) {
        return this.offlineRewardCache.get(uuid);
    }

    public void removeOfflineReward(final UUID uuid) {
        this.offlineRewardCache.remove(uuid);
        this.offlineConfiguration.set(uuid.toString(), (Object) null);
        try {
            this.offlineConfiguration.save(this.offlineFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "failed to save offline configuration", ex);
        }
    }

    public String formatDouble(final double d) {
        final NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        return format.format(d);
    }

    public void loadConfig() {
        final File names = new File(this.getDataFolder(), "items.yml");
        final File namesFile = new File(this.getDataFolder(), "items.yml");
        if (!names.exists()) {
            this.saveResource("items.yml", false);
        }
        if (!namesFile.exists()) {
            this.saveResource("items.yml", false);
        }
        this.itemsFile = YamlConfiguration.loadConfiguration(namesFile);
    }

    private void loadOfflineRewards() {
        try {
            Class.forName("com.sainttx.auctions.api.reward.ItemReward");
        } catch (Throwable t) {
            this.getLogger().log(Level.SEVERE, "failed to load offline rewards", t);
            return;
        }
        if (!this.offlineFile.exists()) {
            try {
                this.offlineFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.offlineConfiguration = YamlConfiguration.loadConfiguration(this.offlineFile);
        for (final String string : this.offlineConfiguration.getKeys(false)) {
            final Object obj = this.offlineConfiguration.get(string);
            Reward reward;
            if (obj instanceof Reward) {
                reward = (Reward) this.offlineConfiguration.get(string);
            } else {
                if (!(obj instanceof ItemStack)) {
                    this.getLogger().info("Cannot load offline reward for player with UUID \"" + string + "\", unknown reward type \""
                                    + obj.getClass().getName() + "\"");
                    continue;
                }
                reward = new ItemReward(this, (ItemStack) obj);
            }
            this.offlineRewardCache.put(UUID.fromString(string), reward);
        }
    }
