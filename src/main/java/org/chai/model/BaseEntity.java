package org.chai.model;

import java.util.Date;

/**
 * Created by victor on 31-Mar-15.
 */
public interface BaseEntity {
    static int SYNC_SUCCESS = 0;
    static int SYNC_FAIL = 1;
    boolean isDirty = true;
    Integer syncronisationStatus = SYNC_FAIL;
    String syncronisationMessage = "";
    Date dateCreated = new Date();
    Date lastUpdated = new Date();

    public Boolean getIsDirty();

    public void setIsDirty(Boolean isDirty);

    public Integer getSyncronisationStatus();

    public void setSyncronisationStatus(Integer syncronisationStatus);

    public Date getDateCreated();

    public void setDateCreated(Date dateCreated);

    public Date getLastUpdated();

    public void setLastUpdated(Date lastUpdated);

    public void setSyncronisationMessage(String message);

    public String getSyncronisationMessage();

}
