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

import org.chai.model.Task;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TASK.
*/
public class TaskDao extends AbstractDao<Task, String> {

    public static final String TABLENAME = "TASK";

    /**
     * Properties of entity Task.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property Description = new Property(1, String.class, "description", false, "DESCRIPTION");
        public final static Property Status = new Property(2, String.class, "status", false, "STATUS");
        public final static Property Priority = new Property(3, String.class, "priority", false, "PRIORITY");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property DueDate = new Property(5, java.util.Date.class, "dueDate", false, "DUE_DATE");
        public final static Property CompletionDate = new Property(6, java.util.Date.class, "completionDate", false, "COMPLETION_DATE");
        public final static Property DateScheduled = new Property(7, java.util.Date.class, "dateScheduled", false, "DATE_SCHEDULED");
        public final static Property IsAdhock = new Property(8, Boolean.class, "isAdhock", false, "IS_ADHOCK");
        public final static Property CustomerId = new Property(9, String.class, "customerId", false, "CUSTOMER_ID");
        public final static Property IsDirty = new Property(10, Boolean.class, "isDirty", false, "IS_DIRTY");
        public final static Property SyncronisationStatus = new Property(11, Integer.class, "syncronisationStatus", false, "SYNCRONISATION_STATUS");
        public final static Property SyncronisationMessage = new Property(12, String.class, "syncronisationMessage", false, "SYNCRONISATION_MESSAGE");
        public final static Property DateCreated = new Property(13, java.util.Date.class, "dateCreated", false, "DATE_CREATED");
        public final static Property LastUpdated = new Property(14, java.util.Date.class, "lastUpdated", false, "LAST_UPDATED");
    };

    private DaoSession daoSession;

    private Query<Task> customer_TasksQuery;

    public TaskDao(DaoConfig config) {
        super(config);
    }
    
    public TaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TASK' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'DESCRIPTION' TEXT," + // 1: description
                "'STATUS' TEXT," + // 2: status
                "'PRIORITY' TEXT," + // 3: priority
                "'TYPE' TEXT," + // 4: type
                "'DUE_DATE' INTEGER," + // 5: dueDate
                "'COMPLETION_DATE' INTEGER," + // 6: completionDate
                "'DATE_SCHEDULED' INTEGER," + // 7: dateScheduled
                "'IS_ADHOCK' INTEGER," + // 8: isAdhock
                "'CUSTOMER_ID' TEXT NOT NULL ," + // 9: customerId
                "'IS_DIRTY' INTEGER," + // 10: isDirty
                "'SYNCRONISATION_STATUS' INTEGER," + // 11: syncronisationStatus
                "'SYNCRONISATION_MESSAGE' TEXT," + // 12: syncronisationMessage
                "'DATE_CREATED' INTEGER," + // 13: dateCreated
                "'LAST_UPDATED' INTEGER);"); // 14: lastUpdated
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TASK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Task entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(2, description);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(3, status);
        }
 
        String priority = entity.getPriority();
        if (priority != null) {
            stmt.bindString(4, priority);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
 
        java.util.Date dueDate = entity.getDueDate();
        if (dueDate != null) {
            stmt.bindLong(6, dueDate.getTime());
        }
 
        java.util.Date completionDate = entity.getCompletionDate();
        if (completionDate != null) {
            stmt.bindLong(7, completionDate.getTime());
        }
 
        java.util.Date dateScheduled = entity.getDateScheduled();
        if (dateScheduled != null) {
            stmt.bindLong(8, dateScheduled.getTime());
        }
 
        Boolean isAdhock = entity.getIsAdhock();
        if (isAdhock != null) {
            stmt.bindLong(9, isAdhock ? 1l: 0l);
        }
        stmt.bindString(10, entity.getCustomerId());
 
        Boolean isDirty = entity.getIsDirty();
        if (isDirty != null) {
            stmt.bindLong(11, isDirty ? 1l: 0l);
        }
 
        Integer syncronisationStatus = entity.getSyncronisationStatus();
        if (syncronisationStatus != null) {
            stmt.bindLong(12, syncronisationStatus);
        }
 
        String syncronisationMessage = entity.getSyncronisationMessage();
        if (syncronisationMessage != null) {
            stmt.bindString(13, syncronisationMessage);
        }
 
        java.util.Date dateCreated = entity.getDateCreated();
        if (dateCreated != null) {
            stmt.bindLong(14, dateCreated.getTime());
        }
 
        java.util.Date lastUpdated = entity.getLastUpdated();
        if (lastUpdated != null) {
            stmt.bindLong(15, lastUpdated.getTime());
        }
    }

    @Override
    protected void attachEntity(Task entity) {
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
    public Task readEntity(Cursor cursor, int offset) {
        Task entity = new Task( //
            cursor.getString(offset + 0), // uuid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // description
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // status
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // priority
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // type
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // dueDate
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // completionDate
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // dateScheduled
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0, // isAdhock
            cursor.getString(offset + 9), // customerId
            cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0, // isDirty
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // syncronisationStatus
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // syncronisationMessage
            cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)), // dateCreated
            cursor.isNull(offset + 14) ? null : new java.util.Date(cursor.getLong(offset + 14)) // lastUpdated
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Task entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setDescription(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStatus(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPriority(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDueDate(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setCompletionDate(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setDateScheduled(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setIsAdhock(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
        entity.setCustomerId(cursor.getString(offset + 9));
        entity.setIsDirty(cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0);
        entity.setSyncronisationStatus(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setSyncronisationMessage(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setDateCreated(cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)));
        entity.setLastUpdated(cursor.isNull(offset + 14) ? null : new java.util.Date(cursor.getLong(offset + 14)));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Task entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Task entity) {
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
    
    /** Internal query to resolve the "tasks" to-many relationship of Customer. */
    public List<Task> _queryCustomer_Tasks(String customerId) {
        synchronized (this) {
            if (customer_TasksQuery == null) {
                QueryBuilder<Task> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CustomerId.eq(null));
                customer_TasksQuery = queryBuilder.build();
            }
        }
        Query<Task> query = customer_TasksQuery.forCurrentThread();
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
            builder.append(" FROM TASK T");
            builder.append(" LEFT JOIN CUSTOMER T0 ON T.'CUSTOMER_ID'=T0.'UUID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Task loadCurrentDeep(Cursor cursor, boolean lock) {
        Task entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Customer customer = loadCurrentOther(daoSession.getCustomerDao(), cursor, offset);
         if(customer != null) {
            entity.setCustomer(customer);
        }

        return entity;    
    }

    public Task loadDeep(Long key) {
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
    public List<Task> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Task> list = new ArrayList<Task>(count);
        
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
    
    protected List<Task> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Task> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
