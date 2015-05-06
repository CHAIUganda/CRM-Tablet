package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by victor on 10/13/14.
 */
public class RegionEntity {
    public static Entity createRegionEntity(Schema schema){
        Entity region = schema.addEntity("Region");
        region.addStringProperty("uuid").notNull().unique().primaryKey();
        region.addStringProperty("name").notNull();
        return region;
    }
}
