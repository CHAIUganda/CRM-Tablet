package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.fasterxml.jackson.annotation.JsonIgnore;
// KEEP INCLUDES END
/**
 * Entity mapped to table orders.
 */
public class Order {

    /** Not-null value. */
    private String uuid;
    /** Not-null value. */
    private java.util.Date deliveryDate;
    /** Not-null value. */
    private java.util.Date orderDate;
    /** Not-null value. */
    private String customerId;
    private List<OrderData> orderDatas;

    // KEEP FIELDS - put your custom fields here
    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private transient OrderDao myDao;

    @JsonIgnore
    private Customer customer;
    @JsonIgnore
    private String customer__resolvedKey;

    @JsonIgnore
    private List<Sale> sales;
    // KEEP FIELDS END

    public Order() {
    }

    public Order(String uuid) {
        this.uuid = uuid;
    }

    public Order(String uuid, java.util.Date deliveryDate, java.util.Date orderDate, String customerId) {
        this.uuid = uuid;
        this.deliveryDate = deliveryDate;
        this.orderDate = orderDate;
        this.customerId = customerId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public String getCustomerId() {
        return customerId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /** To-one relationship, resolved on first access. */
    public Customer getCustomer() {
        String __key = this.customerId;
        if (customer__resolvedKey == null || customer__resolvedKey != __key) {
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
            customerId = customer.getUuid();
            customer__resolvedKey = customerId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Sale> getSales() {
        if (sales == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDao targetDao = daoSession.getSaleDao();
            List<Sale> salesNew = targetDao._queryOrder_Sales(uuid);
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

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<OrderData> getOrderDatas() {
        if (orderDatas == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderDataDao targetDao = daoSession.getOrderDataDao();
            List<OrderData> orderDatasNew = targetDao._queryOrder_OrderDatas(uuid);
            synchronized (this) {
                if(orderDatas == null) {
                    orderDatas = orderDatasNew;
                }
            }
        }
        return orderDatas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetOrderDatas() {
        orderDatas = null;
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
