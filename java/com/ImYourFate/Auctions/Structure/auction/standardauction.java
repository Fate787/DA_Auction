package play.dahp.us.auctions.structure.auction;

import play.dahp.us.auctions.structure.*;
import play.dahp.us.auctions.*;
import java.util.*;
import play.dahp.us.auctions.api.reward.*;
import play.dahp.us.auctions.api.*;

public class StandardAuction extends DefaultAuction {

    private StandardAuction(final AuctionPlugin plugin, final UUID ownerUUID, final String ownerName, final double topBid,
                    final Reward reward, final double autowin, final double bidIncrement, final int timeLeft) {
        super(plugin, AuctionType.STANDARD);

        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.winningBid = topBid;
        this.startPrice = topBid;
        this.reward = reward;
        this.autowin = autowin;
        this.bidIncrement = bidIncrement;
        this.timeLeft = timeLeft;
    }

    public static class StandardAuctionBuilder extends DefaultAuctionBuilder {

        public StandardAuctionBuilder(final AuctionPlugin plugin) {
            super(plugin);
        }

        @Override
        public Auction build() {
            super.defaults();
            return new StandardAuction(this.plugin, this.ownerId, this.ownerName, this.bid, this.reward, this.autowin, this.increment, this.time);
        }
    }
}
