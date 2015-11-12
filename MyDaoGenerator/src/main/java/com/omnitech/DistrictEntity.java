package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class DistrictEntity {
    public static Entity createDistrictEntity(Schema schema, Entity region){
        Entity district = schema.addEntity("District");
        district.addStringProperty("uuid").notNull().unique().primaryKey();
        district.addStringProperty("name").notNull();

        Property regionId = district.addStringProperty("regionId").notNull().getProperty();
        ToMany regionToDistricts = region.addToMany(district,regionId);
        regionToDistricts.setName("districts");

        district.addToOne(region,regionId);

        return district;
    }
}
