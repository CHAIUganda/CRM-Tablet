package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class ParishEntity {
    public static Entity createParishEntity(Schema schema,Entity subcounty){
        Entity parish = schema.addEntity("Parish");
        parish.addStringProperty("uuid").notNull().unique().primaryKey();
        parish.addStringProperty("name").notNull();

        Property subcountyId = parish.addStringProperty("subCountyId").notNull().getProperty();
        ToMany subcountyToParishes = subcounty.addToMany(parish,subcountyId);
        subcountyToParishes.setName("parishes");

        parish.addToOne(subcounty,subcountyId);

        return parish;
    }
}
