package com.project.socialevening.holders;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseObject;
import com.project.socialevening.R;
import com.project.socialevening.utility.AppConstants;

/**
 * Created by nitin on 26/01/16.
 */
public class MyBidHolder extends BaseRecycleHolder {

    private TextView auctionName, auctionDesc, auctionTimeTv, auctionMinPrice, auctionMyBidPrice, auctionTotalBids;

    public MyBidHolder(View itemView) {
        super(itemView);
        auctionName = findTV(R.id.tv_auction_name);
        auctionDesc = findTV(R.id.tv_auction_desc);
        auctionTimeTv = findTV(R.id.tv_auction_time);
        auctionMinPrice = findTV(R.id.tv_auction_min_price);
        auctionMyBidPrice = findTV(R.id.tv_auction_my_bid);
        auctionTotalBids = findTV(R.id.tv_auction_bids);
        findView(R.id.ll_your_bid).setVisibility(View.VISIBLE);
        findView(R.id.tv_bid_now).setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolder(Object object, int pos) {
        super.onBindViewHolder(object, pos);

        ParseObject bid = (ParseObject) object;
        ParseObject auction = bid.getParseObject(AppConstants.PARAMS.AUCTION);


        auctionName.setText(auction.getString(AppConstants.PARAMS.AUCTION_NAME));
        long auctionTime = auction.getLong(AppConstants.PARAMS.AUCTION_TIME);
        if (auctionTime > System.currentTimeMillis()) {
            String auctionTimeString = DateUtils.getRelativeTimeSpanString(auctionTime, System.currentTimeMillis(), 0L).toString();
            auctionTimeTv.setText("Closing " + auctionTimeString);
        } else {
            auctionTimeTv.setText("Expired");
        }
        auctionDesc.setText(auction.getString(AppConstants.PARAMS.AUCTION_DESC));
        double price = auction.getDouble(AppConstants.PARAMS.AUCTION_PRICE);
        auctionMinPrice.setText("$ " + price);
        auctionMyBidPrice.setText("$ "+bid.getDouble(AppConstants.PARAMS.BID_PRICE)+"");
        int bidCount = auction.getInt(AppConstants.PARAMS.AUCTION_BID_COUNT);
        auctionTotalBids.setText(bidCount + "");
    }
}
