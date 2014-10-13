package org.chai.model;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.chai.model.promotion;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PROMOTION.
*/
public class promotionDao extends AbstractDao<promotion, Long> {

    public static final String TABLENAME = "PROMOTION";

    /**
     * Properties of entity promotion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Description = new Property(1, String.class, "description", false, "DESCRIPTION");
        public final static Property StartDate = new Property(2, java.util.Date.class, "startDate", false, "START_DATE");
        public final static Property StopDate = new Property(3, java.util.Date.class, "stopDate", false, "STOP_DATE");
        public final static Property ProductId = new Property(4, long.class, "productId", false, "PRODUCT_ID");
    };

    private DaoSession daoSession;

    private Query<promotion> product_PromotionsQuery;

    public promotionDao(DaoConfig config) {
        super(config);
    }
    
    public promotionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PROMOTION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DESCRIPTION' TEXT NOT NULL ," + // 1: description
                "'START_DATE' INTEGER NOT NULL ," + // 2: startDate
                "'STOP_DATE' INTEGER NOT NULL ," + // 3: stopDate
                "'PRODUCT_ID' INTEGER NOT NULL );"); // 4: productId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PROMOTION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, promotion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getDescription());
        stmt.bindLong(3, entity.getStartDate().getTime());
        stmt.bindLong(4, entity.getStopDate().getTime());
        stmt.bindLong(5, entity.getProductId());
    }

    @Override
    protected void attachEntity(promotion entity) {
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
    public promotion readEntity(Cursor cursor, int offset) {
        promotion entity = new promotion( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // description
            new java.util.Date(cursor.getLong(offset + 2)), // startDate
            new java.util.Date(cursor.getLong(offset + 3)), // stopDate
            cursor.getLong(offset + 4) // productId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, promotion entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescription(cursor.getString(offset + 1));
        entity.setStartDate(new java.util.Date(cursor.getLong(offset + 2)));
        entity.setStopDate(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setProductId(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(promotion entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(promotion entity) {
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
    
    /** Internal query to resolve the "promotions" to-many relationship of product. */
    public List<promotion> _queryProduct_Promotions(long productId) {
        synchronized (this) {
            if (product_PromotionsQuery == null) {
                QueryBuilder<promotion> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ProductId.eq(null));
                product_PromotionsQuery = queryBuilder.build();
            }
        }
        Query<promotion> query = product_PromotionsQuery.forCurrentThread();
        query.setParameter(0, productId);
        return query.list();
    }

}
