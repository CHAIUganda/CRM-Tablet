package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table ADHOCK_SALE.
 */
public class AdhockSale {

    /** Not-null value. */
    private String uuid;
    /** Not-null value. */
    private java.util.Date dateOfSale;
    private Boolean doYouStockOrsZinc;
    private String ifNoWhy;
    private String pointOfsaleMaterial;
    private String recommendationNextStep;
    private String governmentApproval;
    private Double latitude;
    private Double longitude;
    /** Not-null value. */
    private String customerId;
    private List<SaleData> adhockSalesDatas;
    private List<StokeData> adhockStockDatas;

    // KEEP FIELDS - put your custom fields here

    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private transient AdhockSaleDao myDao;

    @JsonIgnore
    private Customer customer;
    @JsonIgnore
    private String customer__resolvedKey;
    // KEEP FIELDS END

    public AdhockSale() {
    }

    public AdhockSale(String uuid) {
        this.uuid = uuid;
    }

    public AdhockSale(String uuid, java.util.Date dateOfSale, Boolean doYouStockOrsZinc, String ifNoWhy, String pointOfsaleMaterial, String recommendationNextStep, String governmentApproval, Double latitude, Double longitude, String customerId) {
        this.uuid = uuid;
        this.dateOfSale = dateOfSale;
        this.doYouStockOrsZinc = doYouStockOrsZinc;
        this.ifNoWhy = ifNoWhy;
        this.pointOfsaleMaterial = pointOfsaleMaterial;
        this.recommendationNextStep = recommendationNextStep;
        this.governmentApproval = governmentApproval;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerId = customerId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAdhockSaleDao() : null;
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

    public String getGovernmentApproval() {
        return governmentApproval;
    }

    public void setGovernmentApproval(String governmentApproval) {
        this.governmentApproval = governmentApproval;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
    public List<SaleData> getAdhockSalesDatas() {
        if (adhockSalesDatas == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDataDao targetDao = daoSession.getSaleDataDao();
            List<SaleData> adhockSalesDatasNew = targetDao._queryAdhockSale_AdhockSalesDatas(uuid);
            synchronized (this) {
                if(adhockSalesDatas == null) {
                    adhockSalesDatas = adhockSalesDatasNew;
                }
            }
        }
        return adhockSalesDatas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAdhockSalesDatas() {
        adhockSalesDatas = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<StokeData> getAdhockStockDatas() {
        if (adhockStockDatas == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StokeDataDao targetDao = daoSession.getStokeDataDao();
            List<StokeData> adhockStockDatasNew = targetDao._queryAdhockSale_AdhockStockDatas(uuid);
            synchronized (this) {
                if(adhockStockDatas == null) {
                    adhockStockDatas = adhockStockDatasNew;
                }
            }
        }
        return adhockStockDatas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAdhockStockDatas() {
        adhockStockDatas = null;
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
