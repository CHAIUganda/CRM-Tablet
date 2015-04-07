package org.chai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS
// KEEP INCLUDES - put your custom includes here
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table SUMMARY_REPORT.
 */
public class SummaryReport {

    /** Not-null value. */
    private String uuid;
    private String item;
    private String week;
    private String month;
    private String teamAverageThisWeek;
    private String teamAverageThisMonth;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient SummaryReportDao myDao;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public SummaryReport() {
    }

    public SummaryReport(String uuid) {
        this.uuid = uuid;
    }

    public SummaryReport(String uuid, String item, String week, String month, String teamAverageThisWeek, String teamAverageThisMonth) {
        this.uuid = uuid;
        this.item = item;
        this.week = week;
        this.month = month;
        this.teamAverageThisWeek = teamAverageThisWeek;
        this.teamAverageThisMonth = teamAverageThisMonth;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSummaryReportDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTeamAverageThisWeek() {
        return teamAverageThisWeek;
    }

    public void setTeamAverageThisWeek(String teamAverageThisWeek) {
        this.teamAverageThisWeek = teamAverageThisWeek;
    }

    public String getTeamAverageThisMonth() {
        return teamAverageThisMonth;
    }

    public void setTeamAverageThisMonth(String teamAverageThisMonth) {
        this.teamAverageThisMonth = teamAverageThisMonth;
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
