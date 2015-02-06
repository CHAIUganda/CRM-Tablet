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
 * Entity mapped to table CUSTOMER.
 */
public class Customer {

    /** Not-null value. */
    private String uuid;
    private Double latitude;
    private Double longitude;
    /** Not-null value. */
    private String outletName;
    private String outletType;
    private String outletSize;
    private byte[] outletPicture;
    private String split;
    private String majoritySourceOfSupply;
    private String keyWholeSalerName;
    private String keyWholeSalerContact;
    private Boolean licenceVisible;
    private String typeOfLicence;
    private String descriptionOfOutletLocation;
    private Integer numberOfEmployees;
    private Integer numberOfCustomersPerDay;
    private String restockFrequency;
    private java.util.Date dateOutletOpened;
    private java.util.Date dateCreated;
    private java.util.Date lastUpdated;
    private Boolean isDirty;
    private String tradingCenter;
    private String subcountyUuid;
    private Boolean isActive;
    /** Not-null value. */
    private String subcountyId;
    private List<AdhockSale> adhockSales;

    // KEEP FIELDS - put your custom fields here

    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    @JsonIgnore
    /** Used for active entity operations. */
    private transient CustomerDao myDao;

    @JsonIgnore
    private Subcounty subcounty;
    @JsonIgnore
    private String subcounty__resolvedKey;

    private List<CustomerContact> customerContacts;
    @JsonIgnore
    private List<Order> orders;
    @JsonIgnore
    private List<Task> tasks;
    // KEEP FIELDS END

    public Customer() {
    }

    public Customer(String uuid) {
        this.uuid = uuid;
    }

