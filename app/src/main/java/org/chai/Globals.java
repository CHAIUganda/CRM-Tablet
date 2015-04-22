package org.chai;

import android.app.Activity;

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
       if(gpsTracker == null){
           activity.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   gpsTracker = new GPSTracker(activity.getApplicationContext());
               }
           });
       }
    }

    public GPSTracker getGpsTracker() {
        Utils.log("Getting GPS Tracker -> " + gpsTracker);
        return gpsTracker;
    }

    public static synchronized Globals getInstance(){
        if(instance == null){
            instance = new Globals();
        }
        return instance;
    }
}
