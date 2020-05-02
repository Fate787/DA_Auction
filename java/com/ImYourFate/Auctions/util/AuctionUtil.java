package play.dahp.us.auctions.util;

import org.bukkit.inventory.*;
import org.bukkit.*;

public class AuctionUtil
{
    public static int getFreeSlots(final Inventory inv, final ItemStack base) {
        if (inv == null) {
            throw new IllegalArgumentException("inventory cannot be null");
        }
        if (base == null) {
            throw new IllegalArgumentException("base item cannot be null");
        }
        int totalFree = 0;
        for (final ItemStack is : inv.getStorageContents()) {
            if (is == null || is.getType() == Material.AIR) {
                totalFree += base.getMaxStackSize();
            }
            else if (is.isSimilar(base)) {
                totalFree += ((is.getAmount() > base.getMaxStackSize()) ? 0 : (base.getMaxStackSize() - is.getAmount()));
            }
        }
        return totalFree;
    }
    
    public static int getAmountItems(final Inventory inv, final ItemStack base) {
        if (inv == null) {
            throw new IllegalArgumentException("inventory cannot be null");
        }
        if (base == null) {
            throw new IllegalArgumentException("base item cannot be null");
        }
        int count = 0;
        for (int i = 0; i < 36; ++i) {
            final ItemStack is = inv.getItem(i);
            if (is != null && is.isSimilar(base)) {
                count += is.getAmount();
            }
        }
        return count;
    }
}
