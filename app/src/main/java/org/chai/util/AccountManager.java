package org.chai.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.chai.activities.LoginActivity;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.User;
import org.chai.model.UserDao;
import org.chai.rest.RestClient;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.List;

/**
 * Created by Zed on 4/10/2015.
 */
public class AccountManager {
    private static final String PREFS = "account_preferences";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static void saveUsername(String username, Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public static String getUsername(Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        return prefs.getString(USERNAME, "");
    }

    public static void savePassword(String password, Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PASSWORD, Utils.encrypeString(password));
        editor.commit();
    }

    public static String getPassword(Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        return prefs.getString(PASSWORD, "");
    }

    public static boolean offlineLogin(Context cxt, boolean redirect){
        UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
        SQLiteDatabase db = helper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();

        String user = getUsername(cxt);
        String pass = getPassword(cxt);

        List<User> loggedInUser = userDao.queryBuilder().where(UserDao.Properties.UserName.eq(user), UserDao.Properties.Password.eq(pass)).list();

        if(!loggedInUser.isEmpty()){
            RestClient.userName = user;
            RestClient.password = Utils.decryptString(pass);
            RestClient.role = loggedInUser.get(0).getRole();

            return true;
        }

        if(redirect){
            Toast.makeText(cxt, "Please login", Toast.LENGTH_LONG).show();
            Intent i = new Intent(cxt, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            cxt.startActivity(i);
        }

        return false;
    }

    public static void logout(Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(USERNAME);
        editor.commit();
        Intent i = new Intent(cxt, LoginActivity.class);
        cxt.startActivity(i);
    }
}
