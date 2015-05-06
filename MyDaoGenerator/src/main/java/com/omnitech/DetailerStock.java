package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 2/9/15.
 */
public class DetailerStock {

    public static void createDetailerStockEntity(Schema schema, Entity detailer, Entity malariadetail){

        Entity detailerStock = schema.addEntity("DetailerStock");
        detailerStock.addStringProperty("uuid").notNull().unique().primaryKey();
        detailerStock.addStringProperty("brand").notNull();
        detailerStock.addStringProperty("category").notNull();
        detailerStock.addDoubleProperty("stockLevel").notNull();
        detailerStock.addDoubleProperty("buyingPrice");
        detailerStock.addDoubleProperty("sellingPrice");

        Property detailerId = detailerStock.addStringProperty("detailerId").notNull().getProperty();
        Property malariaDetailId = malariadetail.addStringProperty("malariadetailId").notNull().getProperty();

        ToMany detailerStockToDetailer = detailer.addToMany(detailerStock, detailerId);
        detailerStockToDetailer.setName("detailerStocks");
        detailerStock.addToOne(detailer, detailerId);

        ToMany detailerStockToMalariaDetail = detailer.addToMany(detailerStock, malariaDetailId);
        detailerStockToMalariaDetail.setName("detailerMalariaStocks");
        detailerStock.addToOne(malariadetail, malariaDetailId);

        detailerStock.addBooleanProperty("isDirty");
        detailerStock.addIntProperty("syncronisationStatus");
        detailerStock.addStringProperty("syncronisationMessage");
        detailerStock.addDateProperty("dateCreated");
        detailerStock.addDateProperty("lastUpdated");
        detailerStock.implementsInterface("BaseEntity");
    }
}
