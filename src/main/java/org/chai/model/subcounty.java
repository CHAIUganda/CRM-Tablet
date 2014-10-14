package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table SUBCOUNTY.
 */
public class subcounty {

    private Long id;
    /** Not-null value. */
    private String sysid;
    /** Not-null value. */
    private String name;
    private long districtId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient subcountyDao myDao;

    private List<parish> parishes;
    private List<customer> customers;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public subcounty() {
    }

    public subcounty(Long id) {
        this.id = id;
    }

    public subcounty(Long id, String sysid, String name, long districtId) {
        this.id = id;
        this.sysid = sysid;
        this.name = name;
        this.districtId = districtId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubcountyDao() : null;
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
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<parish> getParishes() {
        if (parishes == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            parishDao targetDao = daoSession.getParishDao();
            List<parish> parishesNew = targetDao._querySubcounty_Parishes(id);
            synchronized (this) {
                if(parishes == null) {
                    parishes = parishesNew;
                }
            }
        }
        return parishes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetParishes() {
        parishes = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<customer> getCustomers() {
        if (customers == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            customerDao targetDao = daoSession.getCustomerDao();
            List<customer> customersNew = targetDao._querySubcounty_Customers(id);
            synchronized (this) {
                if(customers == null) {
                    customers = customersNew;
                }
            }
        }
        return customers;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetCustomers() {
        customers = null;
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
