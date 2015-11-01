package com.project.socialevening.holders;

import android.content.Intent;
import android.view.View;

import com.project.socialevening.activity.TeamActivity;
import com.project.socialevening.utility.AppConstants;

/**
 * Created by nitin on 01/11/15.
 */
public class TeamHolder extends MyTeamHolder {
    public TeamHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onCardClick(View v) {
        Intent i = new Intent(ctx, TeamActivity.class);
        i.putExtra(AppConstants.BUNDLE_KEYS.TEAM_ID, teamObject.getObjectId());
        ctx.startActivity(i);
    }
}
