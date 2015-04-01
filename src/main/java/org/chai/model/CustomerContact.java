package org.chai.model;

import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table CUSTOMER_CONTACT.
 */
public class CustomerContact implements BaseEntity {

    /** Not-null value. */
    private String uuid;
    private String contact;
    private String names;
    private String gender;
    private String role;
    /** Not-null value. */
    private String customerId;

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
    private transient CustomerContactDao myDao;
    @JsonIgnore
    private Customer customer;
    @JsonIgnore
    private String customer__resolvedKey;
    // KEEP FIELDS END

    public CustomerContact() {
    }

    public CustomerContact(String uuid) {
        this.uuid = uuid;
    }

    public CustomerContact(String uuid, String contact, String names, String gender, String role, String customerId, Boolean isDirty, Integer syncronisationStatus, String syncronisationMessage, java.util.Date dateCreated, java.util.Date lastUpdated) {
        this.uuid = uuid;
        this.contact = contact;
        this.names = names;
        this.gender = gender;
        this.role = role;
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
        myDao = daoSession != null ? daoSession.getCustomerContactDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
