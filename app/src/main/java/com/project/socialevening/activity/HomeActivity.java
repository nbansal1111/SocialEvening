package com.project.socialevening.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.fragments.BaseFragment;
import com.project.socialevening.fragments.DrawerFragment;
import com.project.socialevening.fragments.HomeFragment;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.FragmentFactory;

public class HomeActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private int currentFragmentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, new DrawerFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, new HomeFragment()).commit();
    }




    private BaseFragment getFragment(int fragmentType) {
        switch (fragmentType) {
            case AppConstants.FRAGMENT_TYPE.HOME_FRAGMENT:
                return new HomeFragment();

        }
        return null;
    }

    public void onDrawerItemClicked(int fragmentType) {
        if (currentFragmentType == fragmentType) return;
        currentFragmentType = fragmentType;
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, FragmentFactory.getFragment(currentFragmentType)).commit();
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }


    @Override
    protected void onHomePressed() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if (currentFragmentType != AppConstants.FRAGMENT_TYPE.HOME_FRAGMENT) {
            currentFragmentType = AppConstants.FRAGMENT_TYPE.HOME_FRAGMENT;
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_home, getFragment(currentFragmentType)).commit();
        } else {
            super.onBackPressed();
        }
    }
}
