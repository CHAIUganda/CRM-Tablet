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

import org.chai.activities.BaseActivity;
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
import org.chai.model.ProductGroup;
import org.chai.model.ProductGroupDao;
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
import org.chai.util.AccountManager;
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
    private ProductGroupDao productGroupDao;
    private ProductDao productDao;
    private SaleDao saleDao;
    private OrderDao orderDao;
    private AdhockSaleDao adhockSaleDao;
    private SummaryReportDao summaryReportDao;
    private TaskOrderDao taskOrderDao;
    private List<ServerResponse> syncronisationErros;

    public static boolean isSyncing = false;
    public static final String PREFS = "sync_prefs";
    public static final String LAST_SYNCED = "last_synced";

    private static SyncTask syncTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        salesClient = new SalesClient();

        initialiseGreenDao();

        //Sync everything here
        syncTask = new SyncTask();
        syncTask.execute();

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
            productGroupDao = daoSession.getProductGroupDao();
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
            downloadProductGroups();
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Regions...");
        Region[] regions = place.downloadRegions();
        if (regions != null) {
            for (Region region : regions) {
                if(regionDao.load(region.getUuid()) == null){
                    regionDao.insert(region);
                }else{
                    regionDao.update(region);
                }
            }
            downloadDistricts();
        }
    }

    public void downloadDistricts() {
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Districts...");
        District[] districts = place.downloadDistricts();
        if(districts != null){
            for(District d : districts){
                if(districtDao.load(d.getUuid()) == null){
                    districtDao.insert(d);
                }else{
                    districtDao.update(d);
                }
            }
        }
        downloadSubcounties();
    }

    public void downloadSubcounties() {
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Subcounties...");
        Subcounty[] subcounties = place.downloadSubcounties();
        if(subcounties != null){
            for(Subcounty s: subcounties){
                if(subcountyDao.load(s.getUuid()) == null){
                    subcountyDao.insert(s);
                }else{
                    subcountyDao.update(s);
                }
            }
        }
    }

    public void downloadCustomers() {
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Customers..");
        Customer[] customers = customerClient.downloadCustomers();
        Utils.log("Got customers -> " + customers.length);
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        List<Task> taskList = taskDao.queryBuilder().where(TaskDao.Properties.Status.notEq(HomeActivity.STATUS_NEW)).list();

        if (!taskList.isEmpty()) {
            updatePropgress("Uploading Tasks.. " + taskList.size());
        }else{
            Utils.log("Tasks list is empty");
        }

        for (Task task : taskList) {
            if (taskIsHistory(task)) {
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Diarrhea History...");
        DetailerCall[] tasks = taskClient.downloadDiarrheaHistory();
        if(tasks != null){
            Task task;
            DetailerCall dc;
            for(DetailerCall d: tasks){
                dc = detailerCallDao.load(d.getUuid());
                task = taskDao.load(d.getUuid());
                if(dc == null && task == null){
                    task = new Task();

                    task.setStatus(HomeActivity.STATUS_COMPLETE);
                    task.setType("detailer");
                    task.setCompletionDate(d.getCompletionDate());
                    task.setIsDirty(false);
                    task.setCustomerId(d.getCustomerId());

                    task.setUuid(d.getUuid());
                    taskDao.insert(task);

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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Malaria History...");
        MalariaDetail[] tasks = taskClient.downloadMalariaHistory();
        if(tasks != null){
            Task task;
            MalariaDetail md;
            for(MalariaDetail d: tasks){
                md = malariaDetailDao.load(d.getUuid());
                task = taskDao.load(d.getUuid());
                if(md == null && task == null){ //If we don't have the task and details in our system - add it
                    task = new Task();
                    task.setStatus(HomeActivity.STATUS_COMPLETE);
                    task.setType("malaria");
                    task.setCompletionDate(d.getCompletionDate());
                    task.setIsDirty(false);
                    task.setCustomerId(d.getCustomerId());

                    task.setUuid(d.getUuid());
                    taskDao.insert(task);

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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
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

    private void downloadProductGroups(){
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        updatePropgress("Downloading Product Groups...");
        ProductGroup[] groups = productClient.downloadProductGroups();
        Utils.log("Got product groups -> " + groups.length);
        if (groups != null) {
            for (ProductGroup group : groups) {
                if(productGroupDao.load(group.getId()) == null){
                    productGroupDao.insert(group);
                }else{
                    productGroupDao.update(group);
                }
            }
            downloadProducts();
        }
    }

    private void downloadProducts() {
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        List<Sale> saleList = saleDao.queryBuilder().where(SaleDao.Properties.IsHistory.notEq(true)).list();
        if (!saleList.isEmpty()) {
            updatePropgress("Uploading Sales...");
        }else{
            Utils.log("Sales is empty");
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        List<AdhockSale> saleList = adhockSaleDao.loadAll();
        if (!saleList.isEmpty()) {
            updatePropgress("Uploading Sales...");
        }
        for (AdhockSale adhockSale : saleList) {
            if(adhockSale.getIsHistory()){
                continue;
            }
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
        List<Order> orderList = orderDao.loadAll();
        if (!orderList.isEmpty()) {
            updatePropgress("Uploading Orders...");
        }
        for (Order order : orderList) {
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
        if(!AccountManager.offlineLogin(getApplication(), false)){
            return;
        }
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
            BaseActivity.updateLastSynced(getApplicationContext());
            stopSelf();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public static void abortSync(){
        Utils.log("Cancelling syncing");
        isSyncing = false;
        if(syncTask != null && syncTask.getStatus() == AsyncTask.Status.RUNNING){
            Utils.log("Sync was running");
            syncTask.cancel(true);
        }else{
            Utils.log("No need sync is not running");
        }
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