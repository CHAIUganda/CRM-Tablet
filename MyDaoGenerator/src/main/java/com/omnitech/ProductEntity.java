package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by victor on 10/13/14.
 */
public class ProductEntity {
    public static Entity createProductEntity(Schema schema){
        Entity product = schema.addEntity("Product");

        product.addStringProperty("uuid").notNull().unique().primaryKey();
        product.addStringProperty("name").notNull();
        product.addStringProperty("unitOfMeasure");
        product.addStringProperty("formulation");
        product.addStringProperty("unitPrice");
        product.addStringProperty("groupName");
        product.addIntProperty("groupId");

        return product;

    }
}
