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
import java.util.List;

/**
 * Created by nitin on 26/01/16.
 */
public class MyAuctions extends BaseFragment {
    private String userName;
    private List<ParseObject> auctions = new ArrayList<>();
    private List<BidInfo> myBids = new ArrayList<>();
    private RecyclerView recyclerView;
    private ParseAuctionAdapter adapter;

    public void onRefresh(){
        getAllAuctions();
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
        adapter = new ParseAuctionAdapter(getActivity(), auctions, R.layout.row_auction, AppConstants.VIEW_TYPE.MY_AUCTION);
        recyclerView.setAdapter(adapter);
    }

    private void getAllAuctions() {
        hideVisibility(R.id.frame_empty);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.AUCTIONS);
        query.whereEqualTo(AppConstants.PARAMS.USER, ParseUser.getCurrentUser());
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
                } else {
                    showVisibility(R.id.frame_empty);
                    setOnClickListener(R.id.btn_find_now);
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
    public void onRetryClicked(View view) {
        super.onRetryClicked(view);
        onRefresh();
    }

    @Override
    public int getViewID() {
        return R.layout.fragment_auctions;
    }


}
