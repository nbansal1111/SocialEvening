package com.project.socialevening.holders;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.project.socialevening.R;
import com.project.socialevening.models.Auction;

/**
 * Created by nitin on 26/01/16.
 */
public class AuctionHolder extends BaseRecycleHolder {
    private TextView auctionName, auctionDesc, auctionTime, auctionMinPrice, auctionMyBidPrice, auctionTotalBids;

    public AuctionHolder(View itemView) {
        super(itemView);
        auctionName = findTV(R.id.tv_auction_name);
        auctionDesc = findTV(R.id.tv_auction_name);
        auctionTime = findTV(R.id.tv_auction_name);
        auctionMinPrice = findTV(R.id.tv_auction_name);
        auctionMyBidPrice = findTV(R.id.tv_auction_name);
        auctionTotalBids = findTV(R.id.tv_auction_name);
        findTV(R.id.tv_bid_now).setVisibility(View.VISIBLE);
        findView(R.id.ll_your_bid).setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolder(Object object, int pos) {
        super.onBindViewHolder(object, pos);
        Auction auction = (Auction) object;
        auctionName.setText(auction.getAuctionName());
        if (auction.getAuctionTime() > System.currentTimeMillis()) {
            String auctionTimeString = DateUtils.getRelativeTimeSpanString(auction.getAuctionTime(), System.currentTimeMillis(), 0L).toString();
            auctionTime.setText(auctionTimeString);
        } else {
            auctionTime.setText("Expired");
        }
        auctionDesc.setText(auction.getAuctionDetails());

        auctionMinPrice.setText("$ " + auction.getAuctionPrice());
//        auctionMyBidPrice.setText(auction.getMyBidPrice());
        auctionTotalBids.setText(auction.getBidCount() + "");
        findTV(R.id.tv_bid_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
