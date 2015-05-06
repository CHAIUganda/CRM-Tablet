package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class OrderEntity {
    public static Entity createOrderEntity(Schema schema, Entity customer, Entity product) {
        Entity order = schema.addEntity("Order");
        order.setTableName("orders");
        order.addStringProperty("uuid").notNull().unique().primaryKey();
        order.addDateProperty("deliveryDate").notNull();
        Property orderDate = order.addDateProperty("orderDate").notNull().getProperty();

        Property customerRefId = order.addStringProperty("customerId").notNull().getProperty();
        ToMany customerToOrders = customer.addToMany(order, customerRefId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);

        order.addToOne(customer,customerRefId);

        order.addBooleanProperty("isDirty");
        order.addIntProperty("syncronisationStatus");
        order.addStringProperty("syncronisationMessage");
        order.addDateProperty("dateCreated");
        order.addDateProperty("lastUpdated");
        order.implementsInterface("BaseEntity");

        return order;
    }
}
