package org.chai.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by victor on 10/27/14.
 */
public class GPSTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public static final double DEFAULT_LOCATION_ACCURACY = 100.0;
    private Context context;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private boolean isBetterAccurracy = false;

    Location location;
    private double latitude;
    private double longitude;
    private int mLocationCount = 0;

    protected LocationManager locationManager;
    private ProgressDialog mLocationDialog;

    public GPSTracker(Context context){
        this.context = context;
        getLocation();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){
                showSettingsAlert();
            }else{
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    Log.d("Network:",LocationManager.NETWORK_PROVIDER);
                    if(locationManager != null){
                       location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if(isGPSEnabled){
                  if(location == null){
                      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                      Log.d("GPS Enabled", "GPS Enabled");
                      if(locationManager != null){
                          location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                      }
                      if(location != null){
                          latitude = location.getLatitude();
                          longitude = location.getLongitude();
                      }
                  }
                }
            }

        }catch (Exception ex){
            ex.printStackTrace();

        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        if(this.location != null){
            ++mLocationCount;
            Log.i("GPS Tracker","GpsTracker: " + System.currentTimeMillis() +
                    " onLocationChanged(" + mLocationCount + ") lat: " +
                    location.getLatitude() + " long: " +
                    location.getLongitude() + " acc: " +
                    location.getAccuracy() );
            if(mLocationCount>1&&location.getAccuracy() <= DEFAULT_LOCATION_ACCURACY){
               this.latitude = this.location.getLatitude();
                this.longitude = this.location.getLongitude();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle exetras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        //alertDialog.show();
    }
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
}
