package com.project.socialevening.fragments;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.activity.HomeActivity;
import com.project.socialevening.utility.AppConstants;

public class DrawerFragment extends BaseFragment {

    HomeActivity act;
    TextView location;

    @Override
    public void initViews() {
        act = (HomeActivity) getActivity();
        location = (TextView) findView(R.id.tv_mobileNumber);
        location.setText(ParseUser.getCurrentUser().getUsername());
        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                act.closeDrawer();
            }
        });
        setOnClickListener(R.id.ll_home, R.id.ll_teams);

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

            default:
                break;
        }
    }

    final String appPackageName = "com.project.socialevening";

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri
                .parse("https://play.google.com/store/apps/developer?id="
                        + appPackageName));
        startActivity(intent);
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
