package com.project.socialevening.ListAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.Parse;
import com.parse.ParseObject;
import com.project.socialevening.holders.AuctionHolder;
import com.project.socialevening.holders.BaseRecycleHolder;
import com.project.socialevening.holders.MyAuctionsHolder;
import com.project.socialevening.holders.MyBidHolder;
import com.project.socialevening.holders.ParseAuctionHolder;
import com.project.socialevening.models.Auction;
import com.project.socialevening.utility.AppConstants;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nitin on 26/01/16.
 */
public class ParseAuctionAdapter extends RecyclerView.Adapter<BaseRecycleHolder> {
    List<ParseObject> auctions;
    Context ctx;
    int resId, viewType;

    public HashMap<String, ParseObject> getBidMap() {
        return bidMap;
    }

    public void setBidMap(HashMap<String, ParseObject> bidMap) {
        this.bidMap = bidMap;
    }

    private HashMap<String, ParseObject> bidMap = new HashMap<>();

    public ParseAuctionAdapter(Context ctx, List<ParseObject> list, int resourceId, int viewType) {
        this.auctions = list;
        this.ctx = ctx;
        this.resId = resourceId;
        this.viewType = viewType;
    }

    @Override
    public BaseRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(resId, null);
        switch (viewType) {
            case AppConstants.VIEW_TYPE.CARD_AUCTION:
                return new ParseAuctionHolder(v, bidMap);
            case AppConstants.VIEW_TYPE.MY_BIDS:
                return new MyBidHolder(v);
            case AppConstants.VIEW_TYPE.MY_AUCTION:
                return new MyAuctionsHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecycleHolder holder, int position) {
        holder.onBindViewHolder(auctions.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {

        return viewType;
    }

    @Override
    public int getItemCount() {
        return auctions.size();
    }
}
