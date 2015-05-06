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

import org.chai.model.Subcounty;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SUBCOUNTY.
*/
public class SubcountyDao extends AbstractDao<Subcounty, String> {

    public static final String TABLENAME = "SUBCOUNTY";

    /**
     * Properties of entity Subcounty.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property DistrictId = new Property(2, String.class, "districtId", false, "DISTRICT_ID");
    };

    private DaoSession daoSession;

    private Query<Subcounty> district_SubcountiesQuery;

    public SubcountyDao(DaoConfig config) {
        super(config);
    }
    
    public SubcountyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SUBCOUNTY' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'DISTRICT_ID' TEXT NOT NULL );"); // 2: districtId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SUBCOUNTY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Subcounty entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getDistrictId());
    }

    @Override
    protected void attachEntity(Subcounty entity) {
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
    public Subcounty readEntity(Cursor cursor, int offset) {
        Subcounty entity = new Subcounty( //
            cursor.getString(offset + 0), // uuid
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2) // districtId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Subcounty entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setDistrictId(cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Subcounty entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Subcounty entity) {
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
    
    /** Internal query to resolve the "subcounties" to-many relationship of District. */
    public List<Subcounty> _queryDistrict_Subcounties(String districtId) {
        synchronized (this) {
            if (district_SubcountiesQuery == null) {
                QueryBuilder<Subcounty> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.DistrictId.eq(null));
                district_SubcountiesQuery = queryBuilder.build();
            }
        }
        Query<Subcounty> query = district_SubcountiesQuery.forCurrentThread();
        query.setParameter(0, districtId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDistrictDao().getAllColumns());
            builder.append(" FROM SUBCOUNTY T");
            builder.append(" LEFT JOIN DISTRICT T0 ON T.'DISTRICT_ID'=T0.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Subcounty loadCurrentDeep(Cursor cursor, boolean lock) {
        Subcounty entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        District district = loadCurrentOther(daoSession.getDistrictDao(), cursor, offset);
         if(district != null) {
            entity.setDistrict(district);
        }

        return entity;    
    }

    public Subcounty loadDeep(Long key) {
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
    public List<Subcounty> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Subcounty> list = new ArrayList<Subcounty>(count);
        
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
    
    protected List<Subcounty> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Subcounty> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
