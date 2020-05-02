package play.dahp.us.auctions.command.subcommand;

import play.dahp.us.auctions.command.*;
import play.dahp.us.auctions.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.messages.*;
import play.dahp.us.auctions.api.*;
import play.dahp.us.auctions.api.reward.*;

public class ImpoundCommand extends AuctionSubCommand
{
    public ImpoundCommand(final AuctionPlugin plugin) {
        super(plugin, "auctions.command.impound", new String[] { "impound" });
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final MessageHandler handler = this.plugin.getManager().getMessageHandler();
        final Auction auction = this.plugin.getManager().getCurrentAuction();
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can impound auctions");
        }
        else if (auction == null) {
            handler.sendMessage(sender, this.plugin.getMessage("messages.error.noCurrentAuction"));
        }
        else {
            final Player player = (Player)sender;
            final Reward reward = auction.getReward();
            auction.impound();
            reward.giveItem(player);
            final String message = this.plugin.getMessage("messages.auctionImpounded").replace("[player]", player.getName());
            handler.broadcast(message, false);
        }
        return true;
    }
}
