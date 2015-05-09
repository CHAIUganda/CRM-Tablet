package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

import org.chai.model.MalariaDetailDao;

/**
 * Created by Zed on 5/9/2015.
 */
public class MigrationHelper2 extends MigratorHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        MalariaDetailDao.createTable(db, true);
    }
}
