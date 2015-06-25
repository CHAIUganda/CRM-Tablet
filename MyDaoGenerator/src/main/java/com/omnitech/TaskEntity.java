package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 10/13/14.
 */
public class TaskEntity {
    public static Entity createTaskEntity(Schema schema,Entity customer){
        Entity task = schema.addEntity("Task");
        task.addStringProperty("uuid").notNull().unique().primaryKey();
        task.addStringProperty("description");
        task.addStringProperty("status");
        task.addStringProperty("priority");
        task.addStringProperty("type");
        task.addDateProperty("dueDate");
        task.addDateProperty("completionDate");

        task.addDateProperty("dateScheduled");
        task.addBooleanProperty("isAdhock");

        Property customerId = task.addStringProperty("customerId").notNull().getProperty();
        ToMany customerToTasks = customer.addToMany(task, customerId);
        customerToTasks.setName("tasks");

        task.addToOne(customer,customerId);

        task.addBooleanProperty("isDirty");
        task.addIntProperty("syncronisationStatus");
        task.addStringProperty("syncronisationMessage");
        task.addDateProperty("dateCreated");
        task.addDateProperty("lastUpdated");
        task.implementsInterface("BaseEntity");
        return task;

    }

    public static void createTaskOrderEntity(Schema schema,Entity task, Entity product){
        Entity taskOrder = schema.addEntity("TaskOrder");
        taskOrder.addIdProperty();
        taskOrder.addStringProperty("quantity");
        taskOrder.addStringProperty("unitPrice");

        Property taskId = taskOrder.addStringProperty("taskId").notNull().getProperty();
        ToMany taskToTaskOrders = task.addToMany(taskOrder, taskId);
        taskToTaskOrders.setName("lineItems");

        Property productId = taskOrder.addStringProperty("productId").notNull().getProperty();
        ToMany productToTaskOrders = product.addToMany(taskOrder,productId);
        productToTaskOrders.setName("taskOrderproducts");

        taskOrder.addToOne(task,taskId);
        taskOrder.addToOne(product,productId);

        taskOrder.addBooleanProperty("isDirty");
        taskOrder.addIntProperty("syncronisationStatus");
        taskOrder.addStringProperty("syncronisationMessage");
        taskOrder.addDateProperty("dateCreated");
        taskOrder.addDateProperty("lastUpdated");
        taskOrder.implementsInterface("BaseEntity");

    }
}
