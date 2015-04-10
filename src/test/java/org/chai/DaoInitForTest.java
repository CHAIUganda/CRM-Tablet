package org.chai;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.chai.model.*;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.Date;
import java.util.UUID;

/**
 * Created by victor on 10-Apr-15.
 */
public class DaoInitForTest {
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

    public DaoInitForTest() {
        initialiseGreenDao();
    }

    public void initialiseGreenDao() {
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

    public Customer setUpCustomer(String subcountyId){
        Customer customer = new Customer(UUID.randomUUID().toString());
        customer.setLatitude(0.31234535);
        customer.setLongitude(31.09530835);
        customer.setOutletName("Test Outlet");
        customer.setSubcountyId(subcountyId);
        CustomerContact customerContact = new CustomerContact(UUID.randomUUID().toString());
        customerContact.setCustomerId(customer.getUuid());
        daoSession.insert(customerContact);
        daoSession.insert(customer);
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
