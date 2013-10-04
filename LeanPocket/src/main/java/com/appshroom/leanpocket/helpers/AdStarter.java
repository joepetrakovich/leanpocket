package com.appshroom.leanpocket.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * Created by jpetrakovich on 9/24/13.
 */
public class AdStarter {

    private final static int DAYS_UNTIL_ADS = 5;


    /**
     * Call this method at the end of your OnCreate method to determine whether
     * to show ads
     */
    public static void app_launched(Context context, AdView adView) {

        SharedPreferences mSecPrefs = new SecurePreferences(context);

        if (mSecPrefs.getBoolean(Consts.SHARED_PREFS_IS_PREMIUM, false)) {
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences("adstarter", 0);

        SharedPreferences.Editor editor = prefs.edit();

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait for the number of days used
        // until ads start

        if (System.currentTimeMillis() >= date_firstLaunch
                + (DAYS_UNTIL_ADS * 24 * 60 * 60 * 1000)) {

            showAds(adView);

        }


        editor.commit();
    }


    public static void showAds(AdView adView) {

        adView.loadAd(new AdRequest());
    }


}
