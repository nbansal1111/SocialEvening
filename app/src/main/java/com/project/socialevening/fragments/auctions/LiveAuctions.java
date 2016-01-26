package com.project.socialevening.fragments.auctions;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.ListAdapters.ParseAuctionAdapter;
import com.project.socialevening.R;
import com.project.socialevening.activity.BidActivity;
import com.project.socialevening.fragments.BaseFragment;
import com.project.socialevening.models.BidInfo;
import com.project.socialevening.utility.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nitin on 26/01/16.
 */
public class LiveAuctions extends BaseFragment {
    private String userName;
    private List<ParseObject> auctions = new ArrayList<>();
    private List<ParseObject> myBids = new ArrayList<>();
    private HashMap<String, ParseObject> bidMap = new HashMap<>();
    private RecyclerView recyclerView;
    private ParseAuctionAdapter adapter;

    @Override
    public void initViews() {
        recyclerView = (RecyclerView) findView(R.id.recyclerView);
        initRecyclerView();
        getMyBids();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new ParseAuctionAdapter(getActivity(), auctions, R.layout.row_auction, AppConstants.VIEW_TYPE.CARD_AUCTION);
        recyclerView.setAdapter(adapter);
    }

    public void onRefresh() {
        getMyBids();
    }

    private void getMyBids() {
        hideVisibility(R.id.frame_empty);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.BID);
        query.whereEqualTo(AppConstants.PARAMS.USER, ParseUser.getCurrentUser());
        executeTask(AppConstants.TASK_CODES.MY_BIDS, query);
    }

    private void getAllAuctions() {
        hideVisibility(R.id.frame_empty);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.AUCTIONS);
        executeTask(AppConstants.TASK_CODES.PARSE_QUERY, query);
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                List<ParseObject> auctionsObjects = (List<ParseObject>) response;
                if (null != auctionsObjects && auctionsObjects.size() > 0) {
                    auctions.clear();
                    auctions.addAll(auctionsObjects);
                    adapter.notifyDataSetChanged();
                    adapter.setBidMap(bidMap);
                } else {
                    showVisibility(R.id.frame_empty);
                    setOnClickListener(R.id.btn_find_now);
                }
                break;
            case AppConstants.TASK_CODES.MY_BIDS:
                myBids = (List<ParseObject>) response;
                if (myBids != null && myBids.size() > 0) {
                    for (ParseObject bid : myBids) {
                        bidMap.put(bid.getParseObject(AppConstants.PARAMS.AUCTION).getObjectId(), bid);
                    }
                }
                getAllAuctions();
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
    public void onRetryClicked(View view) {
        super.onRetryClicked(view);
        onRefresh();
    }

    @Override
    public int getViewID() {
        return R.layout.fragment_auctions;
    }


}
