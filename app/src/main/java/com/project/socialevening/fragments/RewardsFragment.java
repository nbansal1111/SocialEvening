package com.project.socialevening.fragments;

import android.widget.ImageView;
import android.widget.TextView;

import com.project.socialevening.R;

/**
 * Created by nitin on 29/10/15.
 */
public class RewardsFragment extends BaseFragment {
    ImageView noRewards;
    TextView text;

    @Override
    public void initViews() {
        noRewards = (ImageView) findView(R.id.iv_no_teams);
        text = (TextView) findView(R.id.tv_no_team);

        noRewards.setImageResource(R.drawable.rewards);
        text.setText(R.string.no_rewards);

    }

    @Override
    public int getViewID() {
        return R.layout.layout_no_temas;
    }
}
