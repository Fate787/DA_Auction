package play.dahp.us.auctions.structure.messages.handler;

import play.dahp.us.auctions.AuctionPlugin;
import play.dahp.us.auctions.api.Auction;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionBarMessageHandler extends TextualMessageHandler {

    public ActionBarMessageHandler(final AuctionPlugin plugin) {
        super(plugin);
    }

    @Override
    public void broadcast(String message, final Auction auction, final boolean spammy) {
        super.broadcast(message, auction, spammy);
        
        message = this.formatter.format(this.plugin.getMessage("messages.auctionFormattable.actionBarMessage"), auction);
        if (!message.isEmpty()) {
            final BaseComponent[] actionMsg = TextComponent.fromLegacyText(message);
            for (final CommandSender recipient : this.getAllRecipients()) {
                if (recipient instanceof Player && !this.isIgnoring(recipient)) {
                    ((Player) recipient).spigot().sendMessage(ChatMessageType.ACTION_BAR, actionMsg);
                }
            }
        }
    }
}
