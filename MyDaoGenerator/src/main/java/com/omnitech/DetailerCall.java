package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/30/14.
 */
public class DetailerCall {
    public static Entity createDetailerCallEntity(Schema schema,Entity task){
        Entity detailer = schema.addEntity("DetailerCall");
        detailer.addStringProperty("uuid").unique().notNull().primaryKey();
        detailer.addDateProperty("dateOfSurvey");
        detailer.addIntProperty("diarrheaPatientsInFacility");
        detailer.addStringProperty("heardAboutDiarrheaTreatmentInChildren");
        detailer.addStringProperty("howDidYouHear");
        detailer.addStringProperty("otherWaysHowYouHeard");
        detailer.addStringProperty("whatYouKnowAbtDiarrhea");
        detailer.addStringProperty("diarrheaEffectsOnBody");
        detailer.addStringProperty("knowledgeAbtOrsAndUsage");
        detailer.addStringProperty("knowledgeAbtZincAndUsage");
        detailer.addStringProperty("whyNotUseAntibiotics");
        detailer.addBooleanProperty("doYouStockZinc");
        detailer.addBooleanProperty("doYouStockOrs");
        detailer.addStringProperty("ifNoZincWhy");
        detailer.addStringProperty("ifNoOrsWhy");
        detailer.addStringProperty("pointOfsaleMaterial");
        detailer.addStringProperty("recommendationNextStep");
        detailer.addStringProperty("recommendationLevel");
        detailer.addIntProperty("tenureLength");
        detailer.addDoubleProperty("latitude");
        detailer.addDoubleProperty("longitude");
        detailer.addBooleanProperty("isNew");
        detailer.addBooleanProperty("isHistory");
        detailer.addStringProperty("objections");

        Property taskId = detailer.addStringProperty("taskId").notNull().getProperty();
        ToMany taskToDetailer = task.addToMany(detailer, taskId);

        taskToDetailer.setName("detailers");
        detailer.addToOne(task, taskId);

        detailer.addBooleanProperty("isDirty");
        detailer.addIntProperty("syncronisationStatus");
        detailer.addStringProperty("syncronisationMessage");
        detailer.addDateProperty("dateCreated");
        detailer.addDateProperty("lastUpdated");
        detailer.implementsInterface("BaseEntity");

        return detailer;

    }
}