    public Customer(String uuid, Double latitude, Double longitude, String outletName, String outletType, String outletSize, byte[] outletPicture, String split, String majoritySourceOfSupply, String keyWholeSalerName, String keyWholeSalerContact, Boolean licenceVisible, String typeOfLicence, String descriptionOfOutletLocation, Integer numberOfEmployees, Integer numberOfCustomersPerDay, String restockFrequency, java.util.Date dateOutletOpened, java.util.Date dateCreated, java.util.Date lastUpdated, Boolean isDirty, String tradingCenter, String subcountyUuid, Boolean isActive, String subcountyId) {
        this.uuid = uuid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outletName = outletName;
        this.outletType = outletType;
        this.outletSize = outletSize;
        this.outletPicture = outletPicture;
        this.split = split;
        this.majoritySourceOfSupply = majoritySourceOfSupply;
        this.keyWholeSalerName = keyWholeSalerName;
        this.keyWholeSalerContact = keyWholeSalerContact;
        this.licenceVisible = licenceVisible;
        this.typeOfLicence = typeOfLicence;
        this.descriptionOfOutletLocation = descriptionOfOutletLocation;
        this.numberOfEmployees = numberOfEmployees;
        this.numberOfCustomersPerDay = numberOfCustomersPerDay;
        this.restockFrequency = restockFrequency;
        this.dateOutletOpened = dateOutletOpened;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
        this.isDirty = isDirty;
        this.tradingCenter = tradingCenter;
        this.subcountyUuid = subcountyUuid;
        this.isActive = isActive;
        this.subcountyId = subcountyId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomerDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public String getOutletName() {
        return outletName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletType() {
        return outletType;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getOutletSize() {
        return outletSize;
    }

    public void setOutletSize(String outletSize) {
        this.outletSize = outletSize;
    }

    public byte[] getOutletPicture() {
        return outletPicture;
    }

    public void setOutletPicture(byte[] outletPicture) {
        this.outletPicture = outletPicture;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getMajoritySourceOfSupply() {
        return majoritySourceOfSupply;
    }

    public void setMajoritySourceOfSupply(String majoritySourceOfSupply) {
        this.majoritySourceOfSupply = majoritySourceOfSupply;
    }

    public String getKeyWholeSalerName() {
        return keyWholeSalerName;
    }

    public void setKeyWholeSalerName(String keyWholeSalerName) {
        this.keyWholeSalerName = keyWholeSalerName;
    }

    public String getKeyWholeSalerContact() {
        return keyWholeSalerContact;
    }

    public void setKeyWholeSalerContact(String keyWholeSalerContact) {
        this.keyWholeSalerContact = keyWholeSalerContact;
    }

    public Boolean getLicenceVisible() {
        return licenceVisible;
    }

    public void setLicenceVisible(Boolean licenceVisible) {
        this.licenceVisible = licenceVisible;
    }

    public String getTypeOfLicence() {
        return typeOfLicence;
    }

    public void setTypeOfLicence(String typeOfLicence) {
        this.typeOfLicence = typeOfLicence;
    }

    public String getDescriptionOfOutletLocation() {
        return descriptionOfOutletLocation;
    }

    public void setDescriptionOfOutletLocation(String descriptionOfOutletLocation) {
        this.descriptionOfOutletLocation = descriptionOfOutletLocation;
    }

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public Integer getNumberOfCustomersPerDay() {
        return numberOfCustomersPerDay;
    }

    public void setNumberOfCustomersPerDay(Integer numberOfCustomersPerDay) {
        this.numberOfCustomersPerDay = numberOfCustomersPerDay;
    }

    public String getRestockFrequency() {
        return restockFrequency;
    }

    public void setRestockFrequency(String restockFrequency) {
        this.restockFrequency = restockFrequency;
    }

    public java.util.Date getDateOutletOpened() {
        return dateOutletOpened;
    }

    public void setDateOutletOpened(java.util.Date dateOutletOpened) {
        this.dateOutletOpened = dateOutletOpened;
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

    public Boolean getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public String getTradingCenter() {
        return tradingCenter;
    }

    public void setTradingCenter(String tradingCenter) {
        this.tradingCenter = tradingCenter;
    }

    public String getSubcountyUuid() {
        return subcountyUuid;
    }

    public void setSubcountyUuid(String subcountyUuid) {
        this.subcountyUuid = subcountyUuid;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /** Not-null value. */
    public String getSubcountyId() {
        return subcountyId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSubcountyId(String subcountyId) {
        this.subcountyId = subcountyId;
    }

    /** To-one relationship, resolved on first access. */
    public Subcounty getSubcounty() {
        String __key = this.subcountyId;
        if (subcounty__resolvedKey == null || subcounty__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubcountyDao targetDao = daoSession.getSubcountyDao();
            Subcounty subcountyNew = targetDao.load(__key);
            synchronized (this) {
                subcounty = subcountyNew;
            	subcounty__resolvedKey = __key;
            }
        }
        return subcounty;
    }

    public void setSubcounty(Subcounty subcounty) {
        if (subcounty == null) {
            throw new DaoException("To-one property 'subcountyId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.subcounty = subcounty;
            subcountyId = subcounty.getUuid();
            subcounty__resolvedKey = subcountyId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<CustomerContact> getCustomerContacts() {
        if (customerContacts == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerContactDao targetDao = daoSession.getCustomerContactDao();
            List<CustomerContact> customerContactsNew = targetDao._queryCustomer_CustomerContacts(uuid);
            synchronized (this) {
                if(customerContacts == null) {
                    customerContacts = customerContactsNew;
                }
            }
        }
        return customerContacts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetCustomerContacts() {
        customerContacts = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Order> getOrders() {
        if (orders == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderDao targetDao = daoSession.getOrderDao();
            List<Order> ordersNew = targetDao._queryCustomer_Orders(uuid);
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
    public List<Task> getTasks() {
        if (tasks == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            List<Task> tasksNew = targetDao._queryCustomer_Tasks(uuid);
            synchronized (this) {
                if(tasks == null) {
                    tasks = tasksNew;
                }
            }
        }
        return tasks;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTasks() {
        tasks = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<AdhockSale> getAdhockSales() {
        if (adhockSales == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AdhockSaleDao targetDao = daoSession.getAdhockSaleDao();
            List<AdhockSale> adhockSalesNew = targetDao._queryCustomer_AdhockSales(uuid);
            synchronized (this) {
                if(adhockSales == null) {
                    adhockSales = adhockSalesNew;
                }
            }
        }
        return adhockSales;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetAdhockSales() {
        adhockSales = null;
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
    @Override
    public String toString() {
        return outletName;
    }
    // KEEP METHODS END

}
