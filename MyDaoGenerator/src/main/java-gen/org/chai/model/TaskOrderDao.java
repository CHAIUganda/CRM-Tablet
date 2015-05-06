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

import org.chai.model.TaskOrder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TASK_ORDER.
*/
public class TaskOrderDao extends AbstractDao<TaskOrder, Long> {

    public static final String TABLENAME = "TASK_ORDER";

    /**
     * Properties of entity TaskOrder.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Quantity = new Property(1, String.class, "quantity", false, "QUANTITY");
        public final static Property UnitPrice = new Property(2, String.class, "unitPrice", false, "UNIT_PRICE");
        public final static Property TaskId = new Property(3, String.class, "taskId", false, "TASK_ID");
        public final static Property ProductId = new Property(4, String.class, "productId", false, "PRODUCT_ID");
        public final static Property IsDirty = new Property(5, Boolean.class, "isDirty", false, "IS_DIRTY");
        public final static Property SyncronisationStatus = new Property(6, Integer.class, "syncronisationStatus", false, "SYNCRONISATION_STATUS");
        public final static Property SyncronisationMessage = new Property(7, String.class, "syncronisationMessage", false, "SYNCRONISATION_MESSAGE");
        public final static Property DateCreated = new Property(8, java.util.Date.class, "dateCreated", false, "DATE_CREATED");
        public final static Property LastUpdated = new Property(9, java.util.Date.class, "lastUpdated", false, "LAST_UPDATED");
    };

    private DaoSession daoSession;

    private Query<TaskOrder> task_LineItemsQuery;
    private Query<TaskOrder> product_TaskOrderproductsQuery;

    public TaskOrderDao(DaoConfig config) {
        super(config);
    }
    
    public TaskOrderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TASK_ORDER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'QUANTITY' TEXT," + // 1: quantity
                "'UNIT_PRICE' TEXT," + // 2: unitPrice
                "'TASK_ID' TEXT NOT NULL ," + // 3: taskId
                "'PRODUCT_ID' TEXT NOT NULL ," + // 4: productId
                "'IS_DIRTY' INTEGER," + // 5: isDirty
                "'SYNCRONISATION_STATUS' INTEGER," + // 6: syncronisationStatus
                "'SYNCRONISATION_MESSAGE' TEXT," + // 7: syncronisationMessage
                "'DATE_CREATED' INTEGER," + // 8: dateCreated
                "'LAST_UPDATED' INTEGER);"); // 9: lastUpdated
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TASK_ORDER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TaskOrder entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String quantity = entity.getQuantity();
        if (quantity != null) {
            stmt.bindString(2, quantity);
        }
 
        String unitPrice = entity.getUnitPrice();
        if (unitPrice != null) {
            stmt.bindString(3, unitPrice);
        }
        stmt.bindString(4, entity.getTaskId());
        stmt.bindString(5, entity.getProductId());
 
        Boolean isDirty = entity.getIsDirty();
        if (isDirty != null) {
            stmt.bindLong(6, isDirty ? 1l: 0l);
        }
 
        Integer syncronisationStatus = entity.getSyncronisationStatus();
        if (syncronisationStatus != null) {
            stmt.bindLong(7, syncronisationStatus);
        }
 
        String syncronisationMessage = entity.getSyncronisationMessage();
        if (syncronisationMessage != null) {
            stmt.bindString(8, syncronisationMessage);
        }
 
        java.util.Date dateCreated = entity.getDateCreated();
        if (dateCreated != null) {
            stmt.bindLong(9, dateCreated.getTime());
        }
 
        java.util.Date lastUpdated = entity.getLastUpdated();
        if (lastUpdated != null) {
            stmt.bindLong(10, lastUpdated.getTime());
        }
    }

    @Override
    protected void attachEntity(TaskOrder entity) {
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
    public TaskOrder readEntity(Cursor cursor, int offset) {
        TaskOrder entity = new TaskOrder( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // quantity
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // unitPrice
            cursor.getString(offset + 3), // taskId
            cursor.getString(offset + 4), // productId
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // isDirty
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // syncronisationStatus
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // syncronisationMessage
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)), // dateCreated
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)) // lastUpdated
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TaskOrder entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setQuantity(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUnitPrice(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTaskId(cursor.getString(offset + 3));
        entity.setProductId(cursor.getString(offset + 4));
        entity.setIsDirty(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setSyncronisationStatus(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setSyncronisationMessage(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDateCreated(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
        entity.setLastUpdated(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TaskOrder entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TaskOrder entity) {
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
    
    /** Internal query to resolve the "lineItems" to-many relationship of Task. */
    public List<TaskOrder> _queryTask_LineItems(String taskId) {
        synchronized (this) {
            if (task_LineItemsQuery == null) {
                QueryBuilder<TaskOrder> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TaskId.eq(null));
                task_LineItemsQuery = queryBuilder.build();
            }
        }
        Query<TaskOrder> query = task_LineItemsQuery.forCurrentThread();
        query.setParameter(0, taskId);
        return query.list();
    }

    /** Internal query to resolve the "taskOrderproducts" to-many relationship of Product. */
    public List<TaskOrder> _queryProduct_TaskOrderproducts(String productId) {
        synchronized (this) {
            if (product_TaskOrderproductsQuery == null) {
                QueryBuilder<TaskOrder> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ProductId.eq(null));
                product_TaskOrderproductsQuery = queryBuilder.build();
            }
        }
        Query<TaskOrder> query = product_TaskOrderproductsQuery.forCurrentThread();
        query.setParameter(0, productId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getTaskDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getProductDao().getAllColumns());
            builder.append(" FROM TASK_ORDER T");
            builder.append(" LEFT JOIN TASK T0 ON T.'TASK_ID'=T0.'UUID'");
            builder.append(" LEFT JOIN PRODUCT T1 ON T.'PRODUCT_ID'=T1.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected TaskOrder loadCurrentDeep(Cursor cursor, boolean lock) {
        TaskOrder entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Task task = loadCurrentOther(daoSession.getTaskDao(), cursor, offset);
         if(task != null) {
            entity.setTask(task);
        }
        offset += daoSession.getTaskDao().getAllColumns().length;

        Product product = loadCurrentOther(daoSession.getProductDao(), cursor, offset);
         if(product != null) {
            entity.setProduct(product);
        }

        return entity;    
    }

    public TaskOrder loadDeep(Long key) {
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
    public List<TaskOrder> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<TaskOrder> list = new ArrayList<TaskOrder>(count);
        
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
    
    protected List<TaskOrder> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<TaskOrder> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
