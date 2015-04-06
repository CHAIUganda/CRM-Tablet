package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.chai.model.Region;
import org.chai.model.RegionDao;
import org.chai.model.VillageDao;

/**
 * Created by victor on 06-Apr-15.
 */
public class MigrationHelper1 extends MigratorHelper {
    /* Upgrade from DB schema 1 to schema 2 */
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        //analyse changes in the domains and update accordingly
        db.execSQL("ALTER TABLE "+ VillageDao.TABLENAME+" ADD COLUMN "+VillageDao.Properties.Population.columnName+" TEXT");
        Log.i("Update chai_crm:","Updated table "+ VillageDao.TABLENAME);
    }
}
