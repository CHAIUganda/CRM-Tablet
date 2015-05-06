package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by victor on 2/6/15.
 */
public class StockData {

    public static void creatStockDataEntity(Schema schema,Entity sale,Entity adhockSale,Entity product){
        Entity stockData = schema.addEntity("StokeData");
        stockData.addStringProperty("uuid").notNull().unique().primaryKey();
        stockData.addIntProperty("quantity").notNull();

        Property saleId = stockData.addStringProperty("saleId").getProperty();
        ToMany stockDataToSales = sale.addToMany(stockData,saleId);
        stockDataToSales.setName("stockDatas");

        Property adhockSaleId = stockData.addStringProperty("adhockStockId").getProperty();
        ToMany adhockStockDataToSales = adhockSale.addToMany(stockData,adhockSaleId);
        adhockStockDataToSales.setName("adhockStockDatas");

        Property productRefId = stockData.addStringProperty("productId").notNull().getProperty();
        ToMany productToStockData = product.addToMany(stockData, productRefId);
        productToStockData.setName("stockDatas");

        stockData.addToOne(sale,saleId);
        stockData.addToOne(product, productRefId);
        stockData.addToOne(adhockSale, adhockSaleId);


        stockData.addBooleanProperty("isDirty");
        stockData.addIntProperty("syncronisationStatus");
        stockData.addStringProperty("syncronisationMessage");
        stockData.addDateProperty("dateCreated");
        stockData.addDateProperty("lastUpdated");
        stockData.implementsInterface("BaseEntity");
    }
}
