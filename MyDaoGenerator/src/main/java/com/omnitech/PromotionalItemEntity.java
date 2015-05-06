package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class PromotionalItemEntity {
    public static void createPromotionalItemEntity(Schema schema,Entity promotion){
        Entity item = schema.addEntity("PromotionalItem");
        item.addStringProperty("uuid").notNull().unique().primaryKey();
        item.addStringProperty("name").notNull();

        Property promotionId = item.addStringProperty("promotionId").notNull().getProperty();
        ToMany promotionToItems = promotion.addToMany(item,promotionId);
        promotionToItems.setName("items");

        item.addToOne(promotion,promotionId);
    }
}
