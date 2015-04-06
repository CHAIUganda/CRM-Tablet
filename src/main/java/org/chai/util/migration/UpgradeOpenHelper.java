package org.chai.util.migration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import org.chai.model.DaoMaster;

/**
 * Created by victor on 06-Apr-15.
 */
public class UpgradeOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = "UpgradeOpenHelper";

    public UpgradeOpenHelper(Context context, String name, CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            try {
//                MigrationHelper+i is the class that migrates from i to i++
                MigratorHelper migratorHelper = (MigratorHelper) Class.forName("org.chai.util.migration.MigrationHelper" + i).newInstance();
                if (migratorHelper != null) {
                    migratorHelper.onUpgrade(sqLiteDatabase);
                }
            } catch (Exception ex) {
                Log.e(TAG, "Could not migrate from schema: " + i + " to " + i++);
                /* If something fail prevent the DB to be updated to future version if the previous version has not been upgraded successfully */
                break;
            }
        }
    }
}
