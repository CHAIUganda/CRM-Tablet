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

import org.chai.model.sale;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SALE.
*/
public class saleDao extends AbstractDao<sale, Long> {

    public static final String TABLENAME = "SALE";

    /**
     * Properties of entity sale.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Sysid = new Property(1, String.class, "sysid", false, "SYSID");
        public final static Property Quantity = new Property(2, int.class, "quantity", false, "QUANTITY");
        public final static Property SalePrice = new Property(3, int.class, "salePrice", false, "SALE_PRICE");
        public final static Property DateOfSale = new Property(4, java.util.Date.class, "dateOfSale", false, "DATE_OF_SALE");
        public final static Property OrderId = new Property(5, long.class, "orderId", false, "ORDER_ID");
        public final static Property ProductId = new Property(6, long.class, "productId", false, "PRODUCT_ID");
    };

    private DaoSession daoSession;

    private Query<sale> order_SalesQuery;
    private Query<sale> product_SalesQuery;

    public saleDao(DaoConfig config) {
        super(config);
    }
    
    public saleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SALE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SYSID' TEXT NOT NULL UNIQUE ," + // 1: sysid
                "'QUANTITY' INTEGER NOT NULL ," + // 2: quantity
                "'SALE_PRICE' INTEGER NOT NULL ," + // 3: salePrice
                "'DATE_OF_SALE' INTEGER NOT NULL ," + // 4: dateOfSale
                "'ORDER_ID' INTEGER NOT NULL ," + // 5: orderId
                "'PRODUCT_ID' INTEGER NOT NULL );"); // 6: productId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SALE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, sale entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getSysid());
        stmt.bindLong(3, entity.getQuantity());
        stmt.bindLong(4, entity.getSalePrice());
        stmt.bindLong(5, entity.getDateOfSale().getTime());
        stmt.bindLong(6, entity.getOrderId());
        stmt.bindLong(7, entity.getProductId());
    }

    @Override
    protected void attachEntity(sale entity) {
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
    public sale readEntity(Cursor cursor, int offset) {
        sale entity = new sale( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // sysid
            cursor.getInt(offset + 2), // quantity
            cursor.getInt(offset + 3), // salePrice
            new java.util.Date(cursor.getLong(offset + 4)), // dateOfSale
            cursor.getLong(offset + 5), // orderId
            cursor.getLong(offset + 6) // productId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, sale entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSysid(cursor.getString(offset + 1));
        entity.setQuantity(cursor.getInt(offset + 2));
        entity.setSalePrice(cursor.getInt(offset + 3));
        entity.setDateOfSale(new java.util.Date(cursor.getLong(offset + 4)));
        entity.setOrderId(cursor.getLong(offset + 5));
        entity.setProductId(cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(sale entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(sale entity) {
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
    
    /** Internal query to resolve the "sales" to-many relationship of order. */
    public List<sale> _queryOrder_Sales(long orderId) {
        synchronized (this) {
            if (order_SalesQuery == null) {
                QueryBuilder<sale> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.OrderId.eq(null));
                queryBuilder.orderRaw("DATE_OF_SALE ASC");
                order_SalesQuery = queryBuilder.build();
            }
        }
        Query<sale> query = order_SalesQuery.forCurrentThread();
        query.setParameter(0, orderId);
        return query.list();
    }

    /** Internal query to resolve the "sales" to-many relationship of product. */
    public List<sale> _queryProduct_Sales(long productId) {
        synchronized (this) {
            if (product_SalesQuery == null) {
                QueryBuilder<sale> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ProductId.eq(null));
                queryBuilder.orderRaw("DATE_OF_SALE ASC");
                product_SalesQuery = queryBuilder.build();
            }
        }
        Query<sale> query = product_SalesQuery.forCurrentThread();
        query.setParameter(0, productId);
        return query.list();
    }

}
