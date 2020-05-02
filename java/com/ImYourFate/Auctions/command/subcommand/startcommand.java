package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.AuctionPlugin;
import play.dahp.us.auctions.api.Auction;
import play.dahp.us.auctions.api.AuctionType;
import play.dahp.us.auctions.api.Auctions;
import play.dahp.us.auctions.api.event.AuctionCreateEvent;
import play.dahp.us.auctions.api.messages.MessageHandler;
import play.dahp.us.auctions.api.reward.ItemReward;
import play.dahp.us.auctions.api.reward.Reward;
import play.dahp.us.auctions.command.AuctionSubCommand;
import play.dahp.us.auctions.structure.module.AntiSnipeModule;
import play.dahp.us.auctions.structure.module.AutoWinModule;
import play.dahp.us.auctions.util.AuctionUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StartCommand extends AuctionSubCommand
{
    public StartCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.start", new String[] { "start", "s", "star" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final MessageHandler handler = this.plugin.getManager().getMessageHandler();
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can start auctions");
        }
        else if (args.length < 3) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.startSyntax"));
        }
        else if (this.plugin.getManager().isAuctioningDisabled() && !sender.hasPermission("auctions.bypass.general.disabled")) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.auctionsDisabled"));
        }
        else if (!sender.hasPermission("auctions.bypass.general.disabledworld") && this.plugin.isWorldDisabled(((Player)sender).getWorld())) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.cantUsePluginInWorld"));
        }
        else if (!this.plugin.getConfig().getBoolean("auctionSettings.sealedAuctions.enabled", false) && cmd.getName().equalsIgnoreCase("sealedauction")) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.sealedAuctionsDisabled"));
        }
        else {
            final Player player = (Player)sender;
            final double fee = this.plugin.getConfig().getDouble("auctionSettings.startFee", 0.0);
            if (handler.isIgnoring((CommandSender)player)) {
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.currentlyIgnoring"));
            }
            else if (fee > this.plugin.getEconomy().getBalance((OfflinePlayer)player)) {
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.insufficientBalance"));
            }
            else if (player.getGameMode() == GameMode.CREATIVE && !this.plugin.getConfig().getBoolean("auctionSettings.canAuctionInCreative", false) && !player.hasPermission("auctions.bypass.general.creative")) {
                handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.creativeNotAllowed"));
            }
            else {
                Auction.Builder builder;
                if (cmd.getName().equals("sealedauction")) {
                    builder = Auctions.getAuctionBuilder(this.plugin, AuctionType.SEALED);
                }
                else {
                    builder = Auctions.getAuctionBuilder(this.plugin, AuctionType.STANDARD);
                }
                int increment = -1;
                double autowin = -1.0;
                int amount;
                double price;
                try {
                    if (args[1].equalsIgnoreCase("all")) {
                        amount = this.getNumSimilarItem(player, player.getInventory().getItemInMainHand());
                    }
                    else {
                        amount = Integer.parseInt(args[1]);
                    }
                    price = Double.parseDouble(args[2]);
                    if (args.length > 3) {
                        increment = Integer.parseInt(args[3]);
                        if (!this.plugin.getConfig().getBoolean("auctionSettings.incrementCanExceedStartPrice") && increment > price) {
                            handler.sendMessage(sender, this.plugin.getMessage("messages.error.biddingIncrementExceedsStart"));
                            return true;
                        }
                    }
                    if (args.length > 4) {
                        autowin = Double.parseDouble(args[4]);
                        if (autowin < 0.0) {
                            handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.invalidNumberEntered"));
                            return true;
                        }
                        if (!player.hasPermission("auctions.bypass.start.maxautowin") && autowin > this.plugin.getConfig().getDouble("auctionSettings.maximumAutowinAmount", 1000000.0)) {
                            handler.sendMessage(sender, this.plugin.getMessage("messages.error.autowinTooHigh"));
                            return true;
                        }
                    }
                }
                catch (NumberFormatException ex) {
                    handler.sendMessage(sender, this.plugin.getMessage("messages.error.invalidNumberEntered"));
                    return true;
                }
                if (amount <= 0) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.invalidNumberEntered"));
                }
                else if (amount > 2304) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.notEnoughOfItem"));
                }
                else if (Double.isInfinite(price) || Double.isNaN(price) || Double.isInfinite(autowin) || Double.isNaN(autowin)) {
                    handler.sendMessage(sender, this.plugin.getMessage("messages.error.invalidNumberEntered"));
                }
                else if (price < this.plugin.getConfig().getDouble("auctionSettings.minimumStartPrice", 0.0)) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.startPriceTooLow"));
                }
                else if (!player.hasPermission("auctions.bypass.start.maxprice") && price > this.plugin.getConfig().getDouble("auctionSettings.maximumStartPrice", 99999.0)) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.startPriceTooHigh"));
                }
                else if (this.plugin.getManager().getQueue().size() >= this.plugin.getConfig().getInt("auctionSettings.auctionQueueLimit", 3)) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.auctionQueueFull"));
                }
                else if (increment != -1 && (increment < this.plugin.getConfig().getInt("auctionSettings.minimumBidIncrement", 10) || increment > this.plugin.getConfig().getInt("auctionSettings.maximumBidIncrement", 9999))) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.invalidBidIncrement"));
                }
                else if (autowin != -1.0 && !this.plugin.getConfig().getBoolean("auctionSettings.canSpecifyAutowin", true)) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.autowinDisabled"));
                }
                else if (autowin != -1.0 && Double.compare(autowin, price) <= 0) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.autowinBelowStart"));
                }
                else if (this.plugin.getManager().hasActiveAuction(player)) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.alreadyHaveAuction"));
                }
                else if (this.plugin.getManager().hasAuctionInQueue(player)) {
                    handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.alreadyInAuctionQueue"));
                }
                else {
                    final ItemStack hand = player.getInventory().getItemInMainHand();
                    if (hand == null || hand.getType() == Material.AIR) {
                        handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.invalidItemType"));
                    }
                    else if (!player.hasPermission("auctions.bypass.general.bannedmaterial") && this.plugin.getManager().isBannedMaterial(hand.getType())) {
                        handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.invalidItemType"));
                    }
                    else if (!player.hasPermission("auctions.bypass.general.damageditems") && hand.getType().getMaxDurability() > 0 && hand.getDurability() > 0 && !this.plugin.getConfig().getBoolean("auctionSettings.canAuctionDamagedItems", true)) {
                        handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantAuctionDamagedItems"));
                    }
                    else if (AuctionUtil.getAmountItems((Inventory)player.getInventory(), hand) < amount) {
                        handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.notEnoughOfItem"));
                    }
                    else if (!player.hasPermission("auctions.bypass.general.nameditems") && !this.plugin.getConfig().getBoolean("auctionSettings.canAuctionNamedItems", true) && hand.getItemMeta().hasDisplayName()) {
                        handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantAuctionNamedItems"));
                    }
                    else if (!player.hasPermission("auctions.bypass.general.bannedlore") && this.hasBannedLore(hand)) {
                        handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.error.cantAuctionBannedLore"));
                    }
                    else {
                        final ItemStack item = new ItemStack(hand);
                        item.setAmount(amount);
                        final Reward reward = new ItemReward(this.plugin, item);
                        builder.bidIncrement(increment).reward(reward).owner(player).topBid(price).autowin(autowin);
                        final Auction created = builder.build();
                        if (created.getAutowin() > 0.0) {
                            created.addModule(new AutoWinModule(this.plugin, created, autowin));
                        }
                        if (this.plugin.getConfig().getBoolean("auctionSettings.antiSnipe.enable", true)) {
                            created.addModule(new AntiSnipeModule(this.plugin, created));
                        }
                        final AuctionCreateEvent event = new AuctionCreateEvent(created, player);
                        Bukkit.getPluginManager().callEvent((Event)event);
                        if (event.isCancelled()) {
                            return true;
                        }
                        player.getInventory().removeItem(new ItemStack[] { item });
                        this.plugin.getEconomy().withdrawPlayer((OfflinePlayer)player, fee);
                        if (this.plugin.getManager().canStartNewAuction()) {
                            this.plugin.getManager().setCurrentAuction(created);
                            created.start();
                            this.plugin.getManager().setCanStartNewAuction(false);
                        }
                        else {
                            this.plugin.getManager().addAuctionToQueue(created);
                            handler.sendMessage((CommandSender)player, this.plugin.getMessage("messages.auctionPlacedInQueue"));
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private int getNumSimilarItem(final Player player, final ItemStack item) {
        final Inventory inv = (Inventory)player.getInventory();
        int amount = 0;
        if (item == null || item.getType() == Material.AIR) {
            return 1;
        }
        for (final ItemStack itm : inv) {
            if (itm != null && itm.isSimilar(item)) {
                amount += itm.getAmount();
            }
        }
        return amount;
    }
    
    public boolean hasBannedLore(final ItemStack item) {
        final List<String> bannedLore = (List<String>)this.plugin.getConfig().getStringList("general.blockedLore");
        if (bannedLore != null && !bannedLore.isEmpty() && item.getItemMeta().hasLore()) {
            final List<String> lore = (List<String>)item.getItemMeta().getLore();
            for (final String loreItem : lore) {
                for (final String banned : bannedLore) {
                    if (loreItem.contains(ChatColor.translateAlternateColorCodes('&', banned))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
