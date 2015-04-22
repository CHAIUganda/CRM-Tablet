package org.chai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.greenrobot.dao.DaoException;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS
// KEEP INCLUDES - put your custom includes here
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table REGION.
 */
public class Region {

    /** Not-null value. */
    private String uuid;
    /** Not-null value. */
    private String name;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RegionDao myDao;

    private List<District> districts;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Region() {
    }

    public Region(String uuid) {
        this.uuid = uuid;
    }

    public Region(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRegionDao() : null;
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
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<District> getDistricts() {
        if (districts == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DistrictDao targetDao = daoSession.getDistrictDao();
            List<District> districtsNew = targetDao._queryRegion_Districts(uuid);
            synchronized (this) {
                if(districts == null) {
                    districts = districtsNew;
                }
            }
        }
        return districts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDistricts() {
        districts = null;
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
