package org.chai.model;

import java.util.List;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table DETAILER_CALL.
 */
public class DetailerCall implements BaseEntity {

    /** Not-null value. */
    private String uuid;
    private java.util.Date dateOfSurvey;
    private Integer diarrheaPatientsInFacility;
    private String heardAboutDiarrheaTreatmentInChildren;
    private String howDidYouHear;
    private String otherWaysHowYouHeard;
    private String whatYouKnowAbtDiarrhea;
    private String diarrheaEffectsOnBody;
    private String knowledgeAbtOrsAndUsage;
    private String knowledgeAbtZincAndUsage;
    private String whyNotUseAntibiotics;
    private Boolean doYouStockZinc;
    private Boolean doYouStockOrs;
    private String ifNoZincWhy;
    private String ifNoOrsWhy;
    private String pointOfsaleMaterial;
    private String recommendationNextStep;
    private String recommendationLevel;
    private Integer tenureLength;
    private Double latitude;
    private Double longitude;
    private Boolean isNew;
    private Boolean isHistory;
    private String objections;
    /** Not-null value. */
    private String taskId;
    private Boolean isDirty;
    private Integer syncronisationStatus;
    private String syncronisationMessage;
    private java.util.Date dateCreated;
    private java.util.Date lastUpdated;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient DetailerCallDao myDao;

    private Task task;
    private String task__resolvedKey;

    private List<DetailerStock> detailerStocks;

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
    private transient DetailerCallDao myDao;
    @JsonIgnore
    private Task task;
    @JsonIgnore
    private String task__resolvedKey;
    // KEEP FIELDS END

    public DetailerCall() {
    }

    public DetailerCall(String uuid) {
        this.uuid = uuid;
    }

