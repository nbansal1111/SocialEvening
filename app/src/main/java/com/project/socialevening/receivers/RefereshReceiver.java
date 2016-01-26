package com.project.socialevening.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by nitin on 04/12/15.
 */
public class RefereshReceiver extends BroadcastReceiver {

    public ArrayList<RefreshListner> listeners = new ArrayList<>();
    public static final String ACTION_REFRESH = "REFRESH";

    public RefereshReceiver(RefreshListner listener) {
        this.listeners.add(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_REFRESH)) {
            for (RefreshListner listener : listeners) {
                listener.onReceive(intent);
            }
        }
    }

    public void removeListener(RefreshListner listener){
        this.listeners.remove(listener);
    }

    public static interface RefreshListner {
        public void onReceive(Intent intent);
    }
}
