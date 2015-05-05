package org.chai.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;

import org.chai.activities.tasks.TaskMainFragment;
import org.chai.model.AdhockSale;
import org.chai.model.AdhockSaleDao;
import org.chai.model.BaseEntity;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.CustomerContactDao;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerCall;
import org.chai.model.DetailerCallDao;
import org.chai.model.District;
import org.chai.model.DistrictDao;
import org.chai.model.Order;
import org.chai.model.OrderDao;
import org.chai.model.ParishDao;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.model.Region;
import org.chai.model.RegionDao;
import org.chai.model.Sale;
import org.chai.model.SaleDao;
import org.chai.model.SaleDataDao;
import org.chai.model.Subcounty;
import org.chai.model.SubcountyDao;
import org.chai.model.SummaryReport;
import org.chai.model.SummaryReportDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.model.TaskOrder;
import org.chai.model.TaskOrderDao;
import org.chai.model.User;
import org.chai.model.VillageDao;
import org.chai.rest.CustomerClient;
import org.chai.rest.Place;
import org.chai.rest.ProductClient;
import org.chai.rest.RestClient;
import org.chai.rest.SalesClient;
import org.chai.rest.TaskClient;
import org.chai.util.MyApplication;
import org.chai.util.ServerResponse;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 11/3/14.
 */
public class CHAISynchroniser extends Service {

    private Place place;
    private CustomerClient customerClient;
    private ProductClient productClient;
    private TaskClient taskClient;
    private SalesClient salesClient;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private RegionDao regionDao;
    private DistrictDao districtDao;
    private SubcountyDao subcountyDao;
    private ParishDao parishDao;
    private VillageDao villageDao;
    private CustomerDao customerDao;
    private CustomerContactDao customerContactDao;
    private TaskDao taskDao;
    private DetailerCallDao detailerCallDao;
    private ProductDao productDao;
    private SaleDao saleDao;
    private SaleDataDao saleDataDao;
    private OrderDao orderDao;
    private AdhockSaleDao adhockSaleDao;
    private SummaryReportDao summaryReportDao;
    private TaskOrderDao taskOrderDao;
    private List<ServerResponse> syncronisationErros;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.log("Starting sync service");
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        salesClient = new SalesClient();

        initialiseGreenDao();

