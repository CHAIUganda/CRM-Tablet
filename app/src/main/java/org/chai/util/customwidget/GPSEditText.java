package org.chai.util.customwidget;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by Zed on 6/30/2015.
 */
public class GPSEditText extends EditText {
    private int progressSpinnerId = -1;
    private Location location;
    ProgressBar progressBar;

    public GPSEditText(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setEnabled(false);
    }

    public void startListeningToLocationUpdates(){
        if(progressSpinnerId != -1){
            progressBar = (ProgressBar)((ViewGroup)this.getParent()).findViewById(progressSpinnerId);
            progressBar.setIndeterminate(true);
        }
    }

    public void setLocation(Location l){
        this.location = l;
        this.setText(location.getLatitude() + "," + location.getLongitude());
    }

    public Location getLocation(){
        return this.location;
    }
}
