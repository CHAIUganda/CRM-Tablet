package org.chai.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.chai.model.DetailerCall;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DETAILER_CALL.
*/
public class DetailerCallDao extends AbstractDao<DetailerCall, Long> {

    public static final String TABLENAME = "DETAILER_CALL";

    /**
     * Properties of entity DetailerCall.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uuid = new Property(1, String.class, "uuid", false, "UUID");
        public final static Property DateOfSurvey = new Property(2, java.util.Date.class, "dateOfSurvey", false, "DATE_OF_SURVEY");
        public final static Property DiarrheaPatientsInFacility = new Property(3, Integer.class, "diarrheaPatientsInFacility", false, "DIARRHEA_PATIENTS_IN_FACILITY");
        public final static Property HeardAboutDiarrheaTreatmentInChildren = new Property(4, String.class, "heardAboutDiarrheaTreatmentInChildren", false, "HEARD_ABOUT_DIARRHEA_TREATMENT_IN_CHILDREN");
        public final static Property HowDidYouHear = new Property(5, String.class, "howDidYouHear", false, "HOW_DID_YOU_HEAR");
        public final static Property OtherWaysHowYouHeard = new Property(6, String.class, "otherWaysHowYouHeard", false, "OTHER_WAYS_HOW_YOU_HEARD");
        public final static Property WhatYouKnowAbtDiarrhea = new Property(7, String.class, "whatYouKnowAbtDiarrhea", false, "WHAT_YOU_KNOW_ABT_DIARRHEA");
        public final static Property DiarrheaEffectsOnBody = new Property(8, String.class, "diarrheaEffectsOnBody", false, "DIARRHEA_EFFECTS_ON_BODY");
        public final static Property KnowledgeAbtOrsAndUsage = new Property(9, String.class, "knowledgeAbtOrsAndUsage", false, "KNOWLEDGE_ABT_ORS_AND_USAGE");
        public final static Property KnowledgeAbtZincAndUsage = new Property(10, String.class, "knowledgeAbtZincAndUsage", false, "KNOWLEDGE_ABT_ZINC_AND_USAGE");
        public final static Property WhyNotUseAntibiotics = new Property(11, String.class, "whyNotUseAntibiotics", false, "WHY_NOT_USE_ANTIBIOTICS");
        public final static Property DoYouStockOrsZinc = new Property(12, Boolean.class, "doYouStockOrsZinc", false, "DO_YOU_STOCK_ORS_ZINC");
        public final static Property HowManyZincInStock = new Property(13, Integer.class, "howManyZincInStock", false, "HOW_MANY_ZINC_IN_STOCK");
        public final static Property HowmanyOrsInStock = new Property(14, Integer.class, "howmanyOrsInStock", false, "HOWMANY_ORS_IN_STOCK");
        public final static Property ZincBrandsold = new Property(15, String.class, "zincBrandsold", false, "ZINC_BRANDSOLD");
        public final static Property OrsBrandSold = new Property(16, String.class, "orsBrandSold", false, "ORS_BRAND_SOLD");
        public final static Property IfNoWhy = new Property(17, String.class, "ifNoWhy", false, "IF_NO_WHY");
        public final static Property ZincPrice = new Property(18, Double.class, "zincPrice", false, "ZINC_PRICE");
        public final static Property OrsPrice = new Property(19, Double.class, "orsPrice", false, "ORS_PRICE");
        public final static Property BuyingPrice = new Property(20, Double.class, "buyingPrice", false, "BUYING_PRICE");
        public final static Property PointOfsaleMaterial = new Property(21, String.class, "pointOfsaleMaterial", false, "POINT_OFSALE_MATERIAL");
        public final static Property RecommendationNextStep = new Property(22, String.class, "recommendationNextStep", false, "RECOMMENDATION_NEXT_STEP");
        public final static Property RecommendationLevel = new Property(23, String.class, "recommendationLevel", false, "RECOMMENDATION_LEVEL");
        public final static Property TenureLength = new Property(24, Integer.class, "tenureLength", false, "TENURE_LENGTH");
        public final static Property Latitude = new Property(25, Double.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(26, Double.class, "longitude", false, "LONGITUDE");
        public final static Property TaskId = new Property(27, long.class, "taskId", false, "TASK_ID");
    };

    private DaoSession daoSession;

    private Query<DetailerCall> task_DetailersQuery;

    public DetailerCallDao(DaoConfig config) {
        super(config);
    }
    
    public DetailerCallDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DETAILER_CALL' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'UUID' TEXT NOT NULL UNIQUE ," + // 1: uuid
                "'DATE_OF_SURVEY' INTEGER," + // 2: dateOfSurvey
                "'DIARRHEA_PATIENTS_IN_FACILITY' INTEGER," + // 3: diarrheaPatientsInFacility
                "'HEARD_ABOUT_DIARRHEA_TREATMENT_IN_CHILDREN' TEXT," + // 4: heardAboutDiarrheaTreatmentInChildren
                "'HOW_DID_YOU_HEAR' TEXT," + // 5: howDidYouHear
                "'OTHER_WAYS_HOW_YOU_HEARD' TEXT," + // 6: otherWaysHowYouHeard
                "'WHAT_YOU_KNOW_ABT_DIARRHEA' TEXT," + // 7: whatYouKnowAbtDiarrhea
                "'DIARRHEA_EFFECTS_ON_BODY' TEXT," + // 8: diarrheaEffectsOnBody
                "'KNOWLEDGE_ABT_ORS_AND_USAGE' TEXT," + // 9: knowledgeAbtOrsAndUsage
                "'KNOWLEDGE_ABT_ZINC_AND_USAGE' TEXT," + // 10: knowledgeAbtZincAndUsage
                "'WHY_NOT_USE_ANTIBIOTICS' TEXT," + // 11: whyNotUseAntibiotics
                "'DO_YOU_STOCK_ORS_ZINC' INTEGER," + // 12: doYouStockOrsZinc
                "'HOW_MANY_ZINC_IN_STOCK' INTEGER," + // 13: howManyZincInStock
                "'HOWMANY_ORS_IN_STOCK' INTEGER," + // 14: howmanyOrsInStock
                "'ZINC_BRANDSOLD' TEXT," + // 15: zincBrandsold
                "'ORS_BRAND_SOLD' TEXT," + // 16: orsBrandSold
                "'IF_NO_WHY' TEXT," + // 17: ifNoWhy
                "'ZINC_PRICE' REAL," + // 18: zincPrice
                "'ORS_PRICE' REAL," + // 19: orsPrice
                "'BUYING_PRICE' REAL," + // 20: buyingPrice
                "'POINT_OFSALE_MATERIAL' TEXT," + // 21: pointOfsaleMaterial
                "'RECOMMENDATION_NEXT_STEP' TEXT," + // 22: recommendationNextStep
                "'RECOMMENDATION_LEVEL' TEXT," + // 23: recommendationLevel
                "'TENURE_LENGTH' INTEGER," + // 24: tenureLength
                "'LATITUDE' REAL," + // 25: latitude
                "'LONGITUDE' REAL," + // 26: longitude
                "'TASK_ID' INTEGER NOT NULL );"); // 27: taskId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DETAILER_CALL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DetailerCall entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUuid());
 
        java.util.Date dateOfSurvey = entity.getDateOfSurvey();
        if (dateOfSurvey != null) {
            stmt.bindLong(3, dateOfSurvey.getTime());
        }
 
        Integer diarrheaPatientsInFacility = entity.getDiarrheaPatientsInFacility();
        if (diarrheaPatientsInFacility != null) {
            stmt.bindLong(4, diarrheaPatientsInFacility);
        }
 
        String heardAboutDiarrheaTreatmentInChildren = entity.getHeardAboutDiarrheaTreatmentInChildren();
        if (heardAboutDiarrheaTreatmentInChildren != null) {
            stmt.bindString(5, heardAboutDiarrheaTreatmentInChildren);
        }
 
        String howDidYouHear = entity.getHowDidYouHear();
        if (howDidYouHear != null) {
            stmt.bindString(6, howDidYouHear);
        }
 
        String otherWaysHowYouHeard = entity.getOtherWaysHowYouHeard();
        if (otherWaysHowYouHeard != null) {
            stmt.bindString(7, otherWaysHowYouHeard);
        }
 
        String whatYouKnowAbtDiarrhea = entity.getWhatYouKnowAbtDiarrhea();
        if (whatYouKnowAbtDiarrhea != null) {
            stmt.bindString(8, whatYouKnowAbtDiarrhea);
        }
 
        String diarrheaEffectsOnBody = entity.getDiarrheaEffectsOnBody();
        if (diarrheaEffectsOnBody != null) {
            stmt.bindString(9, diarrheaEffectsOnBody);
        }
 
        String knowledgeAbtOrsAndUsage = entity.getKnowledgeAbtOrsAndUsage();
        if (knowledgeAbtOrsAndUsage != null) {
            stmt.bindString(10, knowledgeAbtOrsAndUsage);
        }
 
        String knowledgeAbtZincAndUsage = entity.getKnowledgeAbtZincAndUsage();
        if (knowledgeAbtZincAndUsage != null) {
            stmt.bindString(11, knowledgeAbtZincAndUsage);
        }
 
        String whyNotUseAntibiotics = entity.getWhyNotUseAntibiotics();
        if (whyNotUseAntibiotics != null) {
            stmt.bindString(12, whyNotUseAntibiotics);
        }
 
        Boolean doYouStockOrsZinc = entity.getDoYouStockOrsZinc();
        if (doYouStockOrsZinc != null) {
            stmt.bindLong(13, doYouStockOrsZinc ? 1l: 0l);
        }
 
        Integer howManyZincInStock = entity.getHowManyZincInStock();
        if (howManyZincInStock != null) {
            stmt.bindLong(14, howManyZincInStock);
        }
 
        Integer howmanyOrsInStock = entity.getHowmanyOrsInStock();
        if (howmanyOrsInStock != null) {
            stmt.bindLong(15, howmanyOrsInStock);
        }
 
        String zincBrandsold = entity.getZincBrandsold();
        if (zincBrandsold != null) {
            stmt.bindString(16, zincBrandsold);
        }
 
        String orsBrandSold = entity.getOrsBrandSold();
        if (orsBrandSold != null) {
            stmt.bindString(17, orsBrandSold);
        }
 
        String ifNoWhy = entity.getIfNoWhy();
        if (ifNoWhy != null) {
            stmt.bindString(18, ifNoWhy);
        }
 
        Double zincPrice = entity.getZincPrice();
        if (zincPrice != null) {
            stmt.bindDouble(19, zincPrice);
        }
 
        Double orsPrice = entity.getOrsPrice();
        if (orsPrice != null) {
            stmt.bindDouble(20, orsPrice);
        }
 
        Double buyingPrice = entity.getBuyingPrice();
        if (buyingPrice != null) {
            stmt.bindDouble(21, buyingPrice);
        }
 
        String pointOfsaleMaterial = entity.getPointOfsaleMaterial();
        if (pointOfsaleMaterial != null) {
            stmt.bindString(22, pointOfsaleMaterial);
        }
 
        String recommendationNextStep = entity.getRecommendationNextStep();
        if (recommendationNextStep != null) {
            stmt.bindString(23, recommendationNextStep);
        }
 
        String recommendationLevel = entity.getRecommendationLevel();
        if (recommendationLevel != null) {
            stmt.bindString(24, recommendationLevel);
        }
 
        Integer tenureLength = entity.getTenureLength();
        if (tenureLength != null) {
            stmt.bindLong(25, tenureLength);
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(26, latitude);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(27, longitude);
        }
        stmt.bindLong(28, entity.getTaskId());
    }

    @Override
    protected void attachEntity(DetailerCall entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DetailerCall readEntity(Cursor cursor, int offset) {
        DetailerCall entity = new DetailerCall( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // uuid
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // dateOfSurvey
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // diarrheaPatientsInFacility
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // heardAboutDiarrheaTreatmentInChildren
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // howDidYouHear
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // otherWaysHowYouHeard
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // whatYouKnowAbtDiarrhea
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // diarrheaEffectsOnBody
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // knowledgeAbtOrsAndUsage
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // knowledgeAbtZincAndUsage
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // whyNotUseAntibiotics
            cursor.isNull(offset + 12) ? null : cursor.getShort(offset + 12) != 0, // doYouStockOrsZinc
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // howManyZincInStock
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14), // howmanyOrsInStock
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // zincBrandsold
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // orsBrandSold
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // ifNoWhy
            cursor.isNull(offset + 18) ? null : cursor.getDouble(offset + 18), // zincPrice
            cursor.isNull(offset + 19) ? null : cursor.getDouble(offset + 19), // orsPrice
            cursor.isNull(offset + 20) ? null : cursor.getDouble(offset + 20), // buyingPrice
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // pointOfsaleMaterial
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // recommendationNextStep
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // recommendationLevel
            cursor.isNull(offset + 24) ? null : cursor.getInt(offset + 24), // tenureLength
            cursor.isNull(offset + 25) ? null : cursor.getDouble(offset + 25), // latitude
            cursor.isNull(offset + 26) ? null : cursor.getDouble(offset + 26), // longitude
            cursor.getLong(offset + 27) // taskId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DetailerCall entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUuid(cursor.getString(offset + 1));
        entity.setDateOfSurvey(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setDiarrheaPatientsInFacility(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setHeardAboutDiarrheaTreatmentInChildren(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHowDidYouHear(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setOtherWaysHowYouHeard(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWhatYouKnowAbtDiarrhea(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDiarrheaEffectsOnBody(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setKnowledgeAbtOrsAndUsage(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setKnowledgeAbtZincAndUsage(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setWhyNotUseAntibiotics(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setDoYouStockOrsZinc(cursor.isNull(offset + 12) ? null : cursor.getShort(offset + 12) != 0);
        entity.setHowManyZincInStock(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setHowmanyOrsInStock(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
        entity.setZincBrandsold(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setOrsBrandSold(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setIfNoWhy(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setZincPrice(cursor.isNull(offset + 18) ? null : cursor.getDouble(offset + 18));
        entity.setOrsPrice(cursor.isNull(offset + 19) ? null : cursor.getDouble(offset + 19));
        entity.setBuyingPrice(cursor.isNull(offset + 20) ? null : cursor.getDouble(offset + 20));
        entity.setPointOfsaleMaterial(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setRecommendationNextStep(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setRecommendationLevel(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setTenureLength(cursor.isNull(offset + 24) ? null : cursor.getInt(offset + 24));
        entity.setLatitude(cursor.isNull(offset + 25) ? null : cursor.getDouble(offset + 25));
        entity.setLongitude(cursor.isNull(offset + 26) ? null : cursor.getDouble(offset + 26));
        entity.setTaskId(cursor.getLong(offset + 27));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DetailerCall entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DetailerCall entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "detailers" to-many relationship of Task. */
    public List<DetailerCall> _queryTask_Detailers(long taskId) {
        synchronized (this) {
            if (task_DetailersQuery == null) {
                QueryBuilder<DetailerCall> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.TaskId.eq(null));
                task_DetailersQuery = queryBuilder.build();
            }
        }
        Query<DetailerCall> query = task_DetailersQuery.forCurrentThread();
        query.setParameter(0, taskId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getTaskDao().getAllColumns());
            builder.append(" FROM DETAILER_CALL T");
            builder.append(" LEFT JOIN TASK T0 ON T.'TASK_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DetailerCall loadCurrentDeep(Cursor cursor, boolean lock) {
        DetailerCall entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Task task = loadCurrentOther(daoSession.getTaskDao(), cursor, offset);
         if(task != null) {
            entity.setTask(task);
        }

        return entity;    
    }

    public DetailerCall loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<DetailerCall> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DetailerCall> list = new ArrayList<DetailerCall>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<DetailerCall> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DetailerCall> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
