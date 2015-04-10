package org.chai;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.chai.model.*;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 08-Apr-15.
 */
@RunWith(RobolectricTestRunner.class)
public class SyncronisationTest {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private RegionDao regionDao;
    private DistrictDao districtDao;
    private SubcountyDao subcountyDao;
    private CustomerDao customerDao;
    private TaskDao taskDao;
    private DetailerCallDao detailerCallDao;
    private SaleDao saleDao;
    private SaleDataDao saleDataDao;
    private OrderDao orderDao;
    private AdhockSaleDao adhockSaleDao;
    Region region;
    District district;
    Subcounty subcounty;
    Customer customer;
    AdhockSale adhockSale;
    Sale sale;
    Task task;
    Order order;
    DetailerCall detailerCall;

    @Before
    public void setup(){
        initialiseGreenDao();
        region = new Region(UUID.randomUUID().toString(),"Test Region");
        regionDao.insert(region);
        district = new District(UUID.randomUUID().toString(),"Test District",region.getUuid());
        districtDao.insert(district);
        subcounty = new Subcounty(UUID.randomUUID().toString(),"Test Subcounty",district.getUuid());
        subcountyDao.insert(subcounty);
        customer = setUpCustomer(subcounty);
        customerDao.insert(customer);
    }
    @Test
    public void test_db_has_data(){
        List<Customer> customers = customerDao.loadAll();
        assertEquals(1,customers.size());
    }




    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getInMemoryDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            regionDao = daoSession.getRegionDao();
            districtDao = daoSession.getDistrictDao();
            subcountyDao = daoSession.getSubcountyDao();
            customerDao = daoSession.getCustomerDao();
            taskDao = daoSession.getTaskDao();
            detailerCallDao = daoSession.getDetailerCallDao();
            saleDao = daoSession.getSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            orderDao = daoSession.getOrderDao();
            adhockSaleDao = daoSession.getAdhockSaleDao();
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
        }
    }

    private Customer setUpCustomer(Subcounty subcounty){
       Customer customer = new Customer(UUID.randomUUID().toString());
        customer.setLatitude(0.31234535);
        customer.setLongitude(31.09530835);
        customer.setOutletName("Test Outlet");
        customer.setSubcountyId(subcounty.getUuid());
        return customer;
    }
    private AdhockSale setAdhockSale(Customer customer){
        AdhockSale adhockSale = new AdhockSale(UUID.randomUUID().toString());
        adhockSale.setDateOfSale(new Date());
        adhockSale.setCustomerId(customer.getUuid());

        return adhockSale;
    }
    private Sale setUpSale(){
        Sale sale = new Sale(UUID.randomUUID().toString());
        sale.setDateOfSale(new Date());
        sale.setOrderId("0f48f620-b15b-4df3-a0ef-3462c665a17a");

        return sale;
    }
    private Task setUpTask(Customer customer){
        Task task = new Task(UUID.randomUUID().toString());
        task.setCustomerId(customer.getUuid());
        return task;
    }
    private Order setUpOrder(Customer customer){
        Order order = new Order(UUID.randomUUID().toString());
        order.setDeliveryDate(new Date());
        order.setOrderDate(new Date());
        order.setCustomerId(customer.getUuid());
        return order;
    }
    private DetailerCall setUpDetailerCall(Task task){
        DetailerCall detailerCall = new DetailerCall(UUID.randomUUID().toString());
        detailerCall.setTaskId(task.getUuid());

        return detailerCall;
    }
}
