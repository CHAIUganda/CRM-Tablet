package org.chai.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.chai.model.District;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DISTRICT.
*/
public class DistrictDao extends AbstractDao<District, String> {

    public static final String TABLENAME = "DISTRICT";

    /**
     * Properties of entity District.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property RegionId = new Property(2, String.class, "regionId", false, "REGION_ID");
    };

    private DaoSession daoSession;

    private Query<District> region_DistrictsQuery;

    public DistrictDao(DaoConfig config) {
        super(config);
    }
    
    public DistrictDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DISTRICT' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL ," + // 0: uuid
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'REGION_ID' TEXT NOT NULL );"); // 2: regionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DISTRICT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, District entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getRegionId());
    }

    @Override
    protected void attachEntity(District entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public District readEntity(Cursor cursor, int offset) {
        District entity = new District( //
            cursor.getString(offset + 0), // uuid
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2) // regionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, District entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setRegionId(cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(District entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(District entity) {
        if(entity != null) {
            return entity.getUuid();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "districts" to-many relationship of Region. */
    public List<District> _queryRegion_Districts(String regionId) {
        synchronized (this) {
            if (region_DistrictsQuery == null) {
                QueryBuilder<District> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RegionId.eq(null));
                region_DistrictsQuery = queryBuilder.build();
            }
        }
        Query<District> query = region_DistrictsQuery.forCurrentThread();
        query.setParameter(0, regionId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRegionDao().getAllColumns());
            builder.append(" FROM DISTRICT T");
            builder.append(" LEFT JOIN REGION T0 ON T.'REGION_ID'=T0.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected District loadCurrentDeep(Cursor cursor, boolean lock) {
        District entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Region region = loadCurrentOther(daoSession.getRegionDao(), cursor, offset);
         if(region != null) {
            entity.setRegion(region);
        }

        return entity;    
    }

    public District loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<District> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<District> list = new ArrayList<District>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<District> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<District> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
