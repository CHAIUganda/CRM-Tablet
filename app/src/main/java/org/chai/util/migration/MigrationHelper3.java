package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

import org.chai.model.DetailerStockDao;
import org.chai.model.MalariaDetailDao;
import org.chai.util.Utils;

/**
 * Created by Zed on 5/9/2015.
 */
public class MigrationHelper3 extends MigratorHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        Utils.log("Doing MigrationHelper 3 Upgrade");
        //The new Malaria form and pack size
        MalariaDetailDao.createTable(db, true);
        try{
            String sql = "ALTER TABLE " + DetailerStockDao.TABLENAME + " ADD COLUMN " + DetailerStockDao.Properties.PackSize.columnName + " TEXT";
            db.execSQL(sql);
            String sql2 = "ALTER TABLE " + DetailerStockDao.TABLENAME + " ADD COLUMN " + DetailerStockDao.Properties.MalariadetailId.columnName + " TEXT";
            db.execSQL(sql2);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
