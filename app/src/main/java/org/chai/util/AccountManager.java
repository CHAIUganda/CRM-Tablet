package org.chai.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zed on 4/10/2015.
 */
public class AccountManager {
    private static final String PREFS = "account_preferences";
    private static final String USERNAME = "username";

    public static void saveUsername(String username, Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public static String getUsername(Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        return prefs.getString(USERNAME, null);
    }
}
