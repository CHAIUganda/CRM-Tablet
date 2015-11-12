package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

import org.chai.model.ProductGroupDao;
import org.chai.util.Utils;

/**
 * Created by Zed on 11/12/2015.
 */
public class MigrationHelper5 extends MigratorHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        Utils.log("Doing MigrationHelper 5 Upgrade");
        try{
            ProductGroupDao.createTable(db, true);
        }catch (Exception ex){
            Utils.log("Error creating product group table -> " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
