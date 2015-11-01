package com.project.socialevening.fragments;

import android.widget.TextView;

import com.parse.ParseUser;
import com.project.socialevening.R;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Preferences;

/**
 * Created by nitin on 29/10/15.
 */
public class ChallengeHomeFragment extends BaseFragment {
    private TextView sentChallenges, recChallenges;

    @Override
    public void initViews() {
        sentChallenges = (TextView) findView(R.id.tv_ch_sent_count);
        recChallenges = (TextView) findView(R.id.tv_ch_rec_count);

        int sent = ParseUser.getCurrentUser().getInt(AppConstants.PARAMS.CHALLENGES_SENT);
        sentChallenges.setText(sent + "");
        int rec = Preferences.getData(AppConstants.PREF_KEYS.CHALLENGE_COUNT, 0);
        recChallenges.setText(rec + "");
    }

    @Override
    public void onResume() {
        super.onResume();
        int sent = ParseUser.getCurrentUser().getInt(AppConstants.PARAMS.CHALLENGES_SENT);
        sentChallenges.setText(sent + "");
        int rec = Preferences.getData(AppConstants.PREF_KEYS.CHALLENGE_COUNT, 0);
        recChallenges.setText(rec + "");
    }

    @Override
    public int getViewID() {
        return R.layout.fragment_challenges;
    }
}
