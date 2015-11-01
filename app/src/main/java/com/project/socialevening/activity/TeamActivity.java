package com.project.socialevening.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.ListAdapters.TeamMembersAdapter;
import com.project.socialevening.R;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Logger;
import com.project.socialevening.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nitin on 01/11/15.
 */
public class TeamActivity extends BaseActivity {

    private static final int REQ_CODE_CHALLENGE = 0;
    private ListView listView;
    //    private CustomListAdapter<ParseUser> adapter;
    private List<ParseUser> members = new ArrayList<>();
    private ParseObject teamObject;
    private List<String> membersIds = new ArrayList<>();
    private HashSet<String> usersIds = new HashSet<>();

    private RecyclerView recyclerView;
    private TeamMembersAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TeamMembersAdapter(this, members, R.layout.row_team_mate);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        getTeamObject();
        setRecyclerView();
        setOnClickListener(R.id.fab);
        printAllData();

    }

    private void printAllData() {
        Bundle b = getIntent().getExtras();
        if (null != b)
            for (String s : b.keySet()) {
                Logger.info(TAG, "Bundle key:" + s + ", value" + b.get(s));
            }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab:
                selectChallenge();
                break;
            case R.id.btn_joinTeam:
                joinTeam();
                break;
        }
    }

    private void joinTeam() {
        if (null != teamObject) {
            teamObject.addAllUnique(AppConstants.PARAMS.MEMBERS, Arrays.asList(ParseUser.getCurrentUser()));
            executeTask(AppConstants.TASK_CODES.SAVE_PARSE_OBJECT, teamObject);
        }
    }

    private void selectChallenge() {
        Intent i = new Intent(this, SelectChallenge.class);
        startActivityForResult(i, REQ_CODE_CHALLENGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQ_CODE_CHALLENGE:
                if (data != null) {
                    String challengeName = data.getStringExtra(AppConstants.PARAMS.CHALLENGE_NAME);
                    sendChallenge(challengeName);
                }
                break;
        }
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        super.onPostExecute(response, taskCode, params);
        switch (taskCode) {
            case AppConstants.TASK_CODES.SAVE_PARSE_OBJECT:
                showToast(getString(R.string.msg_team_joined));
                if (teamObject != null) {
                    teamObject.increment(AppConstants.PARAMS.MEMBER_COUNT, 1);
                }
                getMembers();
                break;
            case AppConstants.TASK_CODES.PARSE_QUERY:
                List<ParseObject> teams = (List<ParseObject>) response;
                if (null != teams && teams.size() > 0) {
                    teamObject = teams.get(0);
                    setHeader(teamObject);
                }
            case AppConstants.TASK_CODES.GET_TEAM_MEMBERS:

                break;
        }

    }

    private void getMembers() {
        List<ParseUser> teamMembers = teamObject.getList(AppConstants.PARAMS.MEMBERS);

//        membersIds.clear();
        members.clear();
        if (null != teamMembers && teamMembers.size() > 0) {
            for (ParseUser u : teamMembers) {
                usersIds.add(u.getObjectId());
                Logger.info(TAG, "ID:" + u.getObjectId());
                u.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            ParseUser user = (ParseUser) object;
                            members.add(user);
                            adapter.notifyDataSetChanged();
                        } else {
                            Logger.info(TAG, "Error while fetching object:" + e.getMessage());
                        }
                    }
                });
            }
        }
        showJoinTeam();


    }

    private void showJoinTeam() {
        if (!usersIds.contains(ParseUser.getCurrentUser().getObjectId())) {
            showVisibility(R.id.btn_joinTeam);
            setOnClickListener(R.id.btn_joinTeam);
        } else {
            hideVisibility(R.id.btn_joinTeam);
        }
    }


    private void setHeader(ParseObject teamObject) {

        getMembers();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(teamObject.getString(AppConstants.PARAMS.TEAM_NAME));

        // set team image
        ImageView teamImage = (ImageView) findViewById(R.id.iv_team_pic);
        ParseFile file = teamObject.getParseFile(AppConstants.PARAMS.TEAM_IMAGE);
        if (null != file) {
            String url = file.getUrl();
            Util.loadImage(this, url, teamImage, 0);
        }
    }

    private void getTeamObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.TEAM);
        query.whereEqualTo(AppConstants.PARAMS.OBJECT_ID, getIntent().getStringExtra(AppConstants.BUNDLE_KEYS.TEAM_ID));
        executeTask(AppConstants.TASK_CODES.PARSE_QUERY, query);
    }

    private void sendChallenge(String challengeName) {
        if (teamObject != null) {
            teamObject.increment(AppConstants.PARAMS.CHALLENGE_COUNT);
            teamObject.saveInBackground();
            String teamId = teamObject.getObjectId();
            JSONObject obj = new JSONObject();

            ParseUser.getCurrentUser().increment(AppConstants.PARAMS.CHALLENGES_SENT);

            try {
                obj.put(AppConstants.PARAMS.OBJECT_ID, teamId);
                obj.put(AppConstants.PARAMS.CHALLENGE_NAME, challengeName);
                obj.put(AppConstants.PARAMS.ALERT, "New challenge received from " + ParseUser.getCurrentUser().getUsername());
                sendPush(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void sendPush(JSONObject json) {
        Log.d(TAG, "JSON" + json);

        for (ParseUser user : members) {
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("user", user);

// Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setData(json);
            push.sendInBackground();
        }
        showToast(getString(R.string.msg_pushNotification_sent));
    }
}
