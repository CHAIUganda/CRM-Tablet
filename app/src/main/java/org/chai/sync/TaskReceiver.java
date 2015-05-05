package org.chai.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.chai.util.Utils;

/**
 * Created by Zed on 5/5/2015.
 */
public class TaskReceiver extends BroadcastReceiver {
    private static final String INTENT_ACTION = "org.chai.SYNCRONISE_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.log("TaskReceiver -> recived");
    }
}
