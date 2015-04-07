package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table TASK.
 */
public class Task implements BaseEntity {

    /** Not-null value. */
    private String uuid;
    private String description;
    private String status;
    private String priority;
    private String type;
    private java.util.Date dueDate;
    private java.util.Date completionDate;
    private java.util.Date dateScheduled;
    private Boolean isAdhock;
    /** Not-null value. */
    private String customerId;

    private Customer customer;
    private String customer__resolvedKey;

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
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private transient TaskDao myDao;
    @JsonIgnore
    private List<Sale> sales = new ArrayList<Sale>();
    private List<DetailerCall> detailers;
    private List<TaskOrder> lineItems;
    // KEEP FIELDS END

    public Task() {
    }

    public Task(String uuid) {
        this.uuid = uuid;
    }

    public Task(String uuid, String description, String status, String priority, String type, java.util.Date dueDate, java.util.Date completionDate, java.util.Date dateScheduled, Boolean isAdhock, String customerId, Boolean isDirty, Integer syncronisationStatus, String syncronisationMessage, java.util.Date dateCreated, java.util.Date lastUpdated) {
        this.uuid = uuid;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.type = type;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.dateScheduled = dateScheduled;
        this.isAdhock = isAdhock;
        this.customerId = customerId;
        this.isDirty = isDirty;
        this.syncronisationStatus = syncronisationStatus;
        this.syncronisationMessage = syncronisationMessage;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public java.util.Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(java.util.Date dueDate) {
        this.dueDate = dueDate;
    }

    public java.util.Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(java.util.Date completionDate) {
        this.completionDate = completionDate;
    }

    public java.util.Date getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(java.util.Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public Boolean getIsAdhock() {
        return isAdhock;
    }

    public void setIsAdhock(Boolean isAdhock) {
        this.isAdhock = isAdhock;
    }

    /** Not-null value. */
    public String getCustomerId() {
        return customerId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
    public List<TaskOrder> getLineItems() {
        if (lineItems == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskOrderDao targetDao = daoSession.getTaskOrderDao();
            List<TaskOrder> lineItemsNew = targetDao._queryTask_LineItems(uuid);
            synchronized (this) {
                if(lineItems == null) {
                    lineItems = lineItemsNew;
                }
            }
        }
        return lineItems;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetLineItems() {
        lineItems = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Sale> getSales() {
        if (sales == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDao targetDao = daoSession.getSaleDao();
            List<Sale> salesNew = targetDao._queryTask_Sales(uuid);
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
    public List<DetailerCall> getDetailers() {
        if (detailers == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DetailerCallDao targetDao = daoSession.getDetailerCallDao();
            List<DetailerCall> detailersNew = targetDao._queryTask_Detailers(uuid);
            synchronized (this) {
                if(detailers == null) {
                    detailers = detailersNew;
                }
            }
        }
        return detailers;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDetailers() {
        detailers = null;
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
