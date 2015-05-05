package org.chai.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.chai.util.Utils;

/**
 * Created by Zed on 5/5/2015.
 */
public class BootAndUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.log("Boot completed recieved");
        context.startService(new Intent(context, CHAISynchroniser.class));
    }
}
