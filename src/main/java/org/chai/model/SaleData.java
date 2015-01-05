package org.chai.model;

import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table SALE_DATA.
 */
public class SaleData {

    private Long id;
    /** Not-null value. */
    private String uuid;
    private int quantity;
    private int price;
    private String productId;
    private long productRefId;



    // KEEP FIELDS - put your custom fields here

    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private long saleId;
    @JsonIgnore
    private transient SaleDataDao myDao;

    @JsonIgnore
    private Sale sale;
    @JsonIgnore
    private Long sale__resolvedKey;

    @JsonIgnore
    private Product product;
    @JsonIgnore
    private Long product__resolvedKey;
    // KEEP FIELDS END

    public SaleData() {
    }

    public SaleData(Long id) {
        this.id = id;
    }

    public SaleData(Long id, String uuid, int quantity, int price, String productId, long saleId, long productRefId) {
        this.id = id;
        this.uuid = uuid;
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.saleId = saleId;
        this.productRefId = productRefId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSaleDataDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public long getProductRefId() {
        return productRefId;
    }

    public void setProductRefId(long productRefId) {
        this.productRefId = productRefId;
    }

    /** To-one relationship, resolved on first access. */
    public Sale getSale() {
        long __key = this.saleId;
        if (sale__resolvedKey == null || !sale__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDao targetDao = daoSession.getSaleDao();
            Sale saleNew = targetDao.load(__key);
            synchronized (this) {
                sale = saleNew;
            	sale__resolvedKey = __key;
            }
        }
        return sale;
    }

    public void setSale(Sale sale) {
        if (sale == null) {
            throw new DaoException("To-one property 'saleId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.sale = sale;
            saleId = sale.getId();
            sale__resolvedKey = saleId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Product getProduct() {
        long __key = this.productRefId;
        if (product__resolvedKey == null || !product__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
            	product__resolvedKey = __key;
            }
        }
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new DaoException("To-one property 'productRefId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.product = product;
            productRefId = product.getId();
            product__resolvedKey = productRefId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
