package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class CustomerContact {

    public static void createCustomercontactEntity(Schema schema, Entity customer){
        Entity contact = schema.addEntity("CustomerContact");
        contact.addStringProperty("uuid").notNull().unique().primaryKey();
        contact.addStringProperty("contact");
        contact.addStringProperty("names");
        contact.addStringProperty("gender");
        contact.addStringProperty("role");

        Property customerId = contact.addStringProperty("customerId").notNull().getProperty();
        ToMany customerToContacts = customer.addToMany(contact, customerId);
        customerToContacts.setName("customerContacts");

        contact.addToOne(customer, customerId);


        contact.addBooleanProperty("isDirty");
        contact.addIntProperty("syncronisationStatus");
        contact.addStringProperty("syncronisationMessage");
        contact.addDateProperty("dateCreated");
        contact.addDateProperty("lastUpdated");
        contact.implementsInterface("BaseEntity");
    }
}
