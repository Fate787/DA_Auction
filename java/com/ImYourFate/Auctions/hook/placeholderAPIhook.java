package play.dahp.us.auctions.hook;

import play.dahp.us.auctions.AuctionPlugin;
import play.dahp.us.auctions.api.Auction;
import play.dahp.us.auctions.util.TimeUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook {

    public static void registerPlaceHolders(final AuctionPlugin plugin) {
        PlaceholderAPI.registerPlaceholderHook(plugin.getName(), new PlaceholderHook() {

            @Override
            public String onPlaceholderRequest(final Player player, final String token) {
                final Auction current = plugin.getManager().getCurrentAuction();
                if (current == null) {
                    return "unknown";
                }

                if (token.equalsIgnoreCase("itemamount")) {
                    return Integer.toString(current.getReward().getAmount());
                }

                if (token.equalsIgnoreCase("time")) {
                    return TimeUtil.getFormattedTime(current.getTimeLeft(),
                                    plugin.getConfig().getBoolean("general.shortenedTimeFormat", false));
                }

                if (token.equalsIgnoreCase("autowin")) {
                    return plugin.formatDouble(current.getAutowin());
                }

                if (token.equalsIgnoreCase("ownername")) {
                    return (current.getOwnerName() == null) ? "Console" : current.getOwnerName();
                }

                if (token.equalsIgnoreCase("topbiddername")) {
                    return (current.getTopBidderName() == null) ? "Console" : current.getTopBidderName();
                }

                if (token.equalsIgnoreCase("increment")) {
                    return plugin.formatDouble(current.getBidIncrement());
                }

                if (token.equalsIgnoreCase("topbid")) {
                    return plugin.formatDouble(current.getTopBid());
                }

                if (token.equalsIgnoreCase("taxpercent")) {
                    return plugin.formatDouble(current.getTax());
                }

                if (token.equalsIgnoreCase("taxamount")) {
                    return plugin.formatDouble(current.getTaxAmount());
                }

                if (token.equalsIgnoreCase("winnings")) {
                    return plugin.formatDouble(current.getTopBid() - current.getTaxAmount());
                }

                if (token.equalsIgnoreCase("itemname")) {
                    return current.getReward().getName();
                }

                if (token.equalsIgnoreCase("startprice")) {
                    return plugin.formatDouble(current.getStartPrice());
                }

                return null;
            }
        });
    }
}
