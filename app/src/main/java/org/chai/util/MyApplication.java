package org.chai.util;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import org.chai.R;
import org.chai.util.migration.UpgradeOpenHelper;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

/**
 * Created by victor on 23-Mar-15.
 */
public class MyApplication extends Application {
    private static Context mContext;
    TrackerSettings settings;
    public static LocationTracker locationTracker;
    public static View gpsContainer;
    public static EditText locationTextField;
    private static FragmentActivity activity;

    public void onCreate() {
        super.onCreate();
        settings = new TrackerSettings();
        settings.setUseGPS(true);
        settings.setUseNetwork(true);
        settings.setUsePassive(true);
        settings.setTimeBetweenUpdates(1000); // 1 sec
        settings.setMetersBetweenUpdates(2);
        locationTracker = new LocationTracker(this, settings) {

            @Override
            public void onLocationFound(Location location) {
                Utils.log("Location found -> " + location.getLatitude() + " : " + location.getLongitude());
                if(locationTextField != null){
                    locationTextField.setText(location.getLatitude() + "," + location.getLongitude());
                    gpsContainer.findViewById(R.id.prg_loader).setVisibility(View.GONE);
                }
                if(locationTracker != null && locationTracker.isListening()){
                    locationTracker.stopListen();
                }
            }

            @Override
            public void onTimeout() {
                Utils.log("onTimeout()");
                if(gpsContainer != null){
                    registerEditTextForLocationUpdates(gpsContainer, activity);
                }
            }
        };

        mContext = getApplicationContext();
    }

    public static void registerEditTextForLocationUpdates(View container, FragmentActivity a){
        activity = a;

        gpsContainer = container;
        locationTextField = (EditText)container.findViewById(R.id.gps);

        gpsContainer.findViewById(R.id.prg_loader).setVisibility(View.VISIBLE);

        locationTracker.startListen(); //Start listening for location updates

        //locationTracker.quickFix(); //Get quick location - if any

        //Check location settings
        if (!locationTracker.isGPSEnabled()) {
            new GPSSettingsDialog().show(activity.getSupportFragmentManager(), "gps_settings");
            return;
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static UpgradeOpenHelper getDbOpenHelper(){
       return new UpgradeOpenHelper(getContext(), "chai-crm-db", null);
    }
    public static UpgradeOpenHelper getInMemoryDbOpenHelper(){
        return new UpgradeOpenHelper(getContext(),null, null);
    }
}
