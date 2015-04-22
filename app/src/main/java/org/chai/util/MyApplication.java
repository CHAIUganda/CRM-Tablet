package org.chai.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by victor on 23-Mar-15.
 */
public class MyApplication extends Application {
    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
