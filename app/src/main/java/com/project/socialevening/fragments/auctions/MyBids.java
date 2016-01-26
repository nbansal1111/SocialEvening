package com.project.socialevening.fragments.auctions;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.ListAdapters.ParseAuctionAdapter;
import com.project.socialevening.R;
import com.project.socialevening.activity.BidActivity;
import com.project.socialevening.fragments.BaseFragment;
import com.project.socialevening.models.Auction;
import com.project.socialevening.models.BidInfo;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 26/01/16.
 */
public class MyBids extends BaseFragment {
    private String userName;
    private List<ParseObject> myBids = new ArrayList<>();
    private RecyclerView recyclerView;
    private ParseAuctionAdapter adapter;
    public void onRefresh(){
        getAllAuctions();
    }
    @Override
    public void onRetryClicked(View view) {
        super.onRetryClicked(view);
        onRefresh();
    }
    @Override
    public void initViews() {
        recyclerView = (RecyclerView) findView(R.id.recyclerView);
        initRecyclerView();
        getAllAuctions();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new ParseAuctionAdapter(getActivity(), myBids, R.layout.row_auction, AppConstants.VIEW_TYPE.MY_BIDS);
        recyclerView.setAdapter(adapter);
    }

    private void getAllAuctions() {
        hideVisibility(R.id.frame_empty);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.BID);
        query.whereEqualTo(AppConstants.PARAMS.USER, ParseUser.getCurrentUser());
        query.include(AppConstants.PARAMS.AUCTION);
        executeTask(AppConstants.TASK_CODES.PARSE_QUERY, query);
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                List<ParseObject> bidObjects = (List<ParseObject>) response;
                if (null != bidObjects && bidObjects.size() > 0) {
                    myBids.clear();
                    myBids.addAll(bidObjects);
                    adapter.notifyDataSetChanged();
                } else {
                    showVisibility(R.id.frame_empty);
                    setText(R.id.tv_empty, "No Bids Found");
                    hideVisibility(R.id.btn_find_now);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_find_now:
                Intent i = new Intent(getActivity(), BidActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public int getViewID() {
        return R.layout.fragment_auctions;
    }


}
