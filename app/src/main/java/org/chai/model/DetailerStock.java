package org.chai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table DETAILER_STOCK.
 */
public class DetailerStock implements BaseEntity {

    /** Not-null value. */
    private String uuid;
    /** Not-null value. */
    private String brand;
    /** Not-null value. */
    private String category;
    private double stockLevel;
    private Double buyingPrice;
    private Double sellingPrice;
    private String packSize;
    /** Not-null value. */
    private String detailerId;
    /** Not-null value. */
    private String malariadetailId;
    private String malariaDetail__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    @JsonIgnore
    private Boolean isDirty;
    @JsonIgnore
    private Integer syncronisationStatus;
    @JsonIgnore
    private String syncronisationMessage;
    @JsonIgnore
    private java.util.Date dateCreated;
    @JsonIgnore
    private java.util.Date lastUpdated;

    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private transient DetailerStockDao myDao;

    @JsonIgnore
    private MalariaDetail malariaDetail;
    @JsonIgnore
    private DetailerCall detailerCall;
    @JsonIgnore
    private String detailerCall__resolvedKey;
    // KEEP FIELDS END

    public DetailerStock() {
    }

    public DetailerStock(String uuid) {
        this.uuid = uuid;
    }

    public DetailerStock(String uuid, String brand, String category, double stockLevel, Double buyingPrice, Double sellingPrice, String packSize, String detailerId, String malariadetailId, Boolean isDirty, Integer syncronisationStatus, String syncronisationMessage, java.util.Date dateCreated, java.util.Date lastUpdated) {
        this.uuid = uuid;
        this.brand = brand;
        this.category = category;
        this.stockLevel = stockLevel;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.packSize = packSize;
        this.detailerId = detailerId;
        this.malariadetailId = malariadetailId;
        this.isDirty = isDirty;
        this.syncronisationStatus = syncronisationStatus;
        this.syncronisationMessage = syncronisationMessage;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDetailerStockDao() : null;
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
    public String getBrand() {
        return brand;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /** Not-null value. */
    public String getCategory() {
        return category;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCategory(String category) {
        this.category = category;
    }

    public double getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(double stockLevel) {
        this.stockLevel = stockLevel;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    /** Not-null value. */
    public String getDetailerId() {
        return detailerId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDetailerId(String detailerId) {
        this.detailerId = detailerId;
    }

    /** Not-null value. */
    public String getMalariadetailId() {
        return malariadetailId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMalariadetailId(String malariadetailId) {
        this.malariadetailId = malariadetailId;
    }

    public Boolean getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public Integer getSyncronisationStatus() {
        return syncronisationStatus;
    }

    public void setSyncronisationStatus(Integer syncronisationStatus) {
        this.syncronisationStatus = syncronisationStatus;
    }

    public String getSyncronisationMessage() {
        return syncronisationMessage;
    }

    public void setSyncronisationMessage(String syncronisationMessage) {
        this.syncronisationMessage = syncronisationMessage;
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public java.util.Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /** To-one relationship, resolved on first access. */
    public DetailerCall getDetailerCall() {
        String __key = this.detailerId;
        if (detailerCall__resolvedKey == null || detailerCall__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DetailerCallDao targetDao = daoSession.getDetailerCallDao();
            DetailerCall detailerCallNew = targetDao.load(__key);
            synchronized (this) {
                detailerCall = detailerCallNew;
            	detailerCall__resolvedKey = __key;
            }
        }
        return detailerCall;
    }

    public void setDetailerCall(DetailerCall detailerCall) {
        if (detailerCall == null) {
            throw new DaoException("To-one property 'detailerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.detailerCall = detailerCall;
            detailerId = detailerCall.getUuid();
            detailerCall__resolvedKey = detailerId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public MalariaDetail getMalariaDetail() {
        String __key = this.malariadetailId;
        if (malariaDetail__resolvedKey == null || malariaDetail__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MalariaDetailDao targetDao = daoSession.getMalariaDetailDao();
            MalariaDetail malariaDetailNew = targetDao.load(__key);
            synchronized (this) {
                malariaDetail = malariaDetailNew;
            	malariaDetail__resolvedKey = __key;
            }
        }
        return malariaDetail;
    }

    public void setMalariaDetail(MalariaDetail malariaDetail) {
        if (malariaDetail == null) {
            throw new DaoException("To-one property 'malariadetailId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.malariaDetail = malariaDetail;
            malariadetailId = malariaDetail.getUuid();
            malariaDetail__resolvedKey = malariadetailId;
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
