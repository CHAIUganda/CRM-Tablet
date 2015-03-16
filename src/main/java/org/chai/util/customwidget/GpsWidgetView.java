package org.chai.util.customwidget;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import org.chai.R;

import java.text.DecimalFormat;

/**
 * Created by victor on 2/11/15.
 */
public class GpsWidgetView extends LinearLayout implements LocationListener{

    private EditText latLongTxtView;
    private Button captureGpsBtn;
    private String latLongText;
    private Context context;

    public static final double DEFAULT_LOCATION_ACCURACY = 15.0;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private boolean isBetterAccurracy = false;
    private Location mlocation;
    private int mLocationCount = 0;
    protected LocationManager locationManager;
    private ProgressDialog mLocationDialog;

    public GpsWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.gps_template, this, true);

        latLongTxtView = (EditText)getChildAt(0);
        captureGpsBtn = (Button)getChildAt(1);
        captureGpsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationDialog();
                getLocation();
            }
        });
    }

    public String getLatLongText() {
        return latLongTxtView.getText().toString();
    }

    public void setError(String message){
        latLongTxtView.setError(message);
    }

    public void setLatLongText(String latLongText) {
        if(latLongText.equals("null,null")){
            latLongText = "";
        }
        this.latLongText = latLongText;
        this.latLongTxtView.setText(latLongText);
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){
                // no network provider is enabled
            }else{
                this.canGetLocation =true;
                if(isGPSEnabled){
                    if(mlocation == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if(locationManager != null){
                            mlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                      /*  if(mlocation != null){
                            latLongTxtView.setText(mlocation.getLatitude()+","+ mlocation.getLongitude());
                        }*/
                    }
                }
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
                    Log.d("Network:", LocationManager.NETWORK_PROVIDER);
                    if(locationManager != null){
                        mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                       /* if(mlocation != null){
                            latLongTxtView.setText(mlocation.getLatitude()+","+ mlocation.getLongitude());
                        }*/
                    }
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();

        }
        return mlocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mlocation = location;
        if(this.mlocation !=null){
            ++mLocationCount;
            Log.i("GPS Tracker","GpsWidgetView: " + System.currentTimeMillis() +
                    " onLocationChanged(" + mLocationCount + ") lat: " +
                    location.getLatitude() + " long: " +
                    location.getLongitude() + " acc: " +
                    location.getAccuracy() );
            if(mLocationCount>1 && mLocationDialog!=null){
                mLocationDialog.setMessage(context.getString(R.string.location_provider_accuracy, this.mlocation.getProvider(), truncateDouble(location.getAccuracy())));
                if(location.getAccuracy() <= DEFAULT_LOCATION_ACCURACY){
                    returnLocation();
                }
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

    public void setBetterAccurracy(boolean isBetterAccurracy) {
        this.isBetterAccurracy = isBetterAccurracy;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
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
        alertDialog.show();
    }
    /**
     * Sets up the look and actions for the progress dialog while the GPS is searching.
     */
    public void showLocationDialog() {
        // dialog displayed while fetching gps mlocation
        mLocationDialog = new ProgressDialog(context);
        DialogInterface.OnClickListener geopointButtonListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                returnLocation();
                                break;
                            case DialogInterface. BUTTON_NEGATIVE:
                                mlocation = null;
                                break;
                        }
                    }
                };

        // back button doesn't cancel
        mLocationDialog.setCancelable(false);
        mLocationDialog.setIndeterminate(true);
        mLocationDialog.setIcon(android.R.drawable.ic_dialog_info);
        mLocationDialog.setTitle("Getting GPS Location.");
        mLocationDialog.setMessage("Please wait..");
        mLocationDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Capture",
                geopointButtonListener);
        mLocationDialog.setButton(DialogInterface. BUTTON_NEGATIVE,"Cancel",
                geopointButtonListener);
        mLocationDialog.show();
    }

    private void returnLocation(){
        if (mlocation != null&&mLocationDialog!=null&&mLocationDialog.isShowing())  {
            latLongTxtView.setText(mlocation.getLatitude()+","+ mlocation.getLongitude());
            mLocationDialog.dismiss();
        }
    }
    private String truncateDouble(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    public Location getMlocation() {
        return mlocation;
    }

    public void setMlocation(Location mlocation) {
        this.mlocation = mlocation;
    }
}
