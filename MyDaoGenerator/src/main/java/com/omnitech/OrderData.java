package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 1/5/15.
 */
public class OrderData {

    public static void createOrderDataEntity(Schema schema,Entity order,Entity product){
        Entity orderData = schema.addEntity("OrderData");
        orderData.addStringProperty("uuid").notNull().unique().primaryKey();
        orderData.addIntProperty("quantity").notNull();
        orderData.addIntProperty("price").notNull();
        orderData.addBooleanProperty("dropSample");

        Property orderId = orderData.addStringProperty("orderId").notNull().getProperty();
        ToMany orderDataToOrders = order.addToMany(orderData,orderId);
        orderDataToOrders.setName("orderDatas");

        Property productRefId = orderData.addStringProperty("productId").notNull().getProperty();
        ToMany productToOrderData = product.addToMany(orderData,productRefId);
        productToOrderData.setName("orderDatas");

        orderData.addToOne(order,orderId);
        orderData.addToOne(product,productRefId);

        orderData.addBooleanProperty("isDirty");
        orderData.addIntProperty("syncronisationStatus");
        orderData.addStringProperty("syncronisationMessage");
        orderData.addDateProperty("dateCreated");
        orderData.addDateProperty("lastUpdated");
        orderData.implementsInterface("BaseEntity");

    }
}