    public DetailerCall(String uuid, java.util.Date dateOfSurvey, Integer diarrheaPatientsInFacility, String heardAboutDiarrheaTreatmentInChildren, String howDidYouHear, String otherWaysHowYouHeard, String whatYouKnowAbtDiarrhea, String diarrheaEffectsOnBody, String knowledgeAbtOrsAndUsage, String knowledgeAbtZincAndUsage, String whyNotUseAntibiotics, Boolean doYouStockZinc, Boolean doYouStockOrs, String ifNoZincWhy, String ifNoOrsWhy, String pointOfsaleMaterial, String recommendationNextStep, String recommendationLevel, Integer tenureLength, Double latitude, Double longitude, Boolean isNew, Boolean isHistory, String objections, String taskId, Boolean isDirty, Integer syncronisationStatus, String syncronisationMessage, java.util.Date dateCreated, java.util.Date lastUpdated) {
        this.uuid = uuid;
        this.dateOfSurvey = dateOfSurvey;
        this.diarrheaPatientsInFacility = diarrheaPatientsInFacility;
        this.heardAboutDiarrheaTreatmentInChildren = heardAboutDiarrheaTreatmentInChildren;
        this.howDidYouHear = howDidYouHear;
        this.otherWaysHowYouHeard = otherWaysHowYouHeard;
        this.whatYouKnowAbtDiarrhea = whatYouKnowAbtDiarrhea;
        this.diarrheaEffectsOnBody = diarrheaEffectsOnBody;
        this.knowledgeAbtOrsAndUsage = knowledgeAbtOrsAndUsage;
        this.knowledgeAbtZincAndUsage = knowledgeAbtZincAndUsage;
        this.whyNotUseAntibiotics = whyNotUseAntibiotics;
        this.doYouStockZinc = doYouStockZinc;
        this.doYouStockOrs = doYouStockOrs;
        this.ifNoZincWhy = ifNoZincWhy;
        this.ifNoOrsWhy = ifNoOrsWhy;
        this.pointOfsaleMaterial = pointOfsaleMaterial;
        this.recommendationNextStep = recommendationNextStep;
        this.recommendationLevel = recommendationLevel;
        this.tenureLength = tenureLength;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isNew = isNew;
        this.isHistory = isHistory;
        this.objections = objections;
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
        myDao = daoSession != null ? daoSession.getDetailerCallDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public java.util.Date getDateOfSurvey() {
        return dateOfSurvey;
    }

    public void setDateOfSurvey(java.util.Date dateOfSurvey) {
        this.dateOfSurvey = dateOfSurvey;
    }

    public Integer getDiarrheaPatientsInFacility() {
        return diarrheaPatientsInFacility;
    }

    public void setDiarrheaPatientsInFacility(Integer diarrheaPatientsInFacility) {
        this.diarrheaPatientsInFacility = diarrheaPatientsInFacility;
    }

    public String getHeardAboutDiarrheaTreatmentInChildren() {
        return heardAboutDiarrheaTreatmentInChildren;
    }

    public void setHeardAboutDiarrheaTreatmentInChildren(String heardAboutDiarrheaTreatmentInChildren) {
        this.heardAboutDiarrheaTreatmentInChildren = heardAboutDiarrheaTreatmentInChildren;
    }

    public String getHowDidYouHear() {
        return howDidYouHear;
    }

    public void setHowDidYouHear(String howDidYouHear) {
        this.howDidYouHear = howDidYouHear;
    }

    public String getOtherWaysHowYouHeard() {
        return otherWaysHowYouHeard;
    }

    public void setOtherWaysHowYouHeard(String otherWaysHowYouHeard) {
        this.otherWaysHowYouHeard = otherWaysHowYouHeard;
    }

    public String getWhatYouKnowAbtDiarrhea() {
        return whatYouKnowAbtDiarrhea;
    }

    public void setWhatYouKnowAbtDiarrhea(String whatYouKnowAbtDiarrhea) {
        this.whatYouKnowAbtDiarrhea = whatYouKnowAbtDiarrhea;
    }

    public String getDiarrheaEffectsOnBody() {
        return diarrheaEffectsOnBody;
    }

    public void setDiarrheaEffectsOnBody(String diarrheaEffectsOnBody) {
        this.diarrheaEffectsOnBody = diarrheaEffectsOnBody;
    }

    public String getKnowledgeAbtOrsAndUsage() {
        return knowledgeAbtOrsAndUsage;
    }

    public void setKnowledgeAbtOrsAndUsage(String knowledgeAbtOrsAndUsage) {
        this.knowledgeAbtOrsAndUsage = knowledgeAbtOrsAndUsage;
    }

    public String getKnowledgeAbtZincAndUsage() {
        return knowledgeAbtZincAndUsage;
    }

    public void setKnowledgeAbtZincAndUsage(String knowledgeAbtZincAndUsage) {
        this.knowledgeAbtZincAndUsage = knowledgeAbtZincAndUsage;
    }

    public String getWhyNotUseAntibiotics() {
        return whyNotUseAntibiotics;
    }

    public void setWhyNotUseAntibiotics(String whyNotUseAntibiotics) {
        this.whyNotUseAntibiotics = whyNotUseAntibiotics;
    }

    public Boolean getDoYouStockZinc() {
        return doYouStockZinc;
    }

    public void setDoYouStockZinc(Boolean doYouStockZinc) {
        this.doYouStockZinc = doYouStockZinc;
    }

    public Boolean getDoYouStockOrs() {
        return doYouStockOrs;
    }

    public void setDoYouStockOrs(Boolean doYouStockOrs) {
        this.doYouStockOrs = doYouStockOrs;
    }

    public String getIfNoZincWhy() {
        return ifNoZincWhy;
    }

    public void setIfNoZincWhy(String ifNoZincWhy) {
        this.ifNoZincWhy = ifNoZincWhy;
    }

    public String getIfNoOrsWhy() {
        return ifNoOrsWhy;
    }

    public void setIfNoOrsWhy(String ifNoOrsWhy) {
        this.ifNoOrsWhy = ifNoOrsWhy;
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

    public String getRecommendationLevel() {
        return recommendationLevel;
    }

    public void setRecommendationLevel(String recommendationLevel) {
        this.recommendationLevel = recommendationLevel;
    }

    public Integer getTenureLength() {
        return tenureLength;
    }

    public void setTenureLength(Integer tenureLength) {
        this.tenureLength = tenureLength;
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

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Boolean isHistory) {
        this.isHistory = isHistory;
    }

    public String getObjections() {
        return objections;
    }

    public void setObjections(String objections) {
        this.objections = objections;
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
    public List<DetailerStock> getDetailerStocks() {
        if (detailerStocks == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DetailerStockDao targetDao = daoSession.getDetailerStockDao();
            List<DetailerStock> detailerStocksNew = targetDao._queryDetailerCall_DetailerStocks(uuid);
            synchronized (this) {
                if(detailerStocks == null) {
                    detailerStocks = detailerStocksNew;
                }
            }
        }
        return detailerStocks;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDetailerStocks() {
        detailerStocks = null;
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
