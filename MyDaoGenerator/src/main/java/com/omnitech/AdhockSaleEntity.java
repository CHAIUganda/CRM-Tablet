package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 1/8/15.
 */
public class AdhockSaleEntity {

    public static Entity creatAdhockSaleEntity(Schema schema,Entity customer){
        Entity sale = schema.addEntity("AdhockSale");
        sale.addStringProperty("uuid").notNull().unique().primaryKey();
        Property dateOfSale = sale.addDateProperty("dateOfSale").notNull().getProperty();
        sale.addBooleanProperty("doYouStockOrsZinc");
        sale.addStringProperty("ifNoWhy");
        sale.addStringProperty("pointOfsaleMaterial");
        sale.addStringProperty("recommendationNextStep");
        sale.addStringProperty("governmentApproval");
        sale.addDoubleProperty("latitude");
        sale.addDoubleProperty("longitude");
        sale.addBooleanProperty("isHistory");

        Property customerId = sale.addStringProperty("customerId").notNull().getProperty();
        ToMany customerToAdhockSales = customer.addToMany(sale,customerId);
        customerToAdhockSales.setName("adhockSales");
        customerToAdhockSales.orderAsc(dateOfSale);

        sale.addToOne(customer,customerId);


        sale.addBooleanProperty("isDirty");
        sale.addIntProperty("syncronisationStatus");
        sale.addStringProperty("syncronisationMessage");
        sale.addDateProperty("dateCreated");
        sale.addDateProperty("lastUpdated");
        sale.implementsInterface("BaseEntity");

        return sale;
    }
}
