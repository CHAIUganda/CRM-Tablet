package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class SaleEntity {
    public static Entity creatSaleEntity(Schema schema,Entity order,Entity task){
        Entity sale = schema.addEntity("Sale");
        sale.addStringProperty("uuid").notNull().unique().primaryKey();
        Property dateOfSale = sale.addDateProperty("dateOfSale").notNull().getProperty();
        sale.addBooleanProperty("doYouStockOrsZinc");
        sale.addStringProperty("ifNoWhy");
        sale.addStringProperty("pointOfsaleMaterial");
        sale.addStringProperty("recommendationNextStep");
        sale.addStringProperty("governmentApproval");
        sale.addBooleanProperty("isHistory");
        sale.addDoubleProperty("latitude");
        sale.addDoubleProperty("longitude");

        Property orderRefid = sale.addStringProperty("orderId").notNull().getProperty();
        ToMany orderToSales = order.addToMany(sale,orderRefid);
        orderToSales.setName("sales");
        orderToSales.orderAsc(dateOfSale);

        sale.addToOne(order,orderRefid);

        Property taskId = sale.addStringProperty("taskId").notNull().getProperty();
        ToMany taskToSales = task.addToMany(sale,taskId);
        taskToSales.setName("sales");
        taskToSales.orderAsc(dateOfSale);
        sale.addToOne(task,taskId);

        sale.addBooleanProperty("isDirty");
        sale.addIntProperty("syncronisationStatus");
        sale.addStringProperty("syncronisationMessage");
        sale.addDateProperty("dateCreated");
        sale.addDateProperty("lastUpdated");
        sale.implementsInterface("BaseEntity");

        return sale;
    }
}
