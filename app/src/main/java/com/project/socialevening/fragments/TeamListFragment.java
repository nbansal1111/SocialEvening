package com.project.socialevening.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.ListAdapters.MyTeamsAdapter;
import com.project.socialevening.R;
import com.project.socialevening.activity.CreateTeamActivity;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 29/10/15.
 */
public class TeamListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private MyTeamsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ParseObject> myTeams = new ArrayList<>();

    @Override
    public void initViews() {
        initRecyclerView();
        initToolBar(getString(R.string.my_teams));
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findView(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyTeamsAdapter(getActivity(), myTeams, R.layout.card_team);
        recyclerView.setAdapter(adapter);
        getTeamsList();
        setOnClickListener(R.id.fab);
    }

    private void getTeamsList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.TEAM);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        executeTask(AppConstants.TASK_CODES.PARSE_QUERY, query);
    }

    @Override
    public void onRetryClicked(View view) {
        super.onRetryClicked(view);
        getTeamsList();
    }

    @Override
    public void onPreExecute(int taskCode) {
        showProgressBar();
    }

    @Override
    public int getViewID() {
        return R.layout.fragment_team_list;
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                List<ParseObject> objects = (List<ParseObject>) response;
                myTeams.clear();
                myTeams.addAll(objects);
                if (myTeams.size() > 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    showEmptyView();
                }
                break;
        }
    }

    private void showEmptyView() {
        findView(R.id.frame_no_teams).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackgroundError(RestException re, Exception e, int taskCode, Object... params) {
        super.onBackgroundError(re, e, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                onServerError();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(getActivity(), CreateTeamActivity.class));
                break;
        }
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.drawer_icon;
    }
}
