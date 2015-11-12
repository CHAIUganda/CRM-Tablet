package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class PromotionEntity {
    public static Entity createPromotionEntity(Schema schema, Entity product){
        Entity promotion = schema.addEntity("Promotion");
        promotion.addStringProperty("uuid").notNull().unique().primaryKey();
        promotion.addStringProperty("description").notNull();
        promotion.addDateProperty("startDate").notNull();
        promotion.addDateProperty("stopDate").notNull();

        Property productId = promotion.addStringProperty("productId").notNull().getProperty();
        ToMany productToPromotions = product.addToMany(promotion,productId);
        productToPromotions.setName("promotions");

        promotion.addToOne(product,productId);
        return promotion;
    }
}
