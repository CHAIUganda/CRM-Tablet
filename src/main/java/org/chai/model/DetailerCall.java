package org.chai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.chai.model.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table DETAILER_CALL.
 */
public class DetailerCall {

    private Long id;
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
    private Boolean doYouStockOrsZinc;
    private Integer howManyZincInStock;
    private Integer howmanyOrsInStock;
    private String zincBrandsold;
    private String orsBrandSold;
    private String ifNoWhy;
    private Double zincPrice;
    private Double orsPrice;
    private Double buyingPrice;
    private String pointOfsaleMaterial;
    private String recommendationNextStep;
    private String recommendationLevel;
    private Integer tenureLength;
    private Double latitude;
    private Double longitude;
    private long taskId;


    // KEEP FIELDS - put your custom fields here

    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private transient DetailerCallDao myDao;
    @JsonIgnore
    private Task task;
    @JsonIgnore
    private Long task__resolvedKey;
    // KEEP FIELDS END

    public DetailerCall() {
    }

    public DetailerCall(Long id) {
        this.id = id;
    }

    public DetailerCall(Long id, String uuid, java.util.Date dateOfSurvey, Integer diarrheaPatientsInFacility, String heardAboutDiarrheaTreatmentInChildren, String howDidYouHear, String otherWaysHowYouHeard, String whatYouKnowAbtDiarrhea, String diarrheaEffectsOnBody, String knowledgeAbtOrsAndUsage, String knowledgeAbtZincAndUsage, String whyNotUseAntibiotics, Boolean doYouStockOrsZinc, Integer howManyZincInStock, Integer howmanyOrsInStock, String zincBrandsold, String orsBrandSold, String ifNoWhy, Double zincPrice, Double orsPrice, Double buyingPrice, String pointOfsaleMaterial, String recommendationNextStep, String recommendationLevel, Integer tenureLength, Double latitude, Double longitude, long taskId) {
        this.id = id;
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
        this.doYouStockOrsZinc = doYouStockOrsZinc;
        this.howManyZincInStock = howManyZincInStock;
        this.howmanyOrsInStock = howmanyOrsInStock;
        this.zincBrandsold = zincBrandsold;
        this.orsBrandSold = orsBrandSold;
        this.ifNoWhy = ifNoWhy;
        this.zincPrice = zincPrice;
        this.orsPrice = orsPrice;
        this.buyingPrice = buyingPrice;
        this.pointOfsaleMaterial = pointOfsaleMaterial;
        this.recommendationNextStep = recommendationNextStep;
        this.recommendationLevel = recommendationLevel;
        this.tenureLength = tenureLength;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taskId = taskId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDetailerCallDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getDoYouStockOrsZinc() {
        return doYouStockOrsZinc;
    }

    public void setDoYouStockOrsZinc(Boolean doYouStockOrsZinc) {
        this.doYouStockOrsZinc = doYouStockOrsZinc;
    }

    public Integer getHowManyZincInStock() {
        return howManyZincInStock;
    }

    public void setHowManyZincInStock(Integer howManyZincInStock) {
        this.howManyZincInStock = howManyZincInStock;
    }

    public Integer getHowmanyOrsInStock() {
        return howmanyOrsInStock;
    }

    public void setHowmanyOrsInStock(Integer howmanyOrsInStock) {
        this.howmanyOrsInStock = howmanyOrsInStock;
    }

    public String getZincBrandsold() {
        return zincBrandsold;
    }

    public void setZincBrandsold(String zincBrandsold) {
        this.zincBrandsold = zincBrandsold;
    }

    public String getOrsBrandSold() {
        return orsBrandSold;
    }

    public void setOrsBrandSold(String orsBrandSold) {
        this.orsBrandSold = orsBrandSold;
    }

    public String getIfNoWhy() {
        return ifNoWhy;
    }

    public void setIfNoWhy(String ifNoWhy) {
        this.ifNoWhy = ifNoWhy;
    }

    public Double getZincPrice() {
        return zincPrice;
    }

    public void setZincPrice(Double zincPrice) {
        this.zincPrice = zincPrice;
    }

    public Double getOrsPrice() {
        return orsPrice;
    }

    public void setOrsPrice(Double orsPrice) {
        this.orsPrice = orsPrice;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
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

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    /** To-one relationship, resolved on first access. */
    public Task getTask() {
        long __key = this.taskId;
        if (task__resolvedKey == null || !task__resolvedKey.equals(__key)) {
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
            taskId = task.getId();
            task__resolvedKey = taskId;
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
