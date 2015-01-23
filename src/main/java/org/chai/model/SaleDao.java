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
public class SaleDao extends AbstractDao<Sale, String> {

    public static final String TABLENAME = "SALE";

    /**
     * Properties of entity Sale.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property DateOfSale = new Property(1, java.util.Date.class, "dateOfSale", false, "DATE_OF_SALE");
        public final static Property DoYouStockOrsZinc = new Property(2, Boolean.class, "doYouStockOrsZinc", false, "DO_YOU_STOCK_ORS_ZINC");
        public final static Property HowManyZincInStock = new Property(3, Integer.class, "howManyZincInStock", false, "HOW_MANY_ZINC_IN_STOCK");
        public final static Property HowmanyOrsInStock = new Property(4, Integer.class, "howmanyOrsInStock", false, "HOWMANY_ORS_IN_STOCK");
        public final static Property IfNoWhy = new Property(5, String.class, "ifNoWhy", false, "IF_NO_WHY");
        public final static Property PointOfsaleMaterial = new Property(6, String.class, "pointOfsaleMaterial", false, "POINT_OFSALE_MATERIAL");
        public final static Property RecommendationNextStep = new Property(7, String.class, "recommendationNextStep", false, "RECOMMENDATION_NEXT_STEP");
        public final static Property RecommendationLevel = new Property(8, String.class, "recommendationLevel", false, "RECOMMENDATION_LEVEL");
        public final static Property GovernmentApproval = new Property(9, String.class, "governmentApproval", false, "GOVERNMENT_APPROVAL");
        public final static Property OrderId = new Property(10, String.class, "orderId", false, "ORDER_ID");
        public final static Property TaskId = new Property(11, String.class, "taskId", false, "TASK_ID");
    };

    private DaoSession daoSession;

    private Query<Sale> order_SalesQuery;
    private Query<Sale> task_SalesQuery;

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
                "'UUID' TEXT PRIMARY KEY NOT NULL ," + // 0: uuid
                "'DATE_OF_SALE' INTEGER NOT NULL ," + // 1: dateOfSale
                "'DO_YOU_STOCK_ORS_ZINC' INTEGER," + // 2: doYouStockOrsZinc
                "'HOW_MANY_ZINC_IN_STOCK' INTEGER," + // 3: howManyZincInStock
                "'HOWMANY_ORS_IN_STOCK' INTEGER," + // 4: howmanyOrsInStock
                "'IF_NO_WHY' TEXT," + // 5: ifNoWhy
                "'POINT_OFSALE_MATERIAL' TEXT," + // 6: pointOfsaleMaterial
                "'RECOMMENDATION_NEXT_STEP' TEXT," + // 7: recommendationNextStep
                "'RECOMMENDATION_LEVEL' TEXT," + // 8: recommendationLevel
                "'GOVERNMENT_APPROVAL' TEXT," + // 9: governmentApproval
                "'ORDER_ID' TEXT NOT NULL ," + // 10: orderId
                "'TASK_ID' TEXT NOT NULL );"); // 11: taskId
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
 
        Integer howmanyOrsInStock = entity.getHowmanyOrsInStock();
        if (howmanyOrsInStock != null) {
            stmt.bindLong(5, howmanyOrsInStock);
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
        stmt.bindString(11, entity.getOrderId());
        stmt.bindString(12, entity.getTaskId());
    }

    @Override
    protected void attachEntity(Sale entity) {
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
    public Sale readEntity(Cursor cursor, int offset) {
        Sale entity = new Sale( //
            cursor.getString(offset + 0), // uuid
            new java.util.Date(cursor.getLong(offset + 1)), // dateOfSale
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // doYouStockOrsZinc
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // howManyZincInStock
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // howmanyOrsInStock
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ifNoWhy
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pointOfsaleMaterial
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // recommendationNextStep
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // recommendationLevel
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // governmentApproval
            cursor.getString(offset + 10), // orderId
            cursor.getString(offset + 11) // taskId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Sale entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setDateOfSale(new java.util.Date(cursor.getLong(offset + 1)));
        entity.setDoYouStockOrsZinc(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setHowManyZincInStock(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setHowmanyOrsInStock(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setIfNoWhy(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPointOfsaleMaterial(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRecommendationNextStep(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRecommendationLevel(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGovernmentApproval(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setOrderId(cursor.getString(offset + 10));
        entity.setTaskId(cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Sale entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Sale entity) {
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
    
    /** Internal query to resolve the "sales" to-many relationship of Order. */
    public List<Sale> _queryOrder_Sales(String orderId) {
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

    /** Internal query to resolve the "sales" to-many relationship of Task. */
    public List<Sale> _queryTask_Sales(String taskId) {
        synchronized (this) {
            if (task_SalesQuery == null) {
                QueryBuilder<Sale> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TaskId.eq(null));
                queryBuilder.orderRaw("DATE_OF_SALE ASC");
                task_SalesQuery = queryBuilder.build();
            }
        }
        Query<Sale> query = task_SalesQuery.forCurrentThread();
        query.setParameter(0, taskId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getOrderDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getTaskDao().getAllColumns());
            builder.append(" FROM SALE T");
            builder.append(" LEFT JOIN orders T0 ON T.'ORDER_ID'=T0.'UUID'");
            builder.append(" LEFT JOIN TASK T1 ON T.'TASK_ID'=T1.'UUID'");
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
        offset += daoSession.getOrderDao().getAllColumns().length;

        Task task = loadCurrentOther(daoSession.getTaskDao(), cursor, offset);
         if(task != null) {
            entity.setTask(task);
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
