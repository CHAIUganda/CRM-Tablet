package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by victor on 06-Apr-15.
 */
public abstract class MigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);
}
