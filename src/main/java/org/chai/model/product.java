package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table PRODUCT.
 */
public class product {

    private Long id;
    /** Not-null value. */
    private String sysid;
    /** Not-null value. */
    private String productGroup;
    /** Not-null value. */
    private String productName;
    /** Not-null value. */
    private String brandName;
    /** Not-null value. */
    private String formulation;
    /** Not-null value. */
    private String unitOfMeasure;
    private double unitPrice;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient productDao myDao;

    private List<order> orders;
    private List<promotion> promotions;
    private List<sale> sales;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public product() {
    }

    public product(Long id) {
        this.id = id;
    }

    public product(Long id, String sysid, String productGroup, String productName, String brandName, String formulation, String unitOfMeasure, double unitPrice) {
        this.id = id;
        this.sysid = sysid;
        this.productGroup = productGroup;
        this.productName = productName;
        this.brandName = brandName;
        this.formulation = formulation;
        this.unitOfMeasure = unitOfMeasure;
        this.unitPrice = unitPrice;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductDao() : null;
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

    /** Not-null value. */
    public String getProductGroup() {
        return productGroup;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    /** Not-null value. */
    public String getProductName() {
        return productName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /** Not-null value. */
    public String getBrandName() {
        return brandName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /** Not-null value. */
    public String getFormulation() {
        return formulation;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    /** Not-null value. */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<order> getOrders() {
        if (orders == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            orderDao targetDao = daoSession.getOrderDao();
            List<order> ordersNew = targetDao._queryProduct_Orders(id);
            synchronized (this) {
                if(orders == null) {
                    orders = ordersNew;
                }
            }
        }
        return orders;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetOrders() {
        orders = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<promotion> getPromotions() {
        if (promotions == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            promotionDao targetDao = daoSession.getPromotionDao();
            List<promotion> promotionsNew = targetDao._queryProduct_Promotions(id);
            synchronized (this) {
                if(promotions == null) {
                    promotions = promotionsNew;
                }
            }
        }
        return promotions;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetPromotions() {
        promotions = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<sale> getSales() {
        if (sales == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            saleDao targetDao = daoSession.getSaleDao();
            List<sale> salesNew = targetDao._queryProduct_Sales(id);
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
