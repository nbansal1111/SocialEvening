package com.project.socialevening.fragments;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.activity.BidActivity;
import com.project.socialevening.activity.HomeActivity;
import com.project.socialevening.activity.LoginScreen;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.Util;

public class DrawerFragment extends BaseFragment {

    HomeActivity act;

    @Override
    public void initViews() {
        act = (HomeActivity) getActivity();
        setOnClickListener(R.id.ll_home, R.id.ll_my_orders);

    }

    @Override
    public void onClick(View v) {
        act.closeDrawer();
        switch (v.getId()) {
            case R.id.ll_home:
                Intent i = new Intent(getActivity(), BidActivity.class);
                getActivity().startActivityForResult(i, 1);
                break;
            case R.id.ll_my_orders:
                Preferences.deleteAllData();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent login = new Intent(getActivity(), LoginScreen.class);
                            getActivity().startActivity(login);
                            getActivity().finish();
                        } else {
                            showToast("Unable to log out, please try again");
                        }
                    }
                });

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
//                    getActivity().startActivity(new Intent(act, LoginScreen.class));
//                    getActivity().finish();
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
