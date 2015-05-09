package org.chai.util.migration;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.chai.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 06-Apr-15.
 */
public class MigrationHelper1 extends MigratorHelper {
    private static String TYPE_INTEGER = "INTEGER";
    private static String TYPE_TEXT = "TEXT";
    private static String TYPE_REAL = "REAL";

    /* Upgrade from DB schema 1 to schema 2 */
    @Override
    public void onUpgrade(SQLiteDatabase db) {
        //we added meta fields to most tables,
        addMetaFieldsToAllTables(db);
        //analyse changes in the domains and update accordingly
        updateAdhockSale(db);
        updateCustomer(db);
        updateDetailerCall(db);
        TaskOrderDao.createTable(db, true);
        updateReportSummary(db);

    }

    private void updateReportSummary(SQLiteDatabase db) {
        executeAlterChangeSets(db,SummaryReportDao.TABLENAME,SummaryReportDao.Properties.TeamAverageThisWeek.columnName,TYPE_TEXT);
        executeAlterChangeSets(db,SummaryReportDao.TABLENAME,SummaryReportDao.Properties.TeamAverageThisMonth.columnName,TYPE_TEXT);
    }

    private void updateDetailerCall(SQLiteDatabase db) {
        executeAlterChangeSets(db, DetailerCallDao.TABLENAME,DetailerCallDao.Properties.Objections.columnName,TYPE_TEXT);
    }

    private void updateAdhockSale(SQLiteDatabase db) {
        executeAlterChangeSets(db, AdhockSaleDao.TABLENAME, AdhockSaleDao.Properties.IsHistory.columnName, TYPE_INTEGER);
    }

    private void updateCustomer(SQLiteDatabase db) {
        executeAlterChangeSets(db, CustomerDao.TABLENAME, CustomerDao.Properties.Segment.columnName, TYPE_TEXT);
        List<String> customerColumnsToRemove = new ArrayList<String>();
        customerColumnsToRemove.add("DATE_OUTLET_OPENED");
        dropColumns(db, getCustomerCreateCmd(), CustomerDao.TABLENAME,customerColumnsToRemove);
    }

    private void executeAlterChangeSets(SQLiteDatabase db, String tableName, String columnName, String columnType) {
        try{
            String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
            db.execSQL(sql);
            Log.i("Update chai_crm:", sql);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addMetaFieldsToAllTables(SQLiteDatabase db) {
        List<String> tables = new ArrayList<String>();
        tables.add(CustomerDao.TABLENAME);
        tables.add(CustomerContactDao.TABLENAME);
        tables.add(OrderDao.TABLENAME);
        tables.add(TaskDao.TABLENAME);
        tables.add(SaleDao.TABLENAME);
        tables.add(DetailerCallDao.TABLENAME);
        tables.add(OrderDataDao.TABLENAME);
        tables.add(AdhockSaleDao.TABLENAME);
        tables.add(SaleDataDao.TABLENAME);
        tables.add(StokeDataDao.TABLENAME);
        tables.add(DetailerStockDao.TABLENAME);

        for (String table : tables) {
            executeAlterChangeSets(db, table, CustomerDao.Properties.IsDirty.columnName, TYPE_INTEGER);
            executeAlterChangeSets(db, table, CustomerDao.Properties.SyncronisationStatus.columnName, TYPE_INTEGER);
            executeAlterChangeSets(db, table, CustomerDao.Properties.SyncronisationMessage.columnName, TYPE_TEXT);
            executeAlterChangeSets(db, table, CustomerDao.Properties.DateCreated.columnName, TYPE_INTEGER);
            executeAlterChangeSets(db, table, CustomerDao.Properties.LastUpdated.columnName, TYPE_INTEGER);
        }
    }

    private String getCustomerCreateCmd(){
        String cmd = "CREATE TABLE  'CUSTOMER' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'LATITUDE' REAL," + // 1: latitude
                "'LONGITUDE' REAL," + // 2: longitude
                "'OUTLET_NAME' TEXT NOT NULL ," + // 3: outletName
                "'OUTLET_TYPE' TEXT," + // 4: outletType
                "'OUTLET_SIZE' TEXT," + // 5: outletSize
                "'OUTLET_PICTURE' BLOB," + // 6: outletPicture
                "'SPLIT' TEXT," + // 7: split
                "'MAJORITY_SOURCE_OF_SUPPLY' TEXT," + // 8: majoritySourceOfSupply
                "'KEY_WHOLE_SALER_NAME' TEXT," + // 9: keyWholeSalerName
                "'KEY_WHOLE_SALER_CONTACT' TEXT," + // 10: keyWholeSalerContact
                "'LICENCE_VISIBLE' INTEGER," + // 11: licenceVisible
                "'TYPE_OF_LICENCE' TEXT," + // 12: typeOfLicence
                "'DESCRIPTION_OF_OUTLET_LOCATION' TEXT," + // 13: descriptionOfOutletLocation
                "'NUMBER_OF_EMPLOYEES' INTEGER," + // 14: numberOfEmployees
                "'NUMBER_OF_CUSTOMERS_PER_DAY' INTEGER," + // 15: numberOfCustomersPerDay
                "'RESTOCK_FREQUENCY' TEXT," + // 16: restockFrequency
                "'LENGTH_OPEN' TEXT," + // 17: lengthOpen
                "'TRADING_CENTER' TEXT," + // 18: tradingCenter
                "'SUBCOUNTY_UUID' TEXT," + // 19: subcountyUuid
                "'IS_ACTIVE' INTEGER," + // 20: isActive
                "'SEGMENT' TEXT," + // 21: segment
                "'SUBCOUNTY_ID' TEXT NOT NULL ," + // 22: subcountyId
                "'IS_DIRTY' INTEGER," + // 23: isDirty
                "'SYNCRONISATION_STATUS' INTEGER," + // 24: syncronisationStatus
                "'SYNCRONISATION_MESSAGE' TEXT," + // 25: syncronisationMessage
                "'DATE_CREATED' INTEGER," + // 26: dateCreated
                "'LAST_UPDATED' INTEGER);";
        return cmd;
    }
}
