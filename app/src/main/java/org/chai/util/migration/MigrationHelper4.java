package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

import org.chai.model.ProductDao;
import org.chai.util.Utils;

/**
 * Created by Zed on 8/20/2015.
 */
public class MigrationHelper4 extends MigratorHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        Utils.log("Doing MigrationHelper 4 Upgrade - Product Upgrade");
        //The new Product groupId
        try{
            String sql = "ALTER TABLE " + ProductDao.TABLENAME + " ADD COLUMN " + ProductDao.Properties.GroupId.columnName + " TEXT";
            db.execSQL(sql);
        }catch (Exception ex){
            Utils.log("Error adding column -> " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
