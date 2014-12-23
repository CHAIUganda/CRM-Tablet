package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table SALE.
 */
public class Sale {

    private Long id;
    /** Not-null value. */
    private String uuid;
    /** Not-null value. */
    private java.util.Date dateOfSale;
    private Boolean doYouStockOrsZinc;
    private Integer howManyZincInStock;
    private Integer howmanyOrsInStock;
    private String ifNoWhy;
    private String pointOfsaleMaterial;
    private String recommendationNextStep;
    private String recommendationLevel;
    private String governmentApproval;
    private long orderId;
    private int quantity;
    private int salePrice;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient SaleDao myDao;

    private Order order;
    private Long order__resolvedKey;

    private List<SaleData> salesDatas;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Sale() {
    }

    public Sale(Long id) {
        this.id = id;
    }

    public Sale(Long id, String uuid, java.util.Date dateOfSale, Boolean doYouStockOrsZinc, Integer howManyZincInStock, Integer howmanyOrsInStock, String ifNoWhy, String pointOfsaleMaterial, String recommendationNextStep, String recommendationLevel, String governmentApproval, long orderId, int quantity, int salePrice) {
        this.id = id;
        this.uuid = uuid;
        this.dateOfSale = dateOfSale;
        this.doYouStockOrsZinc = doYouStockOrsZinc;
        this.howManyZincInStock = howManyZincInStock;
        this.howmanyOrsInStock = howmanyOrsInStock;
        this.ifNoWhy = ifNoWhy;
        this.pointOfsaleMaterial = pointOfsaleMaterial;
        this.recommendationNextStep = recommendationNextStep;
        this.recommendationLevel = recommendationLevel;
        this.governmentApproval = governmentApproval;
        this.orderId = orderId;
        this.quantity = quantity;
        this.salePrice = salePrice;
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
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /** Not-null value. */
    public java.util.Date getDateOfSale() {
        return dateOfSale;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDateOfSale(java.util.Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public Boolean getDoYouStockOrsZinc() {
        return doYouStockOrsZinc;
    }

    public void setDoYouStockOrsZinc(Boolean doYouStockOrsZinc) {
        this.doYouStockOrsZinc = doYouStockOrsZinc;
    }

    public Integer getHowManyZincInStock() {
        return howManyZincInStock;
    }

    public void setHowManyZincInStock(Integer howManyZincInStock) {
        this.howManyZincInStock = howManyZincInStock;
    }

    public Integer getHowmanyOrsInStock() {
        return howmanyOrsInStock;
    }

    public void setHowmanyOrsInStock(Integer howmanyOrsInStock) {
        this.howmanyOrsInStock = howmanyOrsInStock;
    }

    public String getIfNoWhy() {
        return ifNoWhy;
    }

    public void setIfNoWhy(String ifNoWhy) {
        this.ifNoWhy = ifNoWhy;
    }

    public String getPointOfsaleMaterial() {
        return pointOfsaleMaterial;
    }

    public void setPointOfsaleMaterial(String pointOfsaleMaterial) {
        this.pointOfsaleMaterial = pointOfsaleMaterial;
    }

    public String getRecommendationNextStep() {
        return recommendationNextStep;
    }

    public void setRecommendationNextStep(String recommendationNextStep) {
        this.recommendationNextStep = recommendationNextStep;
    }

    public String getRecommendationLevel() {
        return recommendationLevel;
    }

    public void setRecommendationLevel(String recommendationLevel) {
        this.recommendationLevel = recommendationLevel;
    }

    public String getGovernmentApproval() {
        return governmentApproval;
    }

    public void setGovernmentApproval(String governmentApproval) {
        this.governmentApproval = governmentApproval;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    /** To-one relationship, resolved on first access. */
    public Order getOrder() {
        long __key = this.orderId;
        if (order__resolvedKey == null || !order__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderDao targetDao = daoSession.getOrderDao();
            Order orderNew = targetDao.load(__key);
            synchronized (this) {
                order = orderNew;
            	order__resolvedKey = __key;
            }
        }
        return order;
    }

    public void setOrder(Order order) {
        if (order == null) {
            throw new DaoException("To-one property 'orderId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.order = order;
            orderId = order.getId();
            order__resolvedKey = orderId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<SaleData> getSalesDatas() {
        if (salesDatas == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDataDao targetDao = daoSession.getSaleDataDao();
            List<SaleData> salesDatasNew = targetDao._querySale_SalesDatas(id);
            synchronized (this) {
                if(salesDatas == null) {
                    salesDatas = salesDatasNew;
                }
            }
        }
        return salesDatas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetSalesDatas() {
        salesDatas = null;
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
