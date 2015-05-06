package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by victor on 10/14/14.
 */
public class UserEntity {
    public static void createUserEntity(Schema schema){
        Entity user = schema.addEntity("User");
        user.addStringProperty("uuid").notNull().unique().primaryKey();
        user.addStringProperty("userName").notNull();
        user.addStringProperty("password").notNull();
        user.addBooleanProperty("enabled").notNull();
        user.addBooleanProperty("accountexpired").notNull();
        user.addBooleanProperty("accountlocked").notNull();
        user.addBooleanProperty("passwordexpired");
        user.addStringProperty("role");
    }
}
