package org.chai.sync;

import org.chai.rest.Place;
import org.chai.rest.RestClient;

/**
 * Created by victor on 11/3/14.
 */
public class CHAISynchroniser {

    private Place place;

    public CHAISynchroniser() {
        place = new Place();
    }

    public void startSyncronisationProcess(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                place.downloadRegions();
            }
        }).start();
    }

}
