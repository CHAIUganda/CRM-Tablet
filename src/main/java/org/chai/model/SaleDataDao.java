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

import org.chai.model.SaleData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SALE_DATA.
*/
public class SaleDataDao extends AbstractDao<SaleData, String> {

    public static final String TABLENAME = "SALE_DATA";

    /**
     * Properties of entity SaleData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property Quantity = new Property(1, int.class, "quantity", false, "QUANTITY");
        public final static Property Price = new Property(2, int.class, "price", false, "PRICE");
        public final static Property SaleId = new Property(3, String.class, "saleId", false, "SALE_ID");
        public final static Property AdhockSaleId = new Property(4, String.class, "adhockSaleId", false, "ADHOCK_SALE_ID");
        public final static Property ProductId = new Property(5, String.class, "productId", false, "PRODUCT_ID");
    };

    private DaoSession daoSession;

    private Query<SaleData> sale_SalesDatasQuery;
    private Query<SaleData> adhockSale_AdhockSalesDatasQuery;
    private Query<SaleData> product_SalesDatasQuery;

    public SaleDataDao(DaoConfig config) {
        super(config);
    }
    
    public SaleDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SALE_DATA' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'QUANTITY' INTEGER NOT NULL ," + // 1: quantity
                "'PRICE' INTEGER NOT NULL ," + // 2: price
                "'SALE_ID' TEXT," + // 3: saleId
                "'ADHOCK_SALE_ID' TEXT," + // 4: adhockSaleId
                "'PRODUCT_ID' TEXT NOT NULL );"); // 5: productId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SALE_DATA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SaleData entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
        stmt.bindLong(2, entity.getQuantity());
        stmt.bindLong(3, entity.getPrice());
 
        String saleId = entity.getSaleId();
        if (saleId != null) {
            stmt.bindString(4, saleId);
        }
 
        String adhockSaleId = entity.getAdhockSaleId();
        if (adhockSaleId != null) {
            stmt.bindString(5, adhockSaleId);
        }
        stmt.bindString(6, entity.getProductId());
    }

    @Override
    protected void attachEntity(SaleData entity) {
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
    public SaleData readEntity(Cursor cursor, int offset) {
        SaleData entity = new SaleData( //
            cursor.getString(offset + 0), // uuid
            cursor.getInt(offset + 1), // quantity
            cursor.getInt(offset + 2), // price
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // saleId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // adhockSaleId
            cursor.getString(offset + 5) // productId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SaleData entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setQuantity(cursor.getInt(offset + 1));
        entity.setPrice(cursor.getInt(offset + 2));
        entity.setSaleId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAdhockSaleId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setProductId(cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(SaleData entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(SaleData entity) {
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
    
    /** Internal query to resolve the "salesDatas" to-many relationship of Sale. */
    public List<SaleData> _querySale_SalesDatas(String saleId) {
        synchronized (this) {
            if (sale_SalesDatasQuery == null) {
                QueryBuilder<SaleData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.SaleId.eq(null));
                sale_SalesDatasQuery = queryBuilder.build();
            }
        }
        Query<SaleData> query = sale_SalesDatasQuery.forCurrentThread();
        query.setParameter(0, saleId);
        return query.list();
    }

    /** Internal query to resolve the "adhockSalesDatas" to-many relationship of AdhockSale. */
    public List<SaleData> _queryAdhockSale_AdhockSalesDatas(String adhockSaleId) {
        synchronized (this) {
            if (adhockSale_AdhockSalesDatasQuery == null) {
                QueryBuilder<SaleData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.AdhockSaleId.eq(null));
                adhockSale_AdhockSalesDatasQuery = queryBuilder.build();
            }
        }
        Query<SaleData> query = adhockSale_AdhockSalesDatasQuery.forCurrentThread();
        query.setParameter(0, adhockSaleId);
        return query.list();
    }

    /** Internal query to resolve the "salesDatas" to-many relationship of Product. */
    public List<SaleData> _queryProduct_SalesDatas(String productId) {
        synchronized (this) {
            if (product_SalesDatasQuery == null) {
                QueryBuilder<SaleData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ProductId.eq(null));
                product_SalesDatasQuery = queryBuilder.build();
            }
        }
        Query<SaleData> query = product_SalesDatasQuery.forCurrentThread();
        query.setParameter(0, productId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getSaleDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getProductDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getAdhockSaleDao().getAllColumns());
            builder.append(" FROM SALE_DATA T");
            builder.append(" LEFT JOIN SALE T0 ON T.'SALE_ID'=T0.'UUID'");
            builder.append(" LEFT JOIN PRODUCT T1 ON T.'PRODUCT_ID'=T1.'UUID'");
            builder.append(" LEFT JOIN ADHOCK_SALE T2 ON T.'ADHOCK_SALE_ID'=T2.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected SaleData loadCurrentDeep(Cursor cursor, boolean lock) {
        SaleData entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Sale sale = loadCurrentOther(daoSession.getSaleDao(), cursor, offset);
        entity.setSale(sale);
        offset += daoSession.getSaleDao().getAllColumns().length;

        Product product = loadCurrentOther(daoSession.getProductDao(), cursor, offset);
         if(product != null) {
            entity.setProduct(product);
        }
        offset += daoSession.getProductDao().getAllColumns().length;

        AdhockSale adhockSale = loadCurrentOther(daoSession.getAdhockSaleDao(), cursor, offset);
        entity.setAdhockSale(adhockSale);

        return entity;    
    }

    public SaleData loadDeep(Long key) {
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
    public List<SaleData> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<SaleData> list = new ArrayList<SaleData>(count);
        
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
    
    protected List<SaleData> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<SaleData> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
