package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by Zed on 5/6/2015.
 */
public class MalariaDetailEntity {
    public static Entity createMalariaDetailEntity(Schema schema,Entity task){
        Entity malaria = schema.addEntity("MalariaDetail");

        malaria.addStringProperty("uuid").unique().notNull().primaryKey();
        malaria.addDateProperty("dateOfSurvey");
        malaria.addIntProperty("malariaPatientsInFacility");
        malaria.addIntProperty("numberOfChildren");
        malaria.addStringProperty("doYouPrescribeTreatment");
        malaria.addStringProperty("heardAboutGreenLeaf");
        malaria.addStringProperty("howDidYouHear");
        malaria.addStringProperty("otherWaysHowYouHeard");
        malaria.addStringProperty("howYouSuspectMalaria");
        malaria.addStringProperty("doYouKnowMOHGuidelines");
        malaria.addStringProperty("mohGuidelines");
        malaria.addStringProperty("knowAboutGreenLeafAntimalarials");
        malaria.addStringProperty("whatGreenLeafRepresents");
        malaria.addStringProperty("doYouPrescribeWithoutGreenLeaf");
        malaria.addStringProperty("whyPrescribeWithoutGreenLeaf");
        malaria.addStringProperty("knowWhatSevereMalariaIs");
        malaria.addStringProperty("signsOfSevereMalaria");
        malaria.addStringProperty("howToManagePatientsWithSevereMalaria");
        malaria.addBooleanProperty("doYouStockAntimalarials");
        malaria.addBooleanProperty("doYouStockRDTs");
        malaria.addStringProperty("pointOfsaleMaterial");
        malaria.addStringProperty("recommendationNextStep");
        malaria.addDoubleProperty("latitude");
        malaria.addDoubleProperty("longitude");
        malaria.addBooleanProperty("isNew");
        malaria.addBooleanProperty("isHistory");

        Property taskId = malaria.addStringProperty("taskId").notNull().getProperty();
        ToMany taskToDetailer = task.addToMany(malaria, taskId);

        taskToDetailer.setName("malariadetails");
        malaria.addToOne(task, taskId);

        malaria.addBooleanProperty("isDirty");
        malaria.addIntProperty("syncronisationStatus");
        malaria.addStringProperty("syncronisationMessage");
        malaria.addDateProperty("dateCreated");
        malaria.addDateProperty("lastUpdated");
        malaria.implementsInterface("BaseEntity");

        return malaria;
    }
}
