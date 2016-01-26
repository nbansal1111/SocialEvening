package com.project.socialevening.utility;


/*
 * Copyright (C) 2015, francesco Azzola 
 *
 *(http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 03/09/15
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.project.socialevening.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONCustomReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.d("Push", "Push received");

        if (intent == null)
            return;

        String jsonData = intent.getExtras().getString("com.parse.Data");

        Log.d("Push", "JSON Data [" + jsonData + "]");

        String data = getData(jsonData);
        int count = Preferences.getData(AppConstants.PREF_KEYS.CHALLENGE_COUNT, 0);
        Preferences.saveData(AppConstants.PREF_KEYS.CHALLENGE_COUNT, count + 1);

        // Add custom intent
//        Intent cIntent = new Intent(context, TeamActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create custom notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.discvr_logo_icon)
//                .setContentText(data)
//                .setContentTitle("Notification from Parse")
//                .setContentIntent(pendingIntent);
//
//        Notification notification = builder.build();
//
//        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        nm.notify(1410, notification);

    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return HomeActivity.class;
    }

    private String getData(String jsonData) {
        // Parse JSON Data
        try {
            System.out.println("JSON Data [" + jsonData + "]");
            JSONObject obj = new JSONObject(jsonData);

            return obj.getString("message");
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        return "";
    }
}