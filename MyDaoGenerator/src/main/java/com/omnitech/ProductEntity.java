package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class ProductEntity {
    public static Entity createProductEntity(Schema schema, Entity group){
        Entity product = schema.addEntity("Product");

        product.addStringProperty("uuid").notNull().unique().primaryKey();
        product.addStringProperty("name").notNull();
        product.addStringProperty("unitOfMeasure");
        product.addStringProperty("formulation");
        product.addStringProperty("unitPrice");
        product.addStringProperty("groupName");

        Property groupId = product.addStringProperty("groupId").notNull().getProperty();
        ToMany groupToProducts = group.addToMany(product, groupId);
        groupToProducts.setName("products");

        product.addToOne(group, groupId);

        return product;

    }
}
