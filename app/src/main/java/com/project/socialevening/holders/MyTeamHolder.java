package com.project.socialevening.holders;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.project.socialevening.R;
import com.project.socialevening.activity.FragmentContainer;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Util;

/**
 * Created by nitin on 31/10/15.
 */
public class MyTeamHolder extends BaseRecycleHolder {
    TextView teamName, challengeCount, teamMembers;
    ImageView teamImage;
    protected ParseObject teamObject;

    public MyTeamHolder(View itemView) {
        super(itemView);
        teamName = findTV(R.id.tv_team_name);
        challengeCount = findTV(R.id.tv_challenge_count);
        teamMembers = findTV(R.id.tv_members_count);
        teamImage = findImage(R.id.iv_team_image);
    }

    @Override
    public void onBindViewHolder(Object object, int pos) {
        super.onBindViewHolder(object, pos);
        if (object instanceof ParseObject) {
            teamObject = (ParseObject) object;
            ParseFile file = teamObject.getParseFile(AppConstants.PARAMS.TEAM_IMAGE);
            if (null != file) {
                String url = file.getUrl();
                Util.loadImage(ctx, url, teamImage, 0);
            }
            teamName.setText(teamObject.getString(AppConstants.PARAMS.TEAM_NAME) + "");
        }
    }

    @Override
    protected void onCardClick(View v) {
        super.onCardClick(v);
        Bundle b = new Bundle();
        b.putString(AppConstants.BUNDLE_KEYS.TEAM_ID, teamObject.getObjectId());
        FragmentContainer.startActivity(ctx, AppConstants.FRAGMENT_TYPE.ADD_TEAM_MATE, b);
    }
}
