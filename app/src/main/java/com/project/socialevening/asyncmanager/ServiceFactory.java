package com.project.socialevening.asyncmanager;

import android.content.Context;

import com.project.socialevening.utility.AppConstants;


public class ServiceFactory {

    public static Service getInstance(Context context, int taskCode) {
        Service service = null;
        switch (taskCode) {
            case AppConstants.TASK_CODES.SAVE_PARSE_FILE:
                service = new SaveParseFile();
                break;
            case AppConstants.TASK_CODES.SAVE_PARSE_OBJECT:
                service = new SaveParseObject();
                break;
            case AppConstants.TASK_CODES.PARSE_QUERY:
            case AppConstants.TASK_CODES.GET_TEAM_MEMBERS:
                service = new ParseQueryService();
                break;

            default:
                service = new HttpRestService();
                break;

        }
        return service;
    }

}
