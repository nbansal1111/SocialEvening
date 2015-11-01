package com.project.socialevening.fragments;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.activity.HomeActivity;
import com.project.socialevening.activity.LoginScreen;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.Util;

public class DrawerFragment extends BaseFragment {

    HomeActivity act;
    TextView location;

    @Override
    public void initViews() {
        Util.saveAppLink();
        act = (HomeActivity) getActivity();
        location = (TextView) findView(R.id.tv_name);
        setText(R.id.tv_email, ParseUser.getCurrentUser().getEmail() + "");
        location.setText(ParseUser.getCurrentUser().getUsername());
        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                act.closeDrawer();
            }
        });
        setOnClickListener(R.id.ll_home, R.id.ll_teams, R.id.rl_spread, R.id.ll_log_out);

    }

    @Override
    public void onClick(View v) {
        act.closeDrawer();
        switch (v.getId()) {
            case R.id.ll_home:
                act.onDrawerItemClicked(AppConstants.FRAGMENT_TYPE.HOME_FRAGMENT);
                break;
            case R.id.ll_teams:
                act.onDrawerItemClicked(AppConstants.FRAGMENT_TYPE.MY_TEAMS);
                break;

            case R.id.rl_spread:
                shareApp();
                break;
            case R.id.ll_log_out:
                logOut();
                break;

            default:
                break;
        }
    }

    private void logOut() {
        showToast("Logging you out");
        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Preferences.saveData(Preferences.LOGIN_KEY, false);
                    getActivity().startActivity(new Intent(act, LoginScreen.class));
                    getActivity().finish();
                } else {

                }
            }
        });
    }

    final String appPackageName = "com.project.socialevening";

    private void shareApp() {

        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Social Evening app -- " + Preferences.getAppLink());
        startActivity(i);
    }

    private void launchMarketToRateApp() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }
    }

    @Override
    public int getViewID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_drawer;
    }

}
