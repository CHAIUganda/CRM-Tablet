package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;

import org.chai.model.AdhockSaleDao;
import org.chai.model.ProductGroupDao;
import org.chai.model.SaleDao;
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

            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.StocksORS.columnName, "INTEGER");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.StocksZinc.columnName, "INTEGER");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.StocksACTs.columnName, "INTEGER");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.StocksRDT.columnName, "INTEGER");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.StocksAmox.columnName, "INTEGER");

            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.MinORSPrice.columnName, "TEXT");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.MinZincPrice.columnName, "TEXT");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.MinACTPrice.columnName, "TEXT");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.MinRDTPrice.columnName, "TEXT");
            executeAlterQuery(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.MinAmoxPrice.columnName, "TEXT");

            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.StocksORS.columnName, "INTEGER");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.StocksZinc.columnName, "INTEGER");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.StocksACTs.columnName, "INTEGER");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.StocksRDT.columnName, "INTEGER");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.StocksAmox.columnName, "INTEGER");

            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.MinORSPrice.columnName, "TEXT");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.MinZincPrice.columnName, "TEXT");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.MinACTPrice.columnName, "TEXT");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.MinRDTPrice.columnName, "TEXT");
            executeAlterQuery(db, SaleDao.TABLENAME, SaleDao.Properties.MinAmoxPrice.columnName, "TEXT");
        }catch (Exception ex){
            Utils.log("Error doing 6 upgrade -> " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void executeAlterQuery(SQLiteDatabase db, String table, String column, String type){
        String sql = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + type;
        Utils.log("Executing -> " + sql);
        db.execSQL(sql);
    }
}
