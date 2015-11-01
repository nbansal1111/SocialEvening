package com.project.socialevening.models;

import com.project.socialevening.R;
import com.project.socialevening.utility.AppConstants;

/**
 * Created by nitin on 01/11/15.
 */
public class Challenge {
    private String challengeName;
    private boolean isSelected;

    public Challenge(String cName, int color) {
        this.challengeName = cName;
        this.challengeColorId = color;
    }

    public int getChallengeColorId() {
        return challengeColorId;
    }

    public void setChallengeColorId(int challengeColorId) {
        this.challengeColorId = challengeColorId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    private int challengeColorId;

    public static Challenge getChallenge(String challengeName) {
        int colorCode;
        switch (challengeName) {
            default:
                colorCode = R.color.color_primary_dark;
                break;
            case AppConstants.CHALLENGES.INSTAGRAM:
                colorCode = R.color.color_darkpurple_bg;
                break;
            case AppConstants.CHALLENGES.WHATSAPP:
                colorCode = R.color.color_green_bg;
                break;
        }
        return new Challenge(challengeName, colorCode);
    }
}
