package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

import org.chai.model.DetailerStockDao;
import org.chai.model.MalariaDetailDao;
import org.chai.util.Utils;

/**
 * Created by Zed on 5/9/2015.
 */
public class MigrationHelper2 extends MigratorHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        MalariaDetailDao.createTable(db, true);
        try{
            String sql = "ALTER TABLE " + DetailerStockDao.TABLENAME + " ADD COLUMN " + DetailerStockDao.Properties.PackSize.columnName + " TEXT";
            db.execSQL(sql);
            Utils.log("Update chai_crm: " + sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
