package com.project.socialevening.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.project.socialevening.ListAdapters.CustomPagerAdapter;
import com.project.socialevening.R;
import com.project.socialevening.activity.BidActivity;
import com.project.socialevening.fragments.auctions.LiveAuctions;
import com.project.socialevening.fragments.auctions.MyAuctions;
import com.project.socialevening.fragments.auctions.MyBids;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 29/10/15.
 */
public class HomeFragment extends BaseFragment implements CustomPagerAdapter.PagerAdapterInterface<String>, TabLayout.OnTabSelectedListener {
    private ViewPager pager;
    private TabLayout tabLayout;
    private List<String> tabNames = new ArrayList<>();
    private CustomPagerAdapter<String> adapter;
    private LiveAuctions liveAuctions;
    private MyBids myBids;
    private MyAuctions myAuctions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
    }

    @Override
    public void initViews() {
        initToolBar(getString(R.string.app_name));
        pager = (ViewPager) findView(R.id.viewpager);
        tabLayout = (TabLayout) findView(R.id.tabs);
        initTabNames();
        adapter = new CustomPagerAdapter<>(getChildFragmentManager(), tabNames, this);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(this);
        setOnClickListener(R.id.fab);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fab:
                Intent i = new Intent(getActivity(), BidActivity.class);
                startActivity(i);
                break;
        }
    }

    private void initTabNames() {
        tabNames.add("Auctions");
        tabNames.add("My Bids");
        tabNames.add("My Auctions");
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
                if (liveAuctions == null) {
                    liveAuctions = new LiveAuctions();
                }
                return liveAuctions;
            case 1:
                if (myBids == null) {
                    myBids = new MyBids();
                }
                return myBids;
            case 2:
                if (myAuctions == null) {
                    myAuctions = new MyAuctions();
                }
                return myAuctions;
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onReceive(Intent intent) {
        super.onReceive(intent);
        liveAuctions.onRefresh();
        myAuctions.onRefresh();
        myBids.onRefresh();
    }
}
