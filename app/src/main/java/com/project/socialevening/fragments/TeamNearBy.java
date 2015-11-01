package com.project.socialevening.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.socialevening.ListAdapters.MyTeamsAdapter;
import com.project.socialevening.ListAdapters.TeamsAdapter;
import com.project.socialevening.R;
import com.project.socialevening.exceptionhandler.RestException;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.GPSTracker;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.widgets.WaveDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 31/10/15.
 */
public class TeamNearBy extends BaseFragment {
    private RecyclerView recyclerView;
    private TeamsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ParseObject> myTeams = new ArrayList<>();
    private WaveDrawable wave;
    private ImageView imageView;
    private GPSTracker gps;

    @Override
    public void initViews() {
        gps = new GPSTracker(getActivity());
        imageView = (ImageView) findView(R.id.image);
        setWaveDrawable();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setWaveDrawable() {
        wave = new WaveDrawable(getResources().getColor(R.color.color_primary_dark), 500);
        LinearInterpolator interpolator = new LinearInterpolator();
        wave.setWaveInterpolator(interpolator);
        imageView.setBackground(wave);
        wave.startAnimation();
        getAllTeams(null);
    }


    private void getAllTeams(ParseGeoPoint geoPoint) {
        hideVisibility(R.id.frame_teams, R.id.frame_no_nearby_teams);
        showVisibility(R.id.frame_scanning);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARAMS.TEAM);
//        query.whereNotEqualTo("user", ParseUser.getCurrentUser());
        if (null != geoPoint)
            query.whereNear(AppConstants.PARAMS.LOCATION, geoPoint);
        executeTask(AppConstants.TASK_CODES.PARSE_QUERY, query);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (gps.canGetLocation()) {
            double lat = gps.getLatitude();
            double lng = gps.getLongitude();
            ParseGeoPoint geoPoint = new ParseGeoPoint(lat, lng);
            ParseUser user = ParseUser.getCurrentUser();
            user.put(AppConstants.PARAMS.LOCATION, geoPoint);
            user.saveInBackground();
            getAllTeams(geoPoint);
        } else {
            ParseGeoPoint point = Preferences.getGeoPoint();
            if (point != null) {
                getAllTeams(point);
            } else {
                gps.showSettingsAlert();
            }
        }

    }

    @Override
    public int getViewID() {
        return R.layout.fragment_nearby_teams;
    }

    @Override
    public void onPostExecute(Object response, int taskCode, Object... params) {
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                if (response != null) {
                    final List<ParseObject> list = (List<ParseObject>) response;
                    /// Just to show the animations
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() > 0) {
                                hideVisibility(R.id.frame_scanning, R.id.frame_no_nearby_teams);
                                showVisibility(R.id.frame_teams);
                                setRecyclerView(list);
                            } else {
                                hideVisibility(R.id.frame_scanning, R.id.frame_teams);
                                showVisibility(R.id.frame_no_nearby_teams);
                            }
                        }
                    }, 2000);

                }
                break;
        }
    }

    private void setRecyclerView(List<ParseObject> teams) {
        recyclerView = (RecyclerView) findView(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TeamsAdapter(getActivity(), teams, R.layout.card_team);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPreExecute(int taskCode) {
    }

    @Override
    public void onRetryClicked(View view) {
        super.onRetryClicked(view);
        getAllTeams(Preferences.getGeoPoint());
    }

    @Override
    public void onBackgroundError(RestException re, Exception e, int taskCode, Object... params) {
        switch (taskCode) {
            case AppConstants.TASK_CODES.PARSE_QUERY:
                onServerError();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }
}
