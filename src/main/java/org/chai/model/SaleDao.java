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

import org.chai.model.Sale;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SALE.
*/
public class SaleDao extends AbstractDao<Sale, Long> {

    public static final String TABLENAME = "SALE";

    /**
     * Properties of entity Sale.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uuid = new Property(1, String.class, "uuid", false, "UUID");
        public final static Property DateOfSale = new Property(2, java.util.Date.class, "dateOfSale", false, "DATE_OF_SALE");
        public final static Property DoYouStockOrsZinc = new Property(3, Boolean.class, "doYouStockOrsZinc", false, "DO_YOU_STOCK_ORS_ZINC");
        public final static Property HowManyZincInStock = new Property(4, Integer.class, "howManyZincInStock", false, "HOW_MANY_ZINC_IN_STOCK");
        public final static Property HowmanyOrsInStock = new Property(5, Integer.class, "howmanyOrsInStock", false, "HOWMANY_ORS_IN_STOCK");
        public final static Property IfNoWhy = new Property(6, String.class, "ifNoWhy", false, "IF_NO_WHY");
        public final static Property PointOfsaleMaterial = new Property(7, String.class, "pointOfsaleMaterial", false, "POINT_OFSALE_MATERIAL");
        public final static Property RecommendationNextStep = new Property(8, String.class, "recommendationNextStep", false, "RECOMMENDATION_NEXT_STEP");
        public final static Property RecommendationLevel = new Property(9, String.class, "recommendationLevel", false, "RECOMMENDATION_LEVEL");
        public final static Property GovernmentApproval = new Property(10, String.class, "governmentApproval", false, "GOVERNMENT_APPROVAL");
        public final static Property OrderId = new Property(11, long.class, "orderId", false, "ORDER_ID");
        public final static Property Quantity = new Property(12, int.class, "quantity", false, "QUANTITY");
        public final static Property SalePrice = new Property(13, int.class, "salePrice", false, "SALE_PRICE");
    };

    private DaoSession daoSession;

    private Query<Sale> order_SalesQuery;

    public SaleDao(DaoConfig config) {
        super(config);
    }
    
    public SaleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SALE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'UUID' TEXT NOT NULL UNIQUE ," + // 1: uuid
                "'DATE_OF_SALE' INTEGER NOT NULL ," + // 2: dateOfSale
                "'DO_YOU_STOCK_ORS_ZINC' INTEGER," + // 3: doYouStockOrsZinc
                "'HOW_MANY_ZINC_IN_STOCK' INTEGER," + // 4: howManyZincInStock
                "'HOWMANY_ORS_IN_STOCK' INTEGER," + // 5: howmanyOrsInStock
                "'IF_NO_WHY' TEXT," + // 6: ifNoWhy
                "'POINT_OFSALE_MATERIAL' TEXT," + // 7: pointOfsaleMaterial
                "'RECOMMENDATION_NEXT_STEP' TEXT," + // 8: recommendationNextStep
                "'RECOMMENDATION_LEVEL' TEXT," + // 9: recommendationLevel
                "'GOVERNMENT_APPROVAL' TEXT," + // 10: governmentApproval
                "'ORDER_ID' INTEGER NOT NULL ," + // 11: orderId
                "'QUANTITY' INTEGER NOT NULL ," + // 12: quantity
                "'SALE_PRICE' INTEGER NOT NULL );"); // 13: salePrice
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SALE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Sale entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUuid());
        stmt.bindLong(3, entity.getDateOfSale().getTime());
 
        Boolean doYouStockOrsZinc = entity.getDoYouStockOrsZinc();
        if (doYouStockOrsZinc != null) {
            stmt.bindLong(4, doYouStockOrsZinc ? 1l: 0l);
        }
 
        Integer howManyZincInStock = entity.getHowManyZincInStock();
        if (howManyZincInStock != null) {
            stmt.bindLong(5, howManyZincInStock);
        }
 
        Integer howmanyOrsInStock = entity.getHowmanyOrsInStock();
        if (howmanyOrsInStock != null) {
            stmt.bindLong(6, howmanyOrsInStock);
        }
 
        String ifNoWhy = entity.getIfNoWhy();
        if (ifNoWhy != null) {
            stmt.bindString(7, ifNoWhy);
        }
 
        String pointOfsaleMaterial = entity.getPointOfsaleMaterial();
        if (pointOfsaleMaterial != null) {
            stmt.bindString(8, pointOfsaleMaterial);
        }
 
        String recommendationNextStep = entity.getRecommendationNextStep();
        if (recommendationNextStep != null) {
            stmt.bindString(9, recommendationNextStep);
        }
 
        String recommendationLevel = entity.getRecommendationLevel();
        if (recommendationLevel != null) {
            stmt.bindString(10, recommendationLevel);
        }
 
        String governmentApproval = entity.getGovernmentApproval();
        if (governmentApproval != null) {
            stmt.bindString(11, governmentApproval);
        }
        stmt.bindLong(12, entity.getOrderId());
        stmt.bindLong(13, entity.getQuantity());
        stmt.bindLong(14, entity.getSalePrice());
    }

    @Override
    protected void attachEntity(Sale entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Sale readEntity(Cursor cursor, int offset) {
        Sale entity = new Sale( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // uuid
            new java.util.Date(cursor.getLong(offset + 2)), // dateOfSale
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // doYouStockOrsZinc
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // howManyZincInStock
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // howmanyOrsInStock
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // ifNoWhy
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // pointOfsaleMaterial
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // recommendationNextStep
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // recommendationLevel
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // governmentApproval
            cursor.getLong(offset + 11), // orderId
            cursor.getInt(offset + 12), // quantity
            cursor.getInt(offset + 13) // salePrice
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Sale entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUuid(cursor.getString(offset + 1));
        entity.setDateOfSale(new java.util.Date(cursor.getLong(offset + 2)));
        entity.setDoYouStockOrsZinc(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setHowManyZincInStock(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setHowmanyOrsInStock(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setIfNoWhy(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPointOfsaleMaterial(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRecommendationNextStep(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRecommendationLevel(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGovernmentApproval(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setOrderId(cursor.getLong(offset + 11));
        entity.setQuantity(cursor.getInt(offset + 12));
        entity.setSalePrice(cursor.getInt(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Sale entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Sale entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "sales" to-many relationship of Order. */
    public List<Sale> _queryOrder_Sales(long orderId) {
        synchronized (this) {
            if (order_SalesQuery == null) {
                QueryBuilder<Sale> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.OrderId.eq(null));
                queryBuilder.orderRaw("DATE_OF_SALE ASC");
                order_SalesQuery = queryBuilder.build();
            }
        }
        Query<Sale> query = order_SalesQuery.forCurrentThread();
        query.setParameter(0, orderId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getOrderDao().getAllColumns());
            builder.append(" FROM SALE T");
            builder.append(" LEFT JOIN orders T0 ON T.'ORDER_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Sale loadCurrentDeep(Cursor cursor, boolean lock) {
        Sale entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Order order = loadCurrentOther(daoSession.getOrderDao(), cursor, offset);
         if(order != null) {
            entity.setOrder(order);
        }

        return entity;    
    }

    public Sale loadDeep(Long key) {
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
    public List<Sale> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Sale> list = new ArrayList<Sale>(count);
        
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
    
    protected List<Sale> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Sale> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
