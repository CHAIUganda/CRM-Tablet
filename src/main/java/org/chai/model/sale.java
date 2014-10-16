package org.chai.model;

import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table SALE.
 */
public class sale {

    private Long id;
    /** Not-null value. */
    private String sysid;
    private int quantity;
    private int salePrice;
    /** Not-null value. */
    private java.util.Date dateOfSale;
    private long orderId;
    private long productId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient saleDao myDao;

    private order order;
    private Long order__resolvedKey;

    private product product;
    private Long product__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public sale() {
    }

    public sale(Long id) {
        this.id = id;
    }

    public sale(Long id, String sysid, int quantity, int salePrice, java.util.Date dateOfSale, long orderId, long productId) {
        this.id = id;
        this.sysid = sysid;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.dateOfSale = dateOfSale;
        this.orderId = orderId;
        this.productId = productId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSaleDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getSysid() {
        return sysid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    /** Not-null value. */
    public java.util.Date getDateOfSale() {
        return dateOfSale;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDateOfSale(java.util.Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    /** To-one relationship, resolved on first access. */
    public order getOrder() {
        long __key = this.orderId;
        if (order__resolvedKey == null || !order__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            orderDao targetDao = daoSession.getOrderDao();
            order orderNew = targetDao.load(__key);
            synchronized (this) {
                order = orderNew;
            	order__resolvedKey = __key;
            }
        }
        return order;
    }

    public void setOrder(order order) {
        if (order == null) {
            throw new DaoException("To-one property 'orderId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.order = order;
            orderId = order.getId();
            order__resolvedKey = orderId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public product getProduct() {
        long __key = this.productId;
        if (product__resolvedKey == null || !product__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            productDao targetDao = daoSession.getProductDao();
            product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
            	product__resolvedKey = __key;
            }
        }
        return product;
    }

    public void setProduct(product product) {
        if (product == null) {
            throw new DaoException("To-one property 'productId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.product = product;
            productId = product.getId();
            product__resolvedKey = productId;
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
