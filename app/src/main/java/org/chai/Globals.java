package org.chai;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

import org.chai.util.GPSTracker;
import org.chai.util.Utils;

/**
 * Created by victor on 2/24/15.
 */
public class Globals{
    private static  Globals instance;

    private GPSTracker gpsTracker;

    private Globals(){}

    public void initGpsTracker(final Activity activity){
        Utils.log("Initializing GPS Tracker");
       if(gpsTracker == null){
           activity.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   gpsTracker = new GPSTracker(activity.getApplicationContext());
               }
           });
       }
    }

    public GPSTracker getGpsTracker(FragmentManager fragmentManager) {
        if(!gpsTracker.isGPSEnabled){
            //new GPSSettingsDialog().show(fragmentManager, "gps_settings_dialog");
        }
        return gpsTracker;
    }

    public static synchronized Globals getInstance(){
        if(instance == null){
            instance = new Globals();
        }
        return instance;
    }
}
