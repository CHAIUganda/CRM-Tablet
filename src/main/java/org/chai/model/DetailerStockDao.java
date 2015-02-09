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

import org.chai.model.DetailerStock;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DETAILER_STOCK.
*/
public class DetailerStockDao extends AbstractDao<DetailerStock, String> {

    public static final String TABLENAME = "DETAILER_STOCK";

    /**
     * Properties of entity DetailerStock.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property Brand = new Property(1, String.class, "brand", false, "BRAND");
        public final static Property Category = new Property(2, String.class, "category", false, "CATEGORY");
        public final static Property StockLevel = new Property(3, double.class, "stockLevel", false, "STOCK_LEVEL");
        public final static Property BuyingPrice = new Property(4, Integer.class, "buyingPrice", false, "BUYING_PRICE");
        public final static Property SellingPrice = new Property(5, Integer.class, "sellingPrice", false, "SELLING_PRICE");
        public final static Property DetailerId = new Property(6, String.class, "detailerId", false, "DETAILER_ID");
    };

    private DaoSession daoSession;

    private Query<DetailerStock> detailerCall_DetailerStocksQuery;

    public DetailerStockDao(DaoConfig config) {
        super(config);
    }
    
    public DetailerStockDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DETAILER_STOCK' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'BRAND' TEXT NOT NULL ," + // 1: brand
                "'CATEGORY' TEXT NOT NULL ," + // 2: category
                "'STOCK_LEVEL' REAL NOT NULL ," + // 3: stockLevel
                "'BUYING_PRICE' INTEGER," + // 4: buyingPrice
                "'SELLING_PRICE' INTEGER," + // 5: sellingPrice
                "'DETAILER_ID' TEXT NOT NULL );"); // 6: detailerId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DETAILER_STOCK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DetailerStock entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
        stmt.bindString(2, entity.getBrand());
        stmt.bindString(3, entity.getCategory());
        stmt.bindDouble(4, entity.getStockLevel());
 
        Integer buyingPrice = entity.getBuyingPrice();
        if (buyingPrice != null) {
            stmt.bindLong(5, buyingPrice);
        }
 
        Integer sellingPrice = entity.getSellingPrice();
        if (sellingPrice != null) {
            stmt.bindLong(6, sellingPrice);
        }
        stmt.bindString(7, entity.getDetailerId());
    }

    @Override
    protected void attachEntity(DetailerStock entity) {
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
    public DetailerStock readEntity(Cursor cursor, int offset) {
        DetailerStock entity = new DetailerStock( //
            cursor.getString(offset + 0), // uuid
            cursor.getString(offset + 1), // brand
            cursor.getString(offset + 2), // category
            cursor.getDouble(offset + 3), // stockLevel
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // buyingPrice
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // sellingPrice
            cursor.getString(offset + 6) // detailerId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DetailerStock entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setBrand(cursor.getString(offset + 1));
        entity.setCategory(cursor.getString(offset + 2));
        entity.setStockLevel(cursor.getDouble(offset + 3));
        entity.setBuyingPrice(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setSellingPrice(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setDetailerId(cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(DetailerStock entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(DetailerStock entity) {
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
    
    /** Internal query to resolve the "detailerStocks" to-many relationship of DetailerCall. */
    public List<DetailerStock> _queryDetailerCall_DetailerStocks(String detailerId) {
        synchronized (this) {
            if (detailerCall_DetailerStocksQuery == null) {
                QueryBuilder<DetailerStock> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.DetailerId.eq(null));
                detailerCall_DetailerStocksQuery = queryBuilder.build();
            }
        }
        Query<DetailerStock> query = detailerCall_DetailerStocksQuery.forCurrentThread();
        query.setParameter(0, detailerId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDetailerCallDao().getAllColumns());
            builder.append(" FROM DETAILER_STOCK T");
            builder.append(" LEFT JOIN DETAILER_CALL T0 ON T.'DETAILER_ID'=T0.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DetailerStock loadCurrentDeep(Cursor cursor, boolean lock) {
        DetailerStock entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DetailerCall detailerCall = loadCurrentOther(daoSession.getDetailerCallDao(), cursor, offset);
         if(detailerCall != null) {
            entity.setDetailerCall(detailerCall);
        }

        return entity;    
    }

    public DetailerStock loadDeep(Long key) {
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
    public List<DetailerStock> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DetailerStock> list = new ArrayList<DetailerStock>(count);
        
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
    
    protected List<DetailerStock> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DetailerStock> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
