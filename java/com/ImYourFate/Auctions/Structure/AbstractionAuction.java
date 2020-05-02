package play.dahp.us.auctions.structure;

import play.dahp.us.auctions.*;
import play.dahp.us.auctions.api.*;
import play.dahp.us.auctions.api.module.*;
import play.dahp.us.auctions.api.reward.*;
import org.bukkit.scheduler.*;
import java.util.*;
import org.bukkit.plugin.*;
import play.dahp.us.auctions.api.event.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import play.dahp.us.auctions.api.messages.*;

public abstract class AbstractAuction implements Auction
{
    protected AuctionPlugin plugin;
    protected AuctionType type;
    protected Collection<AuctionModule> modules;
    protected UUID ownerUUID;
    protected String ownerName;
    protected UUID topBidderUUID;
    protected String topBidderName;
    protected double winningBid;
    protected double startPrice;
    protected boolean hasBidBeenPlaced;
    protected Reward reward;
    protected double bidIncrement;
    protected double autowin;
    protected int timeLeft;
    protected BukkitTask timerTask;
    
    public AbstractAuction() {
        this.modules = new HashSet<AuctionModule>();
        this.autowin = -1.0;
    }
    
    @Override
    public UUID getOwner() {
        return this.ownerUUID;
    }
    
    @Override
    public String getOwnerName() {
        return this.ownerName;
    }
    
    @Override
    public boolean hasBids() {
        return this.hasBidBeenPlaced;
    }
    
    @Override
    public boolean hasEnded() {
        return this.timeLeft <= 0 || (this.getAutowin() != -1.0 && this.getAutowin() < this.getTopBid());
    }
    
    @Override
    public UUID getTopBidder() {
        return this.topBidderUUID;
    }
    
    @Override
    public String getTopBidderName() {
        return this.topBidderName;
    }
    
    @Override
    public Reward getReward() {
        return this.reward;
    }
    
    @Override
    public AuctionType getType() {
        return this.type;
    }
    
    @Override
    public double getTopBid() {
        return this.winningBid;
    }
    
    @Override
    public double getStartPrice() {
        return this.startPrice;
    }
    
    @Override
    public double getAutowin() {
        return this.autowin;
    }
    
    @Override
    public int getTimeLeft() {
        return this.timeLeft;
    }
    
    @Override
    public void setTimeLeft(final int time) {
        this.timeLeft = time;
    }
    
    @Override
    public void start() {
        this.timerTask = this.plugin.getServer().getScheduler().runTaskTimer((Plugin)this.plugin, (Runnable)new AuctionTimer(), 20L, 20L);
        this.startMessages();
        final AuctionStartEvent event = new AuctionStartEvent(this);
        Bukkit.getPluginManager().callEvent((Event)event);
    }
    
    @Override
    public double getBidIncrement() {
        return this.bidIncrement;
    }
    
    @Override
    public double getTax() {
        return this.plugin.getConfig().getInt("auctionSettings.taxPercent", 0);
    }
    
    @Override
    public double getTaxAmount() {
        return this.getTopBid() * this.getTax() / 100.0;
    }
    
    @Override
    public Collection<AuctionModule> getModules() {
        return new HashSet<AuctionModule>(this.modules);
    }
    
    @Override
    public void addModule(final AuctionModule module) {
        if (module == null) {
            throw new IllegalArgumentException("module cannot be null");
        }
        this.modules.add(module);
    }
    
    @Override
    public boolean removeModule(final AuctionModule module) {
        return this.modules.remove(module);
    }
    
    @Override
    public abstract void cancel();
    
    @Override
    public abstract void end(final boolean p0);
    
    @Override
    public abstract void impound();
    
    @Override
    public abstract void placeBid(final Player p0, final double p1);
    
    public abstract void runNextAuctionTimer();
    
    protected abstract void startMessages();
    
    public abstract void returnMoneyToAll();
    
    public abstract void broadcastBid();
    
    public class AuctionTimer implements Timer
    {
        @Override
        public void run() {
            final AbstractAuction this$0 = AbstractAuction.this;
            --this$0.timeLeft;
            if (AbstractAuction.this.timeLeft <= 0) {
                AbstractAuction.this.end(true);
            }
            else if (AbstractAuction.this.plugin.isBroadcastTime(AbstractAuction.this.timeLeft)) {
                final MessageHandler handler = AbstractAuction.this.plugin.getManager().getMessageHandler();
                handler.broadcast(AbstractAuction.this.plugin.getMessage("messages.auctionFormattable.timer"), AbstractAuction.this, true);
            }
        }
    }
}
