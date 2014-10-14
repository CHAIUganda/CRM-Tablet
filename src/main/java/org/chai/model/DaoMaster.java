package org.chai.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import org.chai.model.regionDao;
import org.chai.model.districtDao;
import org.chai.model.subcountyDao;
import org.chai.model.parishDao;
import org.chai.model.villageDao;
import org.chai.model.customerDao;
import org.chai.model.customerContactDao;
import org.chai.model.productDao;
import org.chai.model.orderDao;
import org.chai.model.taskDao;
import org.chai.model.promotionDao;
import org.chai.model.promotionalItemDao;
import org.chai.model.saleDao;
import org.chai.model.userDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        regionDao.createTable(db, ifNotExists);
        districtDao.createTable(db, ifNotExists);
        subcountyDao.createTable(db, ifNotExists);
        parishDao.createTable(db, ifNotExists);
        villageDao.createTable(db, ifNotExists);
        customerDao.createTable(db, ifNotExists);
        customerContactDao.createTable(db, ifNotExists);
        productDao.createTable(db, ifNotExists);
        orderDao.createTable(db, ifNotExists);
        taskDao.createTable(db, ifNotExists);
        promotionDao.createTable(db, ifNotExists);
        promotionalItemDao.createTable(db, ifNotExists);
        saleDao.createTable(db, ifNotExists);
        userDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        regionDao.dropTable(db, ifExists);
        districtDao.dropTable(db, ifExists);
        subcountyDao.dropTable(db, ifExists);
        parishDao.dropTable(db, ifExists);
        villageDao.dropTable(db, ifExists);
        customerDao.dropTable(db, ifExists);
        customerContactDao.dropTable(db, ifExists);
        productDao.dropTable(db, ifExists);
        orderDao.dropTable(db, ifExists);
        taskDao.dropTable(db, ifExists);
        promotionDao.dropTable(db, ifExists);
        promotionalItemDao.dropTable(db, ifExists);
        saleDao.dropTable(db, ifExists);
        userDao.dropTable(db, ifExists);
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
        registerDaoClass(regionDao.class);
        registerDaoClass(districtDao.class);
        registerDaoClass(subcountyDao.class);
        registerDaoClass(parishDao.class);
        registerDaoClass(villageDao.class);
        registerDaoClass(customerDao.class);
        registerDaoClass(customerContactDao.class);
        registerDaoClass(productDao.class);
        registerDaoClass(orderDao.class);
        registerDaoClass(taskDao.class);
        registerDaoClass(promotionDao.class);
        registerDaoClass(promotionalItemDao.class);
        registerDaoClass(saleDao.class);
        registerDaoClass(userDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
