package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 12/23/14.
 */
public class SaleData {

    public static void creatSaleDataEntity(Schema schema,Entity sale,Entity adhockSale,Entity product){
        Entity saleData = schema.addEntity("SaleData");
        saleData.addStringProperty("uuid").notNull().unique().primaryKey();
        saleData.addIntProperty("quantity").notNull();
        saleData.addIntProperty("price").notNull();

        Property saleId = saleData.addStringProperty("saleId").getProperty();
        ToMany saleDataToSales = sale.addToMany(saleData,saleId);
        saleDataToSales.setName("salesDatas");

        Property adhockSaleId = saleData.addStringProperty("adhockSaleId").getProperty();
        ToMany adhockSaleDataToSales = adhockSale.addToMany(saleData,adhockSaleId);
        adhockSaleDataToSales.setName("adhockSalesDatas");

        Property productRefId = saleData.addStringProperty("productId").notNull().getProperty();
        ToMany productToSalesData = product.addToMany(saleData,productRefId);
        productToSalesData.setName("salesDatas");

        saleData.addToOne(sale,saleId);
        saleData.addToOne(product,productRefId);
        saleData.addToOne(adhockSale,adhockSaleId);


        saleData.addBooleanProperty("isDirty");
        saleData.addIntProperty("syncronisationStatus");
        saleData.addStringProperty("syncronisationMessage");
        saleData.addDateProperty("dateCreated");
        saleData.addDateProperty("lastUpdated");
        saleData.implementsInterface("BaseEntity");
    }
}
