package play.dahp.us.auctions;

import play.dahp.us.auctions.api.*;
import org.bukkit.*;
import play.dahp.us.auctions.api.messages.*;
import java.util.concurrent.*;
import org.bukkit.entity.*;
import java.util.*;

public class AuctionManagerImpl implements AuctionManager
{
    private final AuctionPlugin plugin;
    private Auction currentAuction;
    private Set<MessageRecipientGroup> recipientGroups;
    private Queue<Auction> auctionQueue;
    private Set<Material> banned;
    private MessageHandler handler;
    private boolean disabled;
    private boolean canAuction;
    
    AuctionManagerImpl(final AuctionPlugin plugin) {
        this.recipientGroups = new HashSet<MessageRecipientGroup>();
        this.auctionQueue = new ConcurrentLinkedQueue<Auction>();
        this.banned = EnumSet.noneOf(Material.class);
        this.canAuction = true;
        this.plugin = plugin;
        this.loadBannedMaterials();
    }
    
    void disable() {
        if (this.getCurrentAuction() != null) {
            this.getCurrentAuction().cancel();
        }
        for (final Auction auction : this.getQueue()) {
            auction.end(false);
        }
        this.getQueue().clear();
    }
    
    @Override
    public int getQueuePosition(final Player player) {
        int position = 0;
        for (final Auction auction : this.getQueue()) {
            if (player.getUniqueId().equals(auction.getOwner())) {
                return position + 1;
            }
            ++position;
        }
        return -1;
    }
    
    @Override
    public boolean hasAuctionInQueue(final Player player) {
        for (final Auction auction : this.getQueue()) {
            if (player.getUniqueId().equals(auction.getOwner())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasActiveAuction(final Player player) {
        return this.getCurrentAuction() != null && player.getUniqueId().equals(this.getCurrentAuction().getOwner());
    }
    
    @Override
    public boolean isAuctioningDisabled() {
        return this.disabled;
    }
    
    @Override
    public void setAuctioningDisabled(final boolean disabled) {
        this.disabled = disabled;
    }
    
    @Override
    public Auction getCurrentAuction() {
        return this.currentAuction;
    }
    
    @Override
    public void setCurrentAuction(final Auction auction) {
        this.currentAuction = auction;
    }
    
    @Override
    public boolean canStartNewAuction() {
        return this.currentAuction == null && this.canAuction;
    }
    
    @Override
    public void setCanStartNewAuction(final boolean start) {
        this.canAuction = start;
    }
    
    @Override
    public Queue<Auction> getQueue() {
        return this.auctionQueue;
    }
    
    @Override
    public void addAuctionToQueue(final Auction auction) {
        this.getQueue().add(auction);
    }
    
    @Override
    public MessageHandler getMessageHandler() {
        return this.handler;
    }
    
    @Override
    public void setMessageHandler(final MessageHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("new message handler cannot be null");
        }
        this.handler = handler;
    }
    
    @Override
    public void addMessageGroup(final MessageRecipientGroup group) {
        this.recipientGroups.add(group);
    }
    
    @Override
    public Collection<MessageRecipientGroup> getMessageGroups() {
        return this.recipientGroups;
    }
    
    @Override
    public void startNextAuction() {
        final Auction next = this.getQueue().poll();
        if (next != null) {
            next.start();
            this.currentAuction = next;
        }
    }
    
    @Override
    public boolean isBannedMaterial(final Material material) {
        return this.banned.contains(material);
    }
    
    private void loadBannedMaterials() {
        if (!this.plugin.getConfig().isList("general.blockedMaterials")) {
            return;
        }
        for (final String materialString : this.plugin.getConfig().getStringList("general.blockedMaterials")) {
            final Material material = Material.getMaterial(materialString);
            if (material == null) {
                this.plugin.getLogger().info("Material \"" + materialString + "\" is not a valid Material and will not be blocked.");
            }
            else {
                this.banned.add(material);
                this.plugin.getLogger().info("Material \"" + material.toString() + "\" added as a blocked material.");
            }
        }
    }
}
