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

import org.chai.model.Product;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PRODUCT.
*/
public class ProductDao extends AbstractDao<Product, String> {

    public static final String TABLENAME = "PRODUCT";

    /**
     * Properties of entity Product.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Uuid = new Property(0, String.class, "uuid", true, "UUID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property UnitOfMeasure = new Property(2, String.class, "unitOfMeasure", false, "UNIT_OF_MEASURE");
        public final static Property Formulation = new Property(3, String.class, "formulation", false, "FORMULATION");
        public final static Property UnitPrice = new Property(4, String.class, "unitPrice", false, "UNIT_PRICE");
        public final static Property GroupName = new Property(5, String.class, "groupName", false, "GROUP_NAME");
        public final static Property GroupId = new Property(6, String.class, "groupId", false, "GROUP_ID");
    };

    private DaoSession daoSession;

    private Query<Product> productGroup_ProductsQuery;

    public ProductDao(DaoConfig config) {
        super(config);
    }
    
    public ProductDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PRODUCT' (" + //
                "'UUID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: uuid
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'UNIT_OF_MEASURE' TEXT," + // 2: unitOfMeasure
                "'FORMULATION' TEXT," + // 3: formulation
                "'UNIT_PRICE' TEXT," + // 4: unitPrice
                "'GROUP_NAME' TEXT," + // 5: groupName
                "'GROUP_ID' TEXT NOT NULL );"); // 6: groupId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PRODUCT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Product entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getUuid());
        stmt.bindString(2, entity.getName());
 
        String unitOfMeasure = entity.getUnitOfMeasure();
        if (unitOfMeasure != null) {
            stmt.bindString(3, unitOfMeasure);
        }
 
        String formulation = entity.getFormulation();
        if (formulation != null) {
            stmt.bindString(4, formulation);
        }
 
        String unitPrice = entity.getUnitPrice();
        if (unitPrice != null) {
            stmt.bindString(5, unitPrice);
        }
 
        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(6, groupName);
        }
        stmt.bindString(7, entity.getGroupId());
    }

    @Override
    protected void attachEntity(Product entity) {
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
    public Product readEntity(Cursor cursor, int offset) {
        Product entity = new Product( //
            cursor.getString(offset + 0), // uuid
            cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // unitOfMeasure
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // formulation
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // unitPrice
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // groupName
            cursor.getString(offset + 6) // groupId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Product entity, int offset) {
        entity.setUuid(cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setUnitOfMeasure(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFormulation(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUnitPrice(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setGroupName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGroupId(cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Product entity, long rowId) {
        return entity.getUuid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Product entity) {
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
    
    /** Internal query to resolve the "products" to-many relationship of ProductGroup. */
    public List<Product> _queryProductGroup_Products(String groupId) {
        synchronized (this) {
            if (productGroup_ProductsQuery == null) {
                QueryBuilder<Product> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.GroupId.eq(null));
                productGroup_ProductsQuery = queryBuilder.build();
            }
        }
        Query<Product> query = productGroup_ProductsQuery.forCurrentThread();
        query.setParameter(0, groupId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getProductGroupDao().getAllColumns());
            builder.append(" FROM PRODUCT T");
            builder.append(" LEFT JOIN PRODUCT_GROUP T0 ON T.'GROUP_ID'=T0.'ID'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Product loadCurrentDeep(Cursor cursor, boolean lock) {
        Product entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        ProductGroup productGroup = loadCurrentOther(daoSession.getProductGroupDao(), cursor, offset);
         if(productGroup != null) {
            entity.setProductGroup(productGroup);
        }

        return entity;    
    }

    public Product loadDeep(Long key) {
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
    public List<Product> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Product> list = new ArrayList<Product>(count);
        
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
    
    protected List<Product> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Product> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
