package com.omnitech;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by victor on 10/12/14.
 */
public class ChaiAndroidDaoGenerator{
    private static final int DATABASE_VERSION = 1;

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(5, "org.chai.model");
        schema.setDefaultJavaPackageDao("org.chai.model");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();

        Entity region = RegionEntity.createRegionEntity(schema);
        Entity district = DistrictEntity.createDistrictEntity(schema, region);
        Entity subcounty = SubcountyEntity.createSubcountyEntity(schema, district);
        Entity parish = ParishEntity.createParishEntity(schema, subcounty);
        Entity village = VillageEntity.createVillageEntity(schema, parish);

        Entity customer = CustomerEntity.createCustomerEntity(schema, subcounty);
        CustomerContact.createCustomercontactEntity(schema, customer);

        Entity productGroup = ProductGroup.createProductGroupEntity(schema);

        Entity product = ProductEntity.createProductEntity(schema, productGroup);
        Entity order = OrderEntity.createOrderEntity(schema, customer, product);
        Entity task = TaskEntity.createTaskEntity(schema, customer);
        TaskEntity.createTaskOrderEntity(schema, task, product);

        Entity promotion = PromotionEntity.createPromotionEntity(schema, product);
        PromotionalItemEntity.createPromotionalItemEntity(schema, promotion);

        Entity saleEntity = SaleEntity.creatSaleEntity(schema, order, task);
        UserEntity.createUserEntity(schema);

        Entity detailerEntity = DetailerCall.createDetailerCallEntity(schema, task);
        Entity malariaDetailEntity = MalariaDetailEntity.createMalariaDetailEntity(schema, task);

        OrderData.createOrderDataEntity(schema, order, product);

        Entity adhockSaleData = AdhockSaleEntity.creatAdhockSaleEntity(schema, customer);

        SaleData.creatSaleDataEntity(schema, saleEntity, adhockSaleData, product);
        StockData.creatStockDataEntity(schema, saleEntity, adhockSaleData, product);

        DetailerStock.createDetailerStockEntity(schema, detailerEntity, malariaDetailEntity);
        SummaryReport.createSummaryReport(schema);

        System.out.println("Destination -> " + args[0]);

        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(schema, args[0]);
    }
}
