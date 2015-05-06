package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class VillageEntity {
    public static Entity createVillageEntity(Schema schema,Entity parish){
        Entity village = schema.addEntity("Village");
        village.addStringProperty("uuid").notNull().primaryKey();
        village.addStringProperty("name").notNull();

        Property parishId = village.addStringProperty("parishId").notNull().getProperty();
        ToMany parishToVillages = parish.addToMany(village,parishId);
        parishToVillages.setName("villages");

        village.addToOne(parish,parishId);
        return village;
    }
}
