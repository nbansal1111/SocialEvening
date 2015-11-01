package com.project.socialevening.fragments;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.project.socialevening.ListAdapters.CustomPagerAdapter;
import com.project.socialevening.R;
import com.project.socialevening.activity.CreateTeamActivity;
import com.project.socialevening.activity.FragmentContainer;
import com.project.socialevening.utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 29/10/15.
 */
public class HomeFragment extends BaseFragment implements CustomPagerAdapter.PagerAdapterInterface<String> {
    private ViewPager pager;
    private TabLayout tabLayout;
    private List<String> tabNames = new ArrayList<>();
    private CustomPagerAdapter<String> adapter;

    @Override
    public void initViews() {
        initToolBar(getString(R.string.social_evening));
        pager = (ViewPager) findView(R.id.viewpager);
        tabLayout = (TabLayout) findView(R.id.tabs);
        initTabNames();
        adapter = new CustomPagerAdapter<>(getChildFragmentManager(), tabNames, this);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        setOnClickListener(R.id.fab);
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

    private void initTabNames() {
        tabNames.add("Around you");
        tabNames.add("Challenges");
        tabNames.add("Rewards");
    }

    private void setAdapter() {

    }

    @Override
    public int getViewID() {
        return R.layout.fragment_home;
    }

    @Override
    public Fragment getFragmentItem(int position, String listItem) {
        switch (position) {
            case 0:
                return new TeamNearBy();
            case 1:
                return new ChallengeHomeFragment();
            case 2:
                return new RewardsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position, String listItem) {
        return listItem;
    }

    @Override
    protected int getHomeIcon() {
        return R.drawable.drawer_icon;
    }
}
