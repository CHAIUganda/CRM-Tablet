package org.chai.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.chai.activities.HomeActivity;
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
import org.chai.model.MalariaDetail;
import org.chai.model.MalariaDetailDao;
import org.chai.model.Order;
import org.chai.model.OrderDao;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.model.Region;
import org.chai.model.RegionDao;
import org.chai.model.Sale;
import org.chai.model.SaleDao;
import org.chai.model.Subcounty;
import org.chai.model.SubcountyDao;
import org.chai.model.SummaryReport;
import org.chai.model.SummaryReportDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.model.TaskOrder;
import org.chai.model.TaskOrderDao;
import org.chai.model.User;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private CustomerDao customerDao;
    private CustomerContactDao customerContactDao;
    private TaskDao taskDao;
    private DetailerCallDao detailerCallDao;
    private MalariaDetailDao malariaDetailDao;
    private ProductDao productDao;
    private SaleDao saleDao;
    private OrderDao orderDao;
    private AdhockSaleDao adhockSaleDao;
    private SummaryReportDao summaryReportDao;
    private TaskOrderDao taskOrderDao;
    private List<ServerResponse> syncronisationErros;

    public static boolean isSyncing = false;
    private static final String PREFS = "sync_prefs";
    private static final String LAST_SYNCED = "last_synced";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        salesClient = new SalesClient();

        initialiseGreenDao();

        //Sync everything here
        new SyncTask().execute();

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
                System.currentTimeMillis() + (1000 * 60 * 60),
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
            customerDao = daoSession.getCustomerDao();
            customerContactDao = daoSession.getCustomerContactDao();
            taskDao = daoSession.getTaskDao();
            detailerCallDao = daoSession.getDetailerCallDao();
            malariaDetailDao = daoSession.getMalariaDetailDao();
            productDao = daoSession.getProductDao();
            saleDao = daoSession.getSaleDao();
            orderDao = daoSession.getOrderDao();
            adhockSaleDao = daoSession.getAdhockSaleDao();
            summaryReportDao = daoSession.getSummaryReportDao();
            taskOrderDao = daoSession.getTaskOrderDao();
        } catch (Exception ex) {
            Utils.log("Error initializing greendao for sync");
        }
    }

    public void startSyncronisationProcess() {
        Utils.log("startSyncronisationProcess()");
        try {
            syncronisationErros = new ArrayList<>();
            downloadRegions();
            downloadCustomers();
            downloadTasks();
            downloadProducts();
            downloadSummaryReports();

            uploadCustomers();
            uploadDirectSales();
            uploadSales();
            uploadTasks();
            uploadOrders();

            downloadDiarrheaHistory();
            downloadMalariaHistory();
            if (!syncronisationErros.isEmpty()) {
                displaySyncErros(syncronisationErros);
            }
        }catch (Exception ex){
            Utils.log("Error syncing -> " + ex.getMessage());
        }
    }

    public void downloadRegions() {
        updatePropgress("Downloading Regions...");
        Region[] regions = place.downloadRegions();
        if (regions != null) {
            regionDao.deleteAll();
            for (Region region : regions) {
                regionDao.insert(region);
            }
            downloadDistricts();
        }
    }

    public void downloadDistricts() {
        updatePropgress("Downloading Districts...");
        District[] districts = place.downloadDistricts();
        if(districts != null){
            districtDao.deleteAll();
            for (District district : districts) {
                districtDao.insert(district);
            }
        }
        downloadSubcounties();
    }

    public void downloadSubcounties() {
        updatePropgress("Downloading Subcounties...");
        Subcounty[] subcounties = place.downloadSubcounties();
        if(subcounties != null){
            subcountyDao.deleteAll();
            for (Subcounty subcounty : subcounties) {
                subcountyDao.insert(subcounty);
            }
        }
    }

    public void downloadCustomers() {
        updatePropgress("Downloading Customers..");
        Customer[] customers = customerClient.downloadCustomers();
        customerDao.insertOrReplaceInTx(customers);
        List<CustomerContact> contacts = new ArrayList<>();
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
        updatePropgress("Downloading Tasks...");

        Task[] tasks = taskClient.downloadTasks();
        Utils.log("Got tasks -> " + tasks.length);
        taskDao.insertOrReplaceInTx(tasks);
        List<TaskOrder> taskOrders = new ArrayList<>();
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
        List<Task> taskList = taskDao.queryBuilder().where(TaskDao.Properties.Status.notEq(HomeActivity.STATUS_NEW)).list();

        if (!taskList.isEmpty()) {
            updatePropgress("Uploading Tasks.. " + taskList.size());
        }else{
            Utils.log("Tasks list is empty");
        }

        for (Task task : taskList) {
            Utils.log("Sycn task -> " + task.getDescription() + " : " + task.getType());
            if (taskIsHistory(task)) {
                Utils.log("Task is history");
                continue;
            }
            if(!taskHasDetails(task)){
                Utils.log("Task has no details -> skipping");
                continue;
            }
            ServerResponse response = taskClient.uploadTask(task);
            Utils.log("Task upload response -> " + response.getStatus() + " : " + response.getMessage());
            if (response.getStatus().equalsIgnoreCase("OK")) {
                Utils.log("Task syncronized succesfully -> " + task.getDescription() + " : " + task.getUuid());
                //set all detailer and sale calls to isHistroy
                if (RestClient.getRole().equalsIgnoreCase(User.ROLE_DETAILER)) {
                    if(task.getType().equalsIgnoreCase("malaria")){
                        MalariaDetail detail = task.getMalariadetails().get(0);
                        detail.setIsHistory(true);
                        detail.setIsDirty(false);
                        detail.setSyncronisationStatus(BaseEntity.SYNC_SUCCESS);
                        malariaDetailDao.update(detail);
                    }else{
                        DetailerCall detailerCall = task.getDetailers().get(0);
                        detailerCall.setIsHistory(true);
                        detailerCall.setIsDirty(false);
                        detailerCall.setSyncronisationStatus(BaseEntity.SYNC_SUCCESS);
                        detailerCallDao.update(detailerCall);
                    }
                }
            }
        }
    }

    private boolean taskIsHistory(Task task) {
        try {
            if (!task.getDetailers().isEmpty() && task.getDetailers().get(0).getIsHistory()) {
                return true;
            }else if (!task.getMalariadetails().isEmpty() && task.getMalariadetails().get(0).getIsHistory()) {
                return true;
            } else if (!task.getSales().isEmpty() && task.getSales().get(0).getIsHistory()) {
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    private boolean taskHasDetails(Task task){
        try {
            return !task.getDetailers().isEmpty() || !task.getSales().isEmpty() || !task.getMalariadetails().isEmpty();
        } catch (Exception ex) {
            Utils.log("Error checking for task details");
        }
        return false;
    }

    private void downloadDiarrheaHistory(){
        updatePropgress("Downloading Diarrhea History...");
        DetailerCall[] tasks = taskClient.downloadDiarrheaHistory();
        if(tasks != null){
            Task task;
            DetailerCall dc;
            for(DetailerCall d: tasks){
                dc = detailerCallDao.load(d.getUuid());
                if(dc == null){
                    task = taskDao.load(d.getUuid());
                    if(task == null){
                        task = new Task();
                    }
                    task.setStatus(HomeActivity.STATUS_COMPLETE);
                    task.setType("detailer");
                    task.setCompletionDate(d.getCompletionDate());
                    task.setIsDirty(false);
                    task.setCustomerId(d.getCustomerId());

                    if(task.getUuid() == null){
                        task.setUuid(d.getUuid());
                        taskDao.insert(task);
                    }else{
                        taskDao.update(task);
                    }

                    d.setTaskId(task.getUuid());
                    d.setDateOfSurvey(d.getCompletionDate());
                    d.setIsDirty(false);
                    d.setIsHistory(true);

                    d.setTaskId(task.getUuid());
                    detailerCallDao.insert(d);
                }
            }
        }
        Utils.log("Done downloading diarrhea history");
    }

    private void downloadMalariaHistory(){
        updatePropgress("Downloading Malaria History...");
        MalariaDetail[] tasks = taskClient.downloadMalariaHistory();
        if(tasks != null){
            Task task;
            MalariaDetail md;
            for(MalariaDetail d: tasks){
                md = malariaDetailDao.load(d.getUuid());
                if(md == null){
                    task = taskDao.load(d.getUuid());
                    if(task == null){
                        task = new Task();
                    }
                    task.setStatus(HomeActivity.STATUS_COMPLETE);
                    task.setType("malaria");
                    task.setCompletionDate(d.getCompletionDate());
                    task.setIsDirty(false);
                    task.setCustomerId(d.getCustomerId());

                    if(task.getUuid() == null){
                        task.setUuid(d.getUuid());
                        taskDao.insert(task);
                    }else{
                        taskDao.update(task);
                    }

                    d.setTaskId(task.getUuid());
                    d.setDateOfSurvey(d.getCompletionDate());
                    d.setIsDirty(false);
                    d.setIsHistory(true);

                    d.setTaskId(task.getUuid());
                    malariaDetailDao.insert(d);
                }
            }
        }
        Utils.log("Done downloading malaria history");
    }

    public void uploadCustomers(){
        List<Customer> customersList = customerDao.queryBuilder().where(CustomerDao.Properties.IsDirty.eq(true)).list();
        for (Customer customer : customersList) {
            ServerResponse response = customerClient.uploadCustomer(customer, RestClient.getRestTemplate());
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
        Product[] products = productClient.downloadProducts();
        Utils.log("downloadProducts -> " + products.length);
        if(products != null){
            productDao.deleteAll();
            for (Product product : products) {
                productDao.insert(product);
            }
        }
    }

    private void uploadSales(){
        List<Sale> saleList = saleDao.queryBuilder().where(SaleDao.Properties.IsHistory.notEq(true)).list();
        if (!saleList.isEmpty()) {
            updatePropgress("Uploading Sales...");
        }
        for (Sale sale : saleList) {
            ServerResponse response = salesClient.uploadSale(sale);
            if (response.getStatus().equalsIgnoreCase("OK")) {
                Utils.log("Sale synced successfully -> " + sale.getTask().getCustomer().getOutletName());
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

    private void uploadDirectSales(){
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
        SummaryReport[] summaryReports = place.getSummaryReports();
        if(summaryReports != null){
            summaryReportDao.deleteAll();
            for (SummaryReport summaryReport : summaryReports) {
                summaryReportDao.insert(summaryReport);
            }
        }
    }

    private void updatePropgress(final String message) {
        Utils.log(message);
    }

    private void displaySyncErros(final List<ServerResponse> errors){
        StringBuilder formartedErrors = new StringBuilder();
        formartedErrors.append("The following errors were encountered during syncronisation process\n");
        for(ServerResponse response:errors){
            String message = response.getMessage();
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> mapObject = mapper.readValue(message,new TypeReference<Map<String, Object>>() {
                });
                formartedErrors.append(response.getItemRef()+":"+mapObject.get("message") +"\n");
                Utils.log(formartedErrors.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class SyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            startSyncronisationProcess();
            return null;
        }

        @Override
        protected void onPreExecute() {
            isSyncing = true;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            saveLastSynced(getApplicationContext(), System.currentTimeMillis());
            isSyncing = false;
            stopSelf();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public static long getLastSynced(Context cxt){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        return prefs.getLong(LAST_SYNCED, -1);
    }

    public static void saveLastSynced(Context cxt, long lastsynced){
        SharedPreferences prefs = cxt.getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(LAST_SYNCED, lastsynced);
        editor.commit();
    }
}