package org.chai.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import org.chai.model.RegionDao;
import org.chai.model.DistrictDao;
import org.chai.model.SubcountyDao;
import org.chai.model.ParishDao;
import org.chai.model.VillageDao;
import org.chai.model.CustomerDao;
import org.chai.model.CustomerContactDao;
import org.chai.model.ProductGroupDao;
import org.chai.model.ProductDao;
import org.chai.model.OrderDao;
import org.chai.model.TaskDao;
import org.chai.model.TaskOrderDao;
import org.chai.model.PromotionDao;
import org.chai.model.PromotionalItemDao;
import org.chai.model.SaleDao;
import org.chai.model.UserDao;
import org.chai.model.DetailerCallDao;
import org.chai.model.MalariaDetailDao;
import org.chai.model.OrderDataDao;
import org.chai.model.AdhockSaleDao;
import org.chai.model.SaleDataDao;
import org.chai.model.StokeDataDao;
import org.chai.model.DetailerStockDao;
import org.chai.model.SummaryReportDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 5): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 5;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        RegionDao.createTable(db, ifNotExists);
        DistrictDao.createTable(db, ifNotExists);
        SubcountyDao.createTable(db, ifNotExists);
        ParishDao.createTable(db, ifNotExists);
        VillageDao.createTable(db, ifNotExists);
        CustomerDao.createTable(db, ifNotExists);
        CustomerContactDao.createTable(db, ifNotExists);
        ProductGroupDao.createTable(db, ifNotExists);
        ProductDao.createTable(db, ifNotExists);
        OrderDao.createTable(db, ifNotExists);
        TaskDao.createTable(db, ifNotExists);
        TaskOrderDao.createTable(db, ifNotExists);
        PromotionDao.createTable(db, ifNotExists);
        PromotionalItemDao.createTable(db, ifNotExists);
        SaleDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        DetailerCallDao.createTable(db, ifNotExists);
        MalariaDetailDao.createTable(db, ifNotExists);
        OrderDataDao.createTable(db, ifNotExists);
        AdhockSaleDao.createTable(db, ifNotExists);
        SaleDataDao.createTable(db, ifNotExists);
        StokeDataDao.createTable(db, ifNotExists);
        DetailerStockDao.createTable(db, ifNotExists);
        SummaryReportDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        RegionDao.dropTable(db, ifExists);
        DistrictDao.dropTable(db, ifExists);
        SubcountyDao.dropTable(db, ifExists);
        ParishDao.dropTable(db, ifExists);
        VillageDao.dropTable(db, ifExists);
        CustomerDao.dropTable(db, ifExists);
        CustomerContactDao.dropTable(db, ifExists);
        ProductGroupDao.dropTable(db, ifExists);
        ProductDao.dropTable(db, ifExists);
        OrderDao.dropTable(db, ifExists);
        TaskDao.dropTable(db, ifExists);
        TaskOrderDao.dropTable(db, ifExists);
        PromotionDao.dropTable(db, ifExists);
        PromotionalItemDao.dropTable(db, ifExists);
        SaleDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        DetailerCallDao.dropTable(db, ifExists);
        MalariaDetailDao.dropTable(db, ifExists);
        OrderDataDao.dropTable(db, ifExists);
        AdhockSaleDao.dropTable(db, ifExists);
        SaleDataDao.dropTable(db, ifExists);
        StokeDataDao.dropTable(db, ifExists);
        DetailerStockDao.dropTable(db, ifExists);
        SummaryReportDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(RegionDao.class);
        registerDaoClass(DistrictDao.class);
        registerDaoClass(SubcountyDao.class);
        registerDaoClass(ParishDao.class);
        registerDaoClass(VillageDao.class);
        registerDaoClass(CustomerDao.class);
        registerDaoClass(CustomerContactDao.class);
        registerDaoClass(ProductGroupDao.class);
        registerDaoClass(ProductDao.class);
        registerDaoClass(OrderDao.class);
        registerDaoClass(TaskDao.class);
        registerDaoClass(TaskOrderDao.class);
        registerDaoClass(PromotionDao.class);
        registerDaoClass(PromotionalItemDao.class);
        registerDaoClass(SaleDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(DetailerCallDao.class);
        registerDaoClass(MalariaDetailDao.class);
        registerDaoClass(OrderDataDao.class);
        registerDaoClass(AdhockSaleDao.class);
        registerDaoClass(SaleDataDao.class);
        registerDaoClass(StokeDataDao.class);
        registerDaoClass(DetailerStockDao.class);
        registerDaoClass(SummaryReportDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
