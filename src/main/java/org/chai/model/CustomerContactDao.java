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

import org.chai.model.CustomerContact;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CUSTOMER_CONTACT.
*/
public class CustomerContactDao extends AbstractDao<CustomerContact, Long> {

    public static final String TABLENAME = "CUSTOMER_CONTACT";

    /**
     * Properties of entity CustomerContact.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uuid = new Property(1, String.class, "uuid", false, "UUID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property FirstName = new Property(3, String.class, "firstName", false, "FIRST_NAME");
        public final static Property Surname = new Property(4, String.class, "surname", false, "SURNAME");
        public final static Property Gender = new Property(5, String.class, "gender", false, "GENDER");
        public final static Property NetworkOrAssociation = new Property(6, String.class, "networkOrAssociation", false, "NETWORK_OR_ASSOCIATION");
        public final static Property Role = new Property(7, String.class, "role", false, "ROLE");
        public final static Property Qualification = new Property(8, String.class, "qualification", false, "QUALIFICATION");
        public final static Property DateCreated = new Property(9, java.util.Date.class, "dateCreated", false, "DATE_CREATED");
        public final static Property LastUpdated = new Property(10, java.util.Date.class, "lastUpdated", false, "LAST_UPDATED");
        public final static Property CustomerId = new Property(11, long.class, "customerId", false, "CUSTOMER_ID");
    };

    private DaoSession daoSession;

    private Query<CustomerContact> customer_CustomerContactsQuery;

    public CustomerContactDao(DaoConfig config) {
        super(config);
    }
    
    public CustomerContactDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CUSTOMER_CONTACT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'UUID' TEXT NOT NULL UNIQUE ," + // 1: uuid
                "'TITLE' TEXT," + // 2: title
                "'FIRST_NAME' TEXT," + // 3: firstName
                "'SURNAME' TEXT," + // 4: surname
                "'GENDER' TEXT," + // 5: gender
                "'NETWORK_OR_ASSOCIATION' TEXT," + // 6: networkOrAssociation
                "'ROLE' TEXT," + // 7: role
                "'QUALIFICATION' TEXT," + // 8: qualification
                "'DATE_CREATED' INTEGER," + // 9: dateCreated
                "'LAST_UPDATED' INTEGER," + // 10: lastUpdated
                "'CUSTOMER_ID' INTEGER NOT NULL );"); // 11: customerId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CUSTOMER_CONTACT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CustomerContact entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUuid());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String firstName = entity.getFirstName();
        if (firstName != null) {
            stmt.bindString(4, firstName);
        }
 
        String surname = entity.getSurname();
        if (surname != null) {
            stmt.bindString(5, surname);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(6, gender);
        }
 
        String networkOrAssociation = entity.getNetworkOrAssociation();
        if (networkOrAssociation != null) {
            stmt.bindString(7, networkOrAssociation);
        }
 
        String role = entity.getRole();
        if (role != null) {
            stmt.bindString(8, role);
        }
 
        String qualification = entity.getQualification();
        if (qualification != null) {
            stmt.bindString(9, qualification);
        }
 
        java.util.Date dateCreated = entity.getDateCreated();
        if (dateCreated != null) {
            stmt.bindLong(10, dateCreated.getTime());
        }
 
        java.util.Date lastUpdated = entity.getLastUpdated();
        if (lastUpdated != null) {
            stmt.bindLong(11, lastUpdated.getTime());
        }
        stmt.bindLong(12, entity.getCustomerId());
    }

    @Override
    protected void attachEntity(CustomerContact entity) {
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
    public CustomerContact readEntity(Cursor cursor, int offset) {
        CustomerContact entity = new CustomerContact( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // uuid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // firstName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // surname
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // gender
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // networkOrAssociation
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // role
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // qualification
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)), // dateCreated
            cursor.isNull(offset + 10) ? null : new java.util.Date(cursor.getLong(offset + 10)), // lastUpdated
            cursor.getLong(offset + 11) // customerId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CustomerContact entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUuid(cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFirstName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSurname(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGender(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNetworkOrAssociation(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRole(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setQualification(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDateCreated(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
        entity.setLastUpdated(cursor.isNull(offset + 10) ? null : new java.util.Date(cursor.getLong(offset + 10)));
        entity.setCustomerId(cursor.getLong(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CustomerContact entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CustomerContact entity) {
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
    
    /** Internal query to resolve the "customerContacts" to-many relationship of Customer. */
    public List<CustomerContact> _queryCustomer_CustomerContacts(long customerId) {
        synchronized (this) {
            if (customer_CustomerContactsQuery == null) {
                QueryBuilder<CustomerContact> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CustomerId.eq(null));
                customer_CustomerContactsQuery = queryBuilder.build();
            }
        }
        Query<CustomerContact> query = customer_CustomerContactsQuery.forCurrentThread();
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
            builder.append(" FROM CUSTOMER_CONTACT T");
            builder.append(" LEFT JOIN CUSTOMER T0 ON T.'CUSTOMER_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected CustomerContact loadCurrentDeep(Cursor cursor, boolean lock) {
        CustomerContact entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Customer customer = loadCurrentOther(daoSession.getCustomerDao(), cursor, offset);
         if(customer != null) {
            entity.setCustomer(customer);
        }

        return entity;    
    }

    public CustomerContact loadDeep(Long key) {
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
    public List<CustomerContact> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<CustomerContact> list = new ArrayList<CustomerContact>(count);
        
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
    
    protected List<CustomerContact> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<CustomerContact> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
