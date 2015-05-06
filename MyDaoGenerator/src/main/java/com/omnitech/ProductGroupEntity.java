package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by victor on 12/31/14.
 */
public class ProductGroupEntity {
    public static Entity createProductGroupEntity(Schema schema){
        Entity productGroup = schema.addEntity("productGroup");
        productGroup.addStringProperty("uuid").notNull().unique().primaryKey();
        productGroup.addBooleanProperty("name");

        return productGroup;
    }
}
