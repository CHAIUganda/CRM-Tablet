package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by victor on 06-Apr-15.
 */
public class Migrate1To2 extends MigratorHelper {
    /* Upgrade from DB schema 1 to schema 2 */
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        //analyse changes in the domains and update accordingly
    }
}
