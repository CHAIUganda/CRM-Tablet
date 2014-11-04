package org.chai.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.chai.model.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER.
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uuid = new Property(1, String.class, "uuid", false, "UUID");
        public final static Property Username = new Property(2, String.class, "username", false, "USERNAME");
        public final static Property Password = new Property(3, String.class, "password", false, "PASSWORD");
        public final static Property Enabled = new Property(4, boolean.class, "enabled", false, "ENABLED");
        public final static Property Accountexpired = new Property(5, boolean.class, "accountexpired", false, "ACCOUNTEXPIRED");
        public final static Property Accountlocked = new Property(6, boolean.class, "accountlocked", false, "ACCOUNTLOCKED");
        public final static Property Passwordexpired = new Property(7, Boolean.class, "passwordexpired", false, "PASSWORDEXPIRED");
    };

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'UUID' TEXT NOT NULL UNIQUE ," + // 1: uuid
                "'USERNAME' TEXT NOT NULL ," + // 2: username
                "'PASSWORD' TEXT NOT NULL ," + // 3: password
                "'ENABLED' INTEGER NOT NULL ," + // 4: enabled
                "'ACCOUNTEXPIRED' INTEGER NOT NULL ," + // 5: accountexpired
                "'ACCOUNTLOCKED' INTEGER NOT NULL ," + // 6: accountlocked
                "'PASSWORDEXPIRED' INTEGER);"); // 7: passwordexpired
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUuid());
        stmt.bindString(3, entity.getUsername());
        stmt.bindString(4, entity.getPassword());
        stmt.bindLong(5, entity.getEnabled() ? 1l: 0l);
        stmt.bindLong(6, entity.getAccountexpired() ? 1l: 0l);
        stmt.bindLong(7, entity.getAccountlocked() ? 1l: 0l);
 
        Boolean passwordexpired = entity.getPasswordexpired();
        if (passwordexpired != null) {
            stmt.bindLong(8, passwordexpired ? 1l: 0l);
        }
    }

    @Override
    protected void attachEntity(User entity) {
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
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // uuid
            cursor.getString(offset + 2), // username
            cursor.getString(offset + 3), // password
            cursor.getShort(offset + 4) != 0, // enabled
            cursor.getShort(offset + 5) != 0, // accountexpired
            cursor.getShort(offset + 6) != 0, // accountlocked
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0 // passwordexpired
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUuid(cursor.getString(offset + 1));
        entity.setUsername(cursor.getString(offset + 2));
        entity.setPassword(cursor.getString(offset + 3));
        entity.setEnabled(cursor.getShort(offset + 4) != 0);
        entity.setAccountexpired(cursor.getShort(offset + 5) != 0);
        entity.setAccountlocked(cursor.getShort(offset + 6) != 0);
        entity.setPasswordexpired(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
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
    
}
