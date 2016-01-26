package com.project.socialevening;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParsePush;
import com.project.socialevening.utility.AppConstants;
import com.project.socialevening.utility.Logger;
import com.project.socialevening.utility.Preferences;
import com.project.socialevening.utility.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nitin on 29/10/15.
 */
public class AppController extends Application {
    private static AppController instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActiveAndroid.initialize(this);
        Preferences.initSharedPreferences(this);
        Parse.initialize(this, AppConstants.PARSE_APP_ID, AppConstants.PARSE_CLIENT_ID);
    }

    public static AppController getInstance() {
        return instance;
    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.project.socialevening",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Logger.info("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
