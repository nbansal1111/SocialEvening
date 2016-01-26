package com.project.socialevening.ListAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.socialevening.holders.AuctionHolder;
import com.project.socialevening.holders.BaseRecycleHolder;
import com.project.socialevening.holders.MyBidHolder;
import com.project.socialevening.models.Auction;
import com.project.socialevening.utility.AppConstants;

import java.util.List;

/**
 * Created by nitin on 26/01/16.
 */
public class AuctionAdapter extends RecyclerView.Adapter<BaseRecycleHolder> {
    List<Auction> auctions;
    Context ctx;
    int resId, viewType;

    public AuctionAdapter(Context ctx, List<Auction> list, int resourceId, int viewType) {
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
                return new AuctionHolder(v);
            case AppConstants.VIEW_TYPE.MY_BIDS:
                return new MyBidHolder(v);
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
