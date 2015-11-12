package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Zed on 11/12/2015.
 */
public class ProductGroup {
    public static Entity createProductGroupEntity(Schema schema){
        Entity group = schema.addEntity("ProductGroup");
        group.addStringProperty("id").notNull().unique().primaryKey();
        group.addStringProperty("name").notNull();

        return group;
    }
}
