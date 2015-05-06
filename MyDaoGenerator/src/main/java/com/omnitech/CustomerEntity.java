package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class CustomerEntity {

    public static Entity createCustomerEntity(Schema schema,Entity subcounty){
        Entity customer = schema.addEntity("Customer");
        customer.addStringProperty("uuid").notNull().unique().primaryKey();
        customer.addDoubleProperty("latitude");
        customer.addDoubleProperty("longitude");
        customer.addStringProperty("outletName").notNull();
        customer.addStringProperty("outletType");
        customer.addStringProperty("outletSize");
        customer.addByteArrayProperty("outletPicture");
        customer.addStringProperty("split");
        customer.addStringProperty("majoritySourceOfSupply");
        customer.addStringProperty("keyWholeSalerName");
        customer.addStringProperty("keyWholeSalerContact");
        customer.addBooleanProperty("licenceVisible");
        customer.addStringProperty("typeOfLicence");
        customer.addStringProperty("descriptionOfOutletLocation");

        customer.addIntProperty("numberOfEmployees");
        customer.addIntProperty("numberOfCustomersPerDay");
        customer.addStringProperty("restockFrequency");

        customer.addStringProperty("lengthOpen");
        customer.addStringProperty("tradingCenter");
        customer.addStringProperty("subcountyUuid");
        customer.addBooleanProperty("isActive");
        customer.addStringProperty("segment");

        Property subcountyId = customer.addStringProperty("subcountyId").notNull().getProperty();
        ToMany subcountyToCustomers = subcounty.addToMany(customer,subcountyId);
        subcountyToCustomers.setName("customers");

        customer.addToOne(subcounty, subcountyId);

        customer.addBooleanProperty("isDirty");
        customer.addIntProperty("syncronisationStatus");
        customer.addStringProperty("syncronisationMessage");
        customer.addDateProperty("dateCreated");
        customer.addDateProperty("lastUpdated");
        customer.implementsInterface("BaseEntity");

        return customer;
    }
}
