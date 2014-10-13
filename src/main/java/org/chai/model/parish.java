package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table PARISH.
 */
public class parish {

    private Long id;
    /** Not-null value. */
    private String name;
    private long subcountyId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient parishDao myDao;

    private List<village> villages;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public parish() {
    }

    public parish(Long id) {
        this.id = id;
    }

    public parish(Long id, String name, long subcountyId) {
        this.id = id;
        this.name = name;
        this.subcountyId = subcountyId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getParishDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public long getSubcountyId() {
        return subcountyId;
    }

    public void setSubcountyId(long subcountyId) {
        this.subcountyId = subcountyId;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<village> getVillages() {
        if (villages == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            villageDao targetDao = daoSession.getVillageDao();
            List<village> villagesNew = targetDao._queryParish_Villages(id);
            synchronized (this) {
                if(villages == null) {
                    villages = villagesNew;
                }
            }
        }
        return villages;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetVillages() {
        villages = null;
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
