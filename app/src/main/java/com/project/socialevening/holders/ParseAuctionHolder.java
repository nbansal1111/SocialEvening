package com.project.socialevening.holders;

import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.fragments.dialogs.BidDialog;
import com.project.socialevening.models.Auction;
import com.project.socialevening.utility.AppConstants;

import java.util.HashMap;

/**
 * Created by nitin on 26/01/16.
 */
public class ParseAuctionHolder extends BaseRecycleHolder {
    private TextView auctionName, auctionDesc, auctionTimeTv, auctionMinPrice, auctionMyBidPrice, auctionTotalBids;

    public HashMap<String, ParseObject> getBidMap() {
        return bidMap;
    }

    public void setBidMap(HashMap<String, ParseObject> bidMap) {
        this.bidMap = bidMap;
    }

    private HashMap<String, ParseObject> bidMap = new HashMap<>();

    public ParseAuctionHolder(View itemView, HashMap<String, ParseObject> bidMap) {
        super(itemView);
        this.bidMap = bidMap;
        auctionName = findTV(R.id.tv_auction_name);
        auctionDesc = findTV(R.id.tv_auction_desc);
        auctionTimeTv = findTV(R.id.tv_auction_time);
        auctionMinPrice = findTV(R.id.tv_auction_min_price);
        auctionMyBidPrice = findTV(R.id.tv_auction_my_bid);
        auctionTotalBids = findTV(R.id.tv_auction_bids);
        findTV(R.id.tv_bid_now).setVisibility(View.VISIBLE);
        findView(R.id.ll_your_bid).setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolder(Object object, int pos) {
        super.onBindViewHolder(object, pos);
        final ParseObject auction = (ParseObject) object;
        boolean isExpired = false;
        auctionName.setText(auction.getString(AppConstants.PARAMS.AUCTION_NAME));
        long auctionTime = auction.getLong(AppConstants.PARAMS.AUCTION_TIME);
        if (auctionTime > System.currentTimeMillis()) {
            String auctionTimeString = DateUtils.getRelativeTimeSpanString(auctionTime, System.currentTimeMillis(), 0L).toString();
            auctionTimeTv.setText("Closing " + auctionTimeString);
        } else {
            auctionTimeTv.setText("Expired");
            isExpired = true;
        }
        auctionDesc.setText(auction.getString(AppConstants.PARAMS.AUCTION_DESC));
        double price = auction.getDouble(AppConstants.PARAMS.AUCTION_PRICE);
        auctionMinPrice.setText("$ " + price);

        if (!ParseUser.getCurrentUser().getObjectId().equals(auction.getParseObject(AppConstants.PARAMS.USER).getObjectId())) {
            if (!isExpired) {

                // Check if You have already bid for this

                if (bidMap.containsKey(auction.getObjectId())) {
                    String currentUserId = ParseUser.getCurrentUser().getObjectId();
                    findView(R.id.ll_your_bid).setVisibility(View.VISIBLE);
                    findTV(R.id.tv_bid_now).setVisibility(View.GONE);
                    ParseObject bidObject = bidMap.get(auction.getObjectId());
                    auctionMyBidPrice.setText("$ " + bidObject.getDouble(AppConstants.PARAMS.BID_PRICE));
                } else {
                    findTV(R.id.tv_bid_now).setVisibility(View.VISIBLE);
                    findTV(R.id.tv_bid_now).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openBidDialog(auction);
                        }
                    });
                }


            } else {
                findTV(R.id.tv_bid_now).setVisibility(View.VISIBLE);
                findTV(R.id.tv_bid_now).setText("Expired");
            }

        } else {
            findTV(R.id.tv_bid_now).setVisibility(View.GONE);
        }


        int bidCount = auction.getInt(AppConstants.PARAMS.AUCTION_BID_COUNT);
        auctionTotalBids.setText(bidCount + "");


    }

    private void openBidDialog(ParseObject auction) {
        AppCompatActivity act = (AppCompatActivity) ctx;
        BidDialog d = BidDialog.getInstance(auction);
        d.show(act.getSupportFragmentManager(), "Bid");
    }
}