        //Sync everything here
        new SyncTask().execute();

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Utils.log("Destroying service and setting alarm");
        // Restart every 1 hour
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60),
                PendingIntent.getService(this, 0, new Intent(this, CHAISynchroniser.class), 0)
        );
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            regionDao = daoSession.getRegionDao();
            districtDao = daoSession.getDistrictDao();
            subcountyDao = daoSession.getSubcountyDao();
            parishDao = daoSession.getParishDao();
            villageDao = daoSession.getVillageDao();
            customerDao = daoSession.getCustomerDao();
            customerContactDao = daoSession.getCustomerContactDao();
            taskDao = daoSession.getTaskDao();
            detailerCallDao = daoSession.getDetailerCallDao();
            productDao = daoSession.getProductDao();
            saleDao = daoSession.getSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            orderDao = daoSession.getOrderDao();
            adhockSaleDao = daoSession.getAdhockSaleDao();
            summaryReportDao = daoSession.getSummaryReportDao();
            taskOrderDao = daoSession.getTaskOrderDao();
        } catch (Exception ex) {
            Utils.log("Error initializing greendao for sync");
        }
    }

    public void startSyncronisationProcess() {
        try{
            syncronisationErros = new ArrayList<ServerResponse>();
            uploadCustomers();
            uploadDirectSales();
            uploadSales();
            uploadTasks();
            uploadOrders();
            regionDao.deleteAll();
            districtDao.deleteAll();
            subcountyDao.deleteAll();

            downloadRegions();
            downloadCustomers();
            downloadTasks();
            downloadProducts();
            downloadSummaryReports();
        }catch(Exception ex){
            Utils.log("Error syncing -> " + ex.getMessage());
        }
        if(!syncronisationErros.isEmpty()){
            //displaySyncErros(syncronisationErros);
        }
    }

    public void downloadRegions() {
        updatePropgress("Downloading Regions...");
        Region[] regions = place.downloadRegions();
        if (regions != null) {
            for (Region region : regions) {
                regionDao.insert(region);
            }
            downloadDistricts();
        }
    }

    public void downloadDistricts() {
        updatePropgress("Downloading Districts...");
        District[] districts = place.downloadDistricts();
        for (District district : districts) {
            districtDao.insert(district);
        }
        downloadSubcounties();
    }

    public void downloadSubcounties() {
        updatePropgress("Downloading Subcounties...");
        Subcounty[] subcounties = place.downloadSubcounties();
        for (Subcounty subcounty : subcounties) {
            subcountyDao.insert(subcounty);
        }
    }

    public void downloadCustomers() {
        updatePropgress("Downloading Customers..");
        Customer[] customers = customerClient.downloadCustomers();
        customerDao.insertOrReplaceInTx(customers);
        List<CustomerContact> contacts = new ArrayList<CustomerContact>();
        for (Customer customer : customers) {
            List<CustomerContact> customerContacts = customer.getCustomerContacts();
            if (customerContacts != null) {
                for (CustomerContact cc : customerContacts) {
                    cc.setCustomerId(customer.getUuid());
                    cc.setCustomer(customer);
                    contacts.add(cc);
                }
            }
        }
        customerContactDao.insertOrReplaceInTx(contacts);
    }

    public void downloadTasks() {
        updatePropgress("Downloading Tasks..");

        Task[] tasks = taskClient.downloadTasks();
        taskDao.insertOrReplaceInTx(tasks);
        List<TaskOrder> taskOrders = new ArrayList<TaskOrder>();
        for (Task task : tasks) {
            List<TaskOrder> lineItems = task.getLineItems();
            if (lineItems != null) {
                for (TaskOrder order : lineItems) {
                    order.setTaskId(task.getUuid());
                    order.setTask(task);
                    taskOrders.add(order);
                }
            }

        }
        taskOrderDao.insertOrReplaceInTx(taskOrders);
    }

    public void uploadTasks() /*throws SyncronizationException*/ {
        List<Task> taskList = taskDao.queryBuilder().where(TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_NEW)).list();

        if (!taskList.isEmpty()) {
            updatePropgress("Uploading Tasks..");
        }else{
            Utils.log("Tasks list is empty");
        }

        for (Task task : taskList) {
            Utils.log("Syncrosnizing task -> " + task.getCustomerId());
            if (taskIsHistory(task)) {
                continue;
            }
            ServerResponse response = taskClient.uploadTask(task);
            Utils.log("After server response");
            if (response.getStatus().equalsIgnoreCase("OK")) {
                Utils.log("Task syncronized succesfully");
                //set all detailer and sale calls to isHistroy
                if (RestClient.role.equalsIgnoreCase(User.ROLE_DETAILER)) {
                    DetailerCall detailerCall = task.getDetailers().get(0);
                    detailerCall.setIsHistory(true);
                    detailerCall.setIsDirty(false);
                    detailerCall.setSyncronisationStatus(BaseEntity.SYNC_SUCCESS);
                    detailerCallDao.update(detailerCall);
                }
            }else{
                Utils.log("Error posting Tasks");
                task.setSyncronisationStatus(BaseEntity.SYNC_FAIL);
                task.setSyncronisationMessage(response.getMessage());
                task.setLastUpdated(new Date());
                task.setIsDirty(true);
                taskDao.update(task);
                syncronisationErros.add(response);
            }
            Utils.log("After the sync");
        }
    }

    private boolean taskIsHistory(Task task) {
        try {
            if (!task.getDetailers().isEmpty() && task.getDetailers().get(0).getIsHistory()) {
                return true;
            } else if (!task.getSales().isEmpty() && task.getSales().get(0).getIsHistory()) {
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public void uploadCustomers() /*throws SyncronizationException*/{
        List<Customer> customersList = customerDao.queryBuilder().where(CustomerDao.Properties.IsDirty.eq(true)).list();
        if (!customersList.isEmpty()) {
            updatePropgress("Uploading Customers...");
        }else{
            Utils.log("No customers");
        }
        for (Customer customer : customersList) {
            ServerResponse response = customerClient.uploadCustomer(customer,RestClient.getRestTemplate());
            if (response.getStatus().equalsIgnoreCase("OK")) {
                customer.setIsDirty(false);
                customerDao.update(customer);
            }else{
                customer.setSyncronisationStatus(BaseEntity.SYNC_FAIL);
                customer.setSyncronisationMessage(response.getMessage());
                customer.setLastUpdated(new Date());
                customer.setIsDirty(true);
                customerDao.update(customer);
                syncronisationErros.add(response);
            }
        }
    }

    private void downloadProducts() {
        productDao.deleteAll();
        Product[] products = productClient.downloadProducts();
        for (Product product : products) {
            productDao.insert(product);
        }
    }

    private void uploadSales() /*throws SyncronizationException*/{
        List<Sale> saleList = saleDao.queryBuilder().where(SaleDao.Properties.IsHistory.notEq(true)).list();
        if (!saleList.isEmpty()) {
            updatePropgress("Uploading Sales...");
        }
        for (Sale sale : saleList) {
            ServerResponse response = salesClient.uploadSale(sale);
            if (response.getStatus().equalsIgnoreCase("OK")) {
                sale.setIsHistory(true);
                sale.setSyncronisationStatus(BaseEntity.SYNC_SUCCESS);
                sale.setIsDirty(false);
                saleDao.update(sale);
            }else{
                sale.setSyncronisationStatus(BaseEntity.SYNC_FAIL);
                sale.setSyncronisationMessage(response.getMessage());
                sale.setLastUpdated(new Date());
                sale.setIsDirty(true);
                saleDao.update(sale);
                syncronisationErros.add(response);
            }
        }
    }

    private void uploadDirectSales() /*throws SyncronizationException*/{
        List<AdhockSale> saleList = adhockSaleDao.loadAll();
        if (!saleList.isEmpty()) {
            updatePropgress("Uploading Sales...");
        }
        for (AdhockSale adhockSale : saleList) {
            if(adhockSale.getIsHistory()){
                continue;
            }
            updatePropgress("Uploading Sales...");
            ServerResponse response = salesClient.uploadDirectSale(adhockSale);
            if (response.getStatus().equalsIgnoreCase("OK")) {
                adhockSale.setIsHistory(true);
                adhockSale.setIsDirty(false);
                adhockSale.setSyncronisationStatus(BaseEntity.SYNC_SUCCESS);
                adhockSaleDao.update(adhockSale);
            }else{
                adhockSale.setSyncronisationStatus(BaseEntity.SYNC_FAIL);
                adhockSale.setSyncronisationMessage(response.getMessage());
                adhockSale.setLastUpdated(new Date());
                adhockSale.setIsDirty(true);
                adhockSaleDao.update(adhockSale);
                syncronisationErros.add(response);
            }
        }
    }

    private void uploadOrders() /*throws SyncronizationException*/{
        List<Order> orderList = orderDao.loadAll();
        if (!orderList.isEmpty()) {
            updatePropgress("Uploading Orders...");
        }
        for (Order order : orderList) {
            updatePropgress("Uploading Orders...");
            ServerResponse response = salesClient.uploadOrder(order);
            if (response.getStatus().equalsIgnoreCase("OK")) {
                orderDao.delete(order);
            }else{
                order.setSyncronisationStatus(BaseEntity.SYNC_FAIL);
                order.setSyncronisationMessage(response.getMessage());
                order.setLastUpdated(new Date());
                order.setIsDirty(true);
                orderDao.update(order);
                syncronisationErros.add(response);
            }
        }
    }

    private void downloadSummaryReports() {
        summaryReportDao.deleteAll();
        SummaryReport[] summaryReports = place.getSummaryReports();
        for (SummaryReport summaryReport : summaryReports) {
            summaryReportDao.insert(summaryReport);
        }
    }

    private void updatePropgress(final String message) {
        Utils.log(message);
    }

    private void displayError(final String message){
        Utils.log("Error -> " + message);
        /*parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(parent.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void displaySyncErros(final List<ServerResponse> errors){
        /*parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder formartedErrors = new StringBuilder();
                formartedErrors.append("The following errors were encountered during syncronisation process\n");
                for(ServerResponse response:errors){
                    String message = response.getMessage();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Map<String, Object> mapObject = mapper.readValue(message,new TypeReference<Map<String, Object>>() {
                        });
                        formartedErrors.append(response.getItemRef()+":"+mapObject.get("message") +"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                new AlertDialog.Builder(parent)
                        .setTitle("Errors:")
                        .setMessage(formartedErrors.toString())
                        .setPositiveButton("ok", null)
                        .show();
            }
        });*/
    }

    private class SyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 5; i++) {
                startSyncronisationProcess();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}