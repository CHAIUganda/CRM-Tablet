package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class SubcountyEntity {
    public static Entity createSubcountyEntity(Schema schema,Entity district){
        Entity subcounty = schema.addEntity("Subcounty");
        subcounty.addStringProperty("uuid").notNull().unique().primaryKey();
        subcounty.addStringProperty("name").notNull();

        Property districtId = subcounty.addStringProperty("districtId").notNull().getProperty();
        ToMany districtToSubcounties = district.addToMany(subcounty,districtId);
        districtToSubcounties.setName("subcounties");

        subcounty.addToOne(district,districtId);
        return subcounty;
    }
}
