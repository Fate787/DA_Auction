package play.dahp.us.auctions.structure.messages.handler;

import play.dahp.us.auctions.AuctionPlugin;
import play.dahp.us.auctions.api.Auction;
import play.dahp.us.auctions.api.AuctionType;
import play.dahp.us.auctions.api.messages.MessageHandler;
import play.dahp.us.auctions.api.messages.MessageHandlerAddon;
import play.dahp.us.auctions.api.messages.MessageRecipientGroup;
import play.dahp.us.auctions.api.reward.ItemReward;
import play.dahp.us.auctions.api.reward.Reward;
import play.dahp.us.auctions.util.TimeUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextualMessageHandler implements MessageHandler, MessageHandlerAddon.SpammyMessagePreventer {

    private final Constructor<?> NBT_TAG_COMPOUND_CONSTRUCTOR;

    private final Method AS_NMS_COPY;
    private final Method SAVE;
    
    public static final Pattern COLOR_FINDER_PATTERN;
    
    MessageFormatter formatter;
    Set<UUID> ignoring;
    protected final AuctionPlugin plugin;
    private Set<UUID> ignoringBids;

    public TextualMessageHandler(final AuctionPlugin plugin) {
        this.ignoring = new HashSet<UUID>();
        this.ignoringBids = new HashSet<UUID>();
        this.plugin = plugin;
        this.formatter = new MessageFormatterImpl();
        
        final Class<?> craftItemStack = Reflections.getCraftBukkitClass("inventory.CraftItemStack");
        AS_NMS_COPY = Reflections.getMethod(craftItemStack, "asNMSCopy", ItemStack.class);

        final Class<?> nmsItemStack = Reflections.getNMSClass("ItemStack");
        final Class<?> nbtTagCompound = Reflections.getNMSClass("NBTTagCompound");

        NBT_TAG_COMPOUND_CONSTRUCTOR = Reflections.getConstructor(nbtTagCompound);
        SAVE = Reflections.getMethod(nmsItemStack, "save", nbtTagCompound);
    }

    @Override
    public void broadcast(final String message, final boolean spammy) {
        this.broadcast(message, null, spammy);
    }

    @Override
    public void broadcast(String message, final Auction auction, final boolean spammy) {
        final String[] split = this.formatter.format(message, auction).split("\n+");
        for (final String msg : split) {
            if (!msg.isEmpty()) {
                final TextComponent coloredMessage = this.createMessage(auction, msg);
                for (final CommandSender recipient : this.getAllRecipients()) {
                    if (recipient != null) {
                        if (this.isIgnoring(recipient) || !(recipient instanceof Player)) {
                            continue;
                        }

                        if (spammy && this.isIgnoringSpam(((Player) recipient).getUniqueId())) {
                            continue;
                        }
                        
                        ((Player) recipient).spigot().sendMessage(coloredMessage);
                    }
                }
            }
        }
    }

    protected Collection<CommandSender> getAllRecipients() {
        final Collection<CommandSender> recipients = new HashSet<CommandSender>();
        for (final MessageRecipientGroup group : this.plugin.getManager().getMessageGroups()) {
            for (final CommandSender recipient : group.getRecipients()) {
                recipients.add(recipient);
            }
        }
        
        recipients.add((CommandSender) Bukkit.getConsoleSender());
        return recipients;
    }

    @Override
    public void sendMessage(final CommandSender recipient, final String message) {
        this.sendMessage(recipient, message, null);
    }

    @Override
    public void sendMessage(final CommandSender recipient, String message, final Auction auction) {
        final String[] split = this.formatter.format(message, auction).split("\n+");
        for (final String msg : split) {
            if (!msg.isEmpty() && recipient != null && (recipient instanceof Player)) {
                ((Player) recipient).spigot().sendMessage(this.createMessage(auction, msg));
            }
        }
    }

    @Override
    public void sendAuctionInformation(final CommandSender recipient, final Auction auction) {
        this.sendMessage(recipient, this.plugin.getMessage("messages.auctionFormattable.info"), auction);
        this.sendMessage(recipient, this.plugin.getMessage("messages.auctionFormattable.increment"), auction);
        if (auction.getTopBidder() != null) {
            this.sendMessage(recipient, this.plugin.getMessage("messages.auctionFormattable.infoTopBidder"), auction);
        }
        if (recipient instanceof Player) {
            final Player player = (Player) recipient;
            final int queuePosition = this.plugin.getManager().getQueuePosition(player);
            if (queuePosition > 0) {
                final String message = this.plugin.getMessage("messages.auctionFormattable.queuePosition").replace("[queuepos]",
                                Integer.toString(queuePosition));
                this.sendMessage((CommandSender) player, message, auction);
            }
        }
    }

    @Override
    public boolean isIgnoring(final CommandSender sender) {
        return (sender instanceof Player && this.ignoring.contains(((Player) sender).getUniqueId()))
                        || !this.getAllRecipients().contains(sender);
    }

    @Override
    public void addIgnoring(final CommandSender sender) {
        if (sender instanceof Player) {
            this.ignoring.add(((Player) sender).getUniqueId());
        }
    }

    @Override
    public boolean removeIgnoring(final CommandSender sender) {
        return sender instanceof Player && this.ignoring.remove(((Player) sender).getUniqueId());
    }

    @Override
    public void addIgnoringSpam(final UUID uuid) {
        this.ignoringBids.add(uuid);
    }

    @Override
    public void removeIgnoringSpam(final UUID uuid) {
        this.ignoringBids.remove(uuid);
    }

    @Override
    public boolean isIgnoringSpam(final UUID uuid) {
        return this.ignoringBids.contains(uuid);
    }

    private TextComponent createMessage(final Auction auction, final String message) {
        final TextComponent coloredMessage = new TextComponent(ChatColor.WHITE.toString());
        
        if (!message.isEmpty()) {
            final String[] split = message.split(" ");
            ChatColor current = ChatColor.WHITE;
            
            for (String str : split) {
                str = ChatColor.translateAlternateColorCodes('&', str);
                
                final String currentColor = ChatColor.getLastColors(str);
                
                current = ChatColor.getByChar(currentColor.isEmpty() ? current.getChar() : currentColor.charAt(1));
                
                if (current == ChatColor.RESET) {
                    current = ChatColor.WHITE;
                }
                
                if (str.contains("[item]") && auction != null) {
                    final String rewardName = this.getRewardName(auction.getReward());
                    
                    String display = this.plugin.getMessage("messages.auctionFormattable.itemFormat");
                    display = ChatColor.translateAlternateColorCodes('&', display.replace("[itemName]", rewardName)
                                    .replace("[itemDisplayName]", this.getItemDisplayName(auction.getReward())));
                    
                    if (this.plugin.getConfig().getBoolean("general.stripItemDisplayNameColor", false)) {
                        display = ChatColor.stripColor(display);
                    }
                    
                    final Set<ChatColor> colors = EnumSet.noneOf(ChatColor.class);
                    final Matcher matcher = TextualMessageHandler.COLOR_FINDER_PATTERN.matcher(display);
                    
                    while (matcher.find()) {
                        final char cc = matcher.group(1).charAt(0);
                        colors.add(ChatColor.getByChar(cc));
                    }
                    
                    final ComponentBuilder builder = new ComponentBuilder("");
                    builder.append(TextComponent.fromLegacyText(display));
                    
                    if (auction.getReward() instanceof ItemReward) {
                        final ItemReward item = (ItemReward) auction.getReward();
                        final ItemStack tooltip = new ItemStack(item.getItem());
                        if (tooltip.getItemMeta() instanceof BookMeta) {
                            final BookMeta meta = (BookMeta) tooltip.getItemMeta();
                            meta.setPages(new String[0]);
                            tooltip.setItemMeta((ItemMeta) meta);
                        }
                        
                        try {
                            String jsonItem = SAVE.invoke(AS_NMS_COPY.invoke(null, tooltip), NBT_TAG_COMPOUND_CONSTRUCTOR.newInstance()).toString();
                            builder.event(new HoverEvent(Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(jsonItem)}));
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                            e.printStackTrace();
                        }
                        
                    }
                    
                    for (ChatColor color : colors) {
                        if (color == ChatColor.RESET) {
                            color = ChatColor.WHITE;
                        }
                        
                        if (color.isColor()) {
                            builder.color(net.md_5.bungee.api.ChatColor.valueOf(color.name()));
                        } else {
                            switch (color) {
                                case BOLD:
                                    builder.bold(true);
                                    break;
                                case ITALIC:
                                    builder.italic(true);
                                    break;
                                case MAGIC:
                                    builder.obfuscated(true);
                                    break;
                                case STRIKETHROUGH:
                                    builder.strikethrough(true);
                                    break;
                                case UNDERLINE:
                                    builder.underlined(true);
                                    break;
                                default:
                                    break;
                                
                            }
                        }
                    }
                    
                    for (final BaseComponent base : builder.create()) {
                        coloredMessage.addExtra(base);
                    }
                } else {
                    final ComponentBuilder builder = new ComponentBuilder("");
                    builder.append(TextComponent.fromLegacyText(str));
                    
                    if (current.isColor()) {
                        builder.color(net.md_5.bungee.api.ChatColor.valueOf(current.name()));
                    } else {
                        switch (current) {
                            case BOLD:
                                builder.bold(true);
                                break;
                            case ITALIC:
                                builder.italic(true);
                                break;
                            case MAGIC:
                                builder.obfuscated(true);
                                break;
                            case STRIKETHROUGH:
                                builder.strikethrough(true);
                                break;
                            case UNDERLINE:
                                builder.underlined(true);
                                break;
                            default:
                                break;
                            
                        }
                    }
                    
                    for (final BaseComponent base : builder.create()) {
                        coloredMessage.addExtra(base);
                    }
                }
                
                coloredMessage.addExtra(" ");
            }
        }
        
        return coloredMessage;
    }

    private String getItemDisplayName(final Reward reward) {
        if (reward instanceof ItemReward) {
            final ItemReward ir = (ItemReward) reward;
            return this.getItemRewardName(ir);
        }
        return this.getRewardName(reward);
    }

    private String getRewardName(final Reward reward) {
        return reward.getName();
    }

    public String getItemRewardName(final ItemReward reward) {
        final ItemStack item = reward.getItem();
        final ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return reward.getName();
    }

    static {
        COLOR_FINDER_PATTERN = Pattern.compile("§([a-f0-9klmnor])");
    }

    public class MessageFormatterImpl implements MessageFormatter {

        @Override
        public String format(final String message) {
            return this.format(message, null);
        }

        @Override
        public String format(String message, final Auction auction) {
            if (message == null) {
                throw new IllegalArgumentException("message cannot be null");
            }
            final boolean truncate = TextualMessageHandler.this.plugin.getConfig().getBoolean("general.truncatedNumberFormat", false);
            if (auction != null) {
                if (auction.getType() == AuctionType.SEALED && !auction.hasEnded() && TextualMessageHandler.this.plugin.getConfig()
                                .getBoolean("auctionSettings.sealedAuctions.concealTopBidder", true)) {
                    message = message.replace("[topbiddername]", "Hidden").replace("[topbid]", "hidden");
                }
                message = message.replace("[itemName]", TextualMessageHandler.this.getRewardName(auction.getReward()));
                message = message.replace("[itemDisplayName]", TextualMessageHandler.this.getItemDisplayName(auction.getReward()));
                message = message.replace("[itemamount]", Integer.toString(auction.getReward().getAmount()));
                message = message.replace("[time]", TimeUtil.getFormattedTime(auction.getTimeLeft(),
                                TextualMessageHandler.this.plugin.getConfig().getBoolean("general.shortenedTimeFormat", false)));
                message = message.replace("[autowin]",
                                truncate ? this.truncateNumber(auction.getAutowin()) : this.formatDouble(auction.getAutowin()));
                message = message.replace("[ownername]", (auction.getOwnerName() == null) ? "Console" : auction.getOwnerName());
                message = message.replace("[topbiddername]",
                                auction.hasBids() ? ((auction.getTopBidderName() == null) ? "Console" : auction.getTopBidderName())
                                                : "Nobody");
                message = message.replace("[increment]",
                                truncate ? this.truncateNumber(auction.getBidIncrement()) : this.formatDouble(auction.getBidIncrement()));
                message = message.replace("[topbid]",
                                truncate ? this.truncateNumber(auction.getTopBid()) : this.formatDouble(auction.getTopBid()));
                message = message.replace("[taxpercent]", this.formatDouble(auction.getTax()));
                message = message.replace("[taxamount]", this.formatDouble(auction.getTaxAmount()));
                message = message.replace("[startprice]",
                                truncate ? this.truncateNumber(auction.getStartPrice()) : this.formatDouble(auction.getStartPrice()));
                final double winnings = auction.getTopBid() - auction.getTaxAmount();
                message = message.replace("[winnings]", truncate ? this.truncateNumber(winnings) : this.formatDouble(winnings));
            }
            return ChatColor.translateAlternateColorCodes('&', message);
        }

        private String formatDouble(final double d) {
            final NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            format.setMaximumFractionDigits(2);
            format.setMinimumFractionDigits(0);
            return format.format(d);
        }

        private String truncateNumber(final double x) {
            return (x < 1000.0) ? this.formatDouble(x)
                            : ((x < 1000000.0) ? (this.formatDouble(x / 1000.0) + "K")
                                            : ((x < 1.0E9) ? (this.formatDouble(x / 1000000.0) + "M")
                                                            : ((x < 1.0E12) ? (this.formatDouble(x / 1.0E9) + "B")
                                                                            : (this.formatDouble(x / 1.0E12) + "T"))));
        }
    }
}
