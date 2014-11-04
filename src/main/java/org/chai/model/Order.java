package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table orders.
 */
public class Order {

    private Long id;
    /** Not-null value. */
    private String uuid;
    private double quantity;
    /** Not-null value. */
    private java.util.Date deliveryDate;
    /** Not-null value. */
    private java.util.Date orderDate;
    /** Not-null value. */
    private String contactTel;
    /** Not-null value. */
    private String contactName;
    private long customerId;
    private long productId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient OrderDao myDao;

    private Customer customer;
    private Long customer__resolvedKey;

    private Product product;
    private Long product__resolvedKey;

    private List<Sale> sales;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long id, String uuid, double quantity, java.util.Date deliveryDate, java.util.Date orderDate, String contactTel, String contactName, long customerId, long productId) {
        this.id = id;
        this.uuid = uuid;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
        this.orderDate = orderDate;
        this.contactTel = contactTel;
        this.contactName = contactName;
        this.customerId = customerId;
        this.productId = productId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /** Not-null value. */
    public java.util.Date getDeliveryDate() {
        return deliveryDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDeliveryDate(java.util.Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /** Not-null value. */
    public java.util.Date getOrderDate() {
        return orderDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setOrderDate(java.util.Date orderDate) {
        this.orderDate = orderDate;
    }

    /** Not-null value. */
    public String getContactTel() {
        return contactTel;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    /** Not-null value. */
    public String getContactName() {
        return contactName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    /** To-one relationship, resolved on first access. */
    public Customer getCustomer() {
        long __key = this.customerId;
        if (customer__resolvedKey == null || !customer__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerDao targetDao = daoSession.getCustomerDao();
            Customer customerNew = targetDao.load(__key);
            synchronized (this) {
                customer = customerNew;
            	customer__resolvedKey = __key;
            }
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new DaoException("To-one property 'customerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.customer = customer;
            customerId = customer.getId();
            customer__resolvedKey = customerId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Product getProduct() {
        long __key = this.productId;
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
            throw new DaoException("To-one property 'productId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.product = product;
            productId = product.getId();
            product__resolvedKey = productId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Sale> getSales() {
        if (sales == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDao targetDao = daoSession.getSaleDao();
            List<Sale> salesNew = targetDao._queryOrder_Sales(id);
            synchronized (this) {
                if(sales == null) {
                    sales = salesNew;
                }
            }
        }
        return sales;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetSales() {
        sales = null;
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
