package play.dahp.us.auctions.api.reward;

import play.dahp.us.auctions.*;
import java.util.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.util.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;

public class ItemReward implements Reward {

    private AuctionPlugin plugin;
    private ItemStack item;

    public ItemReward(final Map<String, Object> itemSerialized) {
        this.item = ItemStack.deserialize(itemSerialized);
    }

    public ItemReward(final AuctionPlugin plugin, final ItemStack item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        this.plugin = plugin;
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public void giveItem(final Player player) {
        final Inventory inventory = (Inventory) player.getInventory();
        final ItemStack reward = new ItemStack(this.item);
        final int free = AuctionUtil.getFreeSlots(inventory, reward);
        if (free < reward.getAmount()) {
            final ItemStack drop = new ItemStack(reward);
            drop.setAmount(drop.getAmount() - free);
            if (free > 0) {
                reward.setAmount(free);
                this.giveItemToPlayer(player, reward);
            }
            player.getWorld().dropItem(player.getLocation(), drop);
            if (this.plugin != null) {
                this.plugin.getMessageHandler().sendMessage((CommandSender) player, this.plugin.getMessage("messages.notEnoughRoom"));
            }
        } else {
            this.giveItemToPlayer(player, reward);
        }
    }

    private void giveItemToPlayer(final Player player, final ItemStack item) {
        if (item.getAmount() > item.getMaxStackSize()) {
            while (item.getAmount() > item.getMaxStackSize()) {
                final ItemStack give = new ItemStack(item);
                give.setAmount(item.getMaxStackSize());
                player.getInventory().addItem(new ItemStack[]{give});
                item.setAmount(item.getAmount() - item.getMaxStackSize());
            }
            player.getInventory().addItem(new ItemStack[]{item});
        } else {
            player.getInventory().addItem(new ItemStack[]{item});
        }
    }

    @Override
    public String getName() {
        return this.plugin.getItemName(this.item);
    }

    @Override
    public int getAmount() {
        return this.item.getAmount();
    }

    public Map<String, Object> serialize() {
        return (Map<String, Object>) this.item.serialize();
    }
}
