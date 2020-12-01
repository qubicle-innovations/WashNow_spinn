package com.spinwash;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by aswin on 06/08/17.
 */

public class WashNowApplication    extends MultiDexApplication {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public WashNowApplication() {
        super();
    }
    synchronized public FirebaseAnalytics getDefaultTracker() {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        }
        return mFirebaseAnalytics;
    }
}
