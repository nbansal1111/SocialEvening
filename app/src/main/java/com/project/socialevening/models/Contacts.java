package com.project.socialevening.models;

import java.util.HashSet;
import java.util.List;

/**
 * Created by nitin on 30/10/15.
 */
public class Contacts {

    private String teamID;
    private String phoneNumber;
    private String displayName;
    private HashSet<String> contactIds;




    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public HashSet<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(HashSet<String> contactIds) {
        this.contactIds = contactIds;
    }


}
