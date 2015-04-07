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
 * Entity mapped to table SALE.
 */
public class Sale implements BaseEntity {

    /** Not-null value. */
    private String uuid;
    /** Not-null value. */
    private java.util.Date dateOfSale;
    private Boolean doYouStockOrsZinc;
    private String ifNoWhy;
    private String pointOfsaleMaterial;
    private String recommendationNextStep;
    private String governmentApproval;
    private Boolean isHistory;
    private Double latitude;
    private Double longitude;
    /** Not-null value. */
    private String orderId;

    private List<SaleData> salesDatas;
    private List<StokeData> stockDatas;

    // KEEP FIELDS - put your custom fields here
    /** Not-null value. */
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
    private String taskId;
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private transient SaleDao myDao;

    @JsonIgnore
    private Order order;
    @JsonIgnore
    private String order__resolvedKey;

    @JsonIgnore
    private Task task;
    @JsonIgnore
    private String task__resolvedKey;
    // KEEP FIELDS END

    public Sale() {
    }

    public Sale(String uuid) {
        this.uuid = uuid;
    }

    public Sale(String uuid, java.util.Date dateOfSale, Boolean doYouStockOrsZinc, String ifNoWhy, String pointOfsaleMaterial, String recommendationNextStep, String governmentApproval, Boolean isHistory, Double latitude, Double longitude, String orderId, String taskId, Boolean isDirty, Integer syncronisationStatus, String syncronisationMessage, java.util.Date dateCreated, java.util.Date lastUpdated) {
        this.uuid = uuid;
        this.dateOfSale = dateOfSale;
        this.doYouStockOrsZinc = doYouStockOrsZinc;
        this.ifNoWhy = ifNoWhy;
        this.pointOfsaleMaterial = pointOfsaleMaterial;
        this.recommendationNextStep = recommendationNextStep;
        this.governmentApproval = governmentApproval;
        this.isHistory = isHistory;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderId = orderId;
        this.taskId = taskId;
        this.isDirty = isDirty;
        this.syncronisationStatus = syncronisationStatus;
        this.syncronisationMessage = syncronisationMessage;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSaleDao() : null;
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

    public Boolean getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Boolean isHistory) {
        this.isHistory = isHistory;
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
    public String getOrderId() {
        return orderId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /** Not-null value. */
    public String getTaskId() {
        return taskId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
    public Order getOrder() {
        String __key = this.orderId;
        if (order__resolvedKey == null || order__resolvedKey != __key) {
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
            orderId = order.getUuid();
            order__resolvedKey = orderId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Task getTask() {
        String __key = this.taskId;
        if (task__resolvedKey == null || task__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            Task taskNew = targetDao.load(__key);
            synchronized (this) {
                task = taskNew;
            	task__resolvedKey = __key;
            }
        }
        return task;
    }

    public void setTask(Task task) {
        if (task == null) {
            throw new DaoException("To-one property 'taskId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.task = task;
            taskId = task.getUuid();
            task__resolvedKey = taskId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<SaleData> getSalesDatas() {
        if (salesDatas == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDataDao targetDao = daoSession.getSaleDataDao();
            List<SaleData> salesDatasNew = targetDao._querySale_SalesDatas(uuid);
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

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<StokeData> getStockDatas() {
        if (stockDatas == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StokeDataDao targetDao = daoSession.getStokeDataDao();
            List<StokeData> stockDatasNew = targetDao._querySale_StockDatas(uuid);
            synchronized (this) {
                if(stockDatas == null) {
                    stockDatas = stockDatasNew;
                }
            }
        }
        return stockDatas;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetStockDatas() {
        stockDatas = null;
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
