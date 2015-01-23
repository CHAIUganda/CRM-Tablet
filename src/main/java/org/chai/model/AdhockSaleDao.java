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

import org.chai.model.AdhockSale;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ADHOCK_SALE.
*/
public class AdhockSaleDao extends AbstractDao<AdhockSale, String> {

    public static final String TABLENAME = "ADHOCK_SALE";

    /**
     * Properties of entity AdhockSale.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property DateOfSale = new Property(1, java.util.Date.class, "dateOfSale", false, "DATE_OF_SALE");
        public final static Property DoYouStockOrsZinc = new Property(2, Boolean.class, "doYouStockOrsZinc", false, "DO_YOU_STOCK_ORS_ZINC");
        public final static Property HowManyZincInStock = new Property(3, Integer.class, "howManyZincInStock", false, "HOW_MANY_ZINC_IN_STOCK");
        public final static Property HowManyOrsInStock = new Property(4, Integer.class, "howManyOrsInStock", false, "HOW_MANY_ORS_IN_STOCK");
        public final static Property IfNoWhy = new Property(5, String.class, "ifNoWhy", false, "IF_NO_WHY");
        public final static Property PointOfsaleMaterial = new Property(6, String.class, "pointOfsaleMaterial", false, "POINT_OFSALE_MATERIAL");
        public final static Property RecommendationNextStep = new Property(7, String.class, "recommendationNextStep", false, "RECOMMENDATION_NEXT_STEP");
        public final static Property RecommendationLevel = new Property(8, String.class, "recommendationLevel", false, "RECOMMENDATION_LEVEL");
        public final static Property GovernmentApproval = new Property(9, String.class, "governmentApproval", false, "GOVERNMENT_APPROVAL");
        public final static Property CustomerId = new Property(10, String.class, "customerId", false, "CUSTOMER_ID");
    };

    private DaoSession daoSession;

    private Query<AdhockSale> customer_AdhockSalesQuery;

    public AdhockSaleDao(DaoConfig config) {
        super(config);
    }
    
    public AdhockSaleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ADHOCK_SALE' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'DATE_OF_SALE' INTEGER NOT NULL ," + // 1: dateOfSale
                "'DO_YOU_STOCK_ORS_ZINC' INTEGER," + // 2: doYouStockOrsZinc
                "'HOW_MANY_ZINC_IN_STOCK' INTEGER," + // 3: howManyZincInStock
                "'HOW_MANY_ORS_IN_STOCK' INTEGER," + // 4: howManyOrsInStock
                "'IF_NO_WHY' TEXT," + // 5: ifNoWhy
                "'POINT_OFSALE_MATERIAL' TEXT," + // 6: pointOfsaleMaterial
                "'RECOMMENDATION_NEXT_STEP' TEXT," + // 7: recommendationNextStep
                "'RECOMMENDATION_LEVEL' TEXT," + // 8: recommendationLevel
                "'GOVERNMENT_APPROVAL' TEXT," + // 9: governmentApproval
                "'CUSTOMER_ID' TEXT NOT NULL );"); // 10: customerId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ADHOCK_SALE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AdhockSale entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
        stmt.bindLong(2, entity.getDateOfSale().getTime());
 
        Boolean doYouStockOrsZinc = entity.getDoYouStockOrsZinc();
        if (doYouStockOrsZinc != null) {
            stmt.bindLong(3, doYouStockOrsZinc ? 1l: 0l);
        }
 
        Integer howManyZincInStock = entity.getHowManyZincInStock();
        if (howManyZincInStock != null) {
            stmt.bindLong(4, howManyZincInStock);
        }
 
        Integer howManyOrsInStock = entity.getHowManyOrsInStock();
        if (howManyOrsInStock != null) {
            stmt.bindLong(5, howManyOrsInStock);
        }
 
        String ifNoWhy = entity.getIfNoWhy();
        if (ifNoWhy != null) {
            stmt.bindString(6, ifNoWhy);
        }
 
        String pointOfsaleMaterial = entity.getPointOfsaleMaterial();
        if (pointOfsaleMaterial != null) {
            stmt.bindString(7, pointOfsaleMaterial);
        }
 
        String recommendationNextStep = entity.getRecommendationNextStep();
        if (recommendationNextStep != null) {
            stmt.bindString(8, recommendationNextStep);
        }
 
        String recommendationLevel = entity.getRecommendationLevel();
        if (recommendationLevel != null) {
            stmt.bindString(9, recommendationLevel);
        }
 
        String governmentApproval = entity.getGovernmentApproval();
        if (governmentApproval != null) {
            stmt.bindString(10, governmentApproval);
        }
        stmt.bindString(11, entity.getCustomerId());
    }

    @Override
    protected void attachEntity(AdhockSale entity) {
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
    public AdhockSale readEntity(Cursor cursor, int offset) {
        AdhockSale entity = new AdhockSale( //
            cursor.getString(offset + 0), // uuid
            new java.util.Date(cursor.getLong(offset + 1)), // dateOfSale
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // doYouStockOrsZinc
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // howManyZincInStock
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // howManyOrsInStock
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ifNoWhy
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pointOfsaleMaterial
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // recommendationNextStep
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // recommendationLevel
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // governmentApproval
            cursor.getString(offset + 10) // customerId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AdhockSale entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setDateOfSale(new java.util.Date(cursor.getLong(offset + 1)));
        entity.setDoYouStockOrsZinc(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setHowManyZincInStock(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setHowManyOrsInStock(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setIfNoWhy(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPointOfsaleMaterial(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRecommendationNextStep(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRecommendationLevel(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGovernmentApproval(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCustomerId(cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(AdhockSale entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(AdhockSale entity) {
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
    
    /** Internal query to resolve the "adhockSales" to-many relationship of Customer. */
    public List<AdhockSale> _queryCustomer_AdhockSales(String customerId) {
        synchronized (this) {
            if (customer_AdhockSalesQuery == null) {
                QueryBuilder<AdhockSale> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CustomerId.eq(null));
                queryBuilder.orderRaw("DATE_OF_SALE ASC");
                customer_AdhockSalesQuery = queryBuilder.build();
            }
        }
        Query<AdhockSale> query = customer_AdhockSalesQuery.forCurrentThread();
        query.setParameter(0, customerId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCustomerDao().getAllColumns());
            builder.append(" FROM ADHOCK_SALE T");
            builder.append(" LEFT JOIN CUSTOMER T0 ON T.'CUSTOMER_ID'=T0.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected AdhockSale loadCurrentDeep(Cursor cursor, boolean lock) {
        AdhockSale entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Customer customer = loadCurrentOther(daoSession.getCustomerDao(), cursor, offset);
         if(customer != null) {
            entity.setCustomer(customer);
        }

        return entity;    
    }

    public AdhockSale loadDeep(Long key) {
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
    public List<AdhockSale> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<AdhockSale> list = new ArrayList<AdhockSale>(count);
        
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
    
    protected List<AdhockSale> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<AdhockSale> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
