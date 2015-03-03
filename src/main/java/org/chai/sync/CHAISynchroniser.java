package org.chai.sync;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import org.chai.model.*;
import org.chai.rest.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 11/3/14.
 */
public class CHAISynchroniser {

    private Activity parent;
    private ProgressDialog progressDialog;
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

    public CHAISynchroniser(Activity activity) {
        this.parent = activity;
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        salesClient = new SalesClient();
        initialiseGreenDao();
    }
    public CHAISynchroniser(Activity parent,ProgressDialog progressDialog){
        this.parent = parent;
        this.progressDialog = progressDialog;
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        salesClient = new SalesClient();
        initialiseGreenDao();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(parent.getApplicationContext(), "chai-crm-db", null);
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
            Log.d("Error=====================================", ex.getLocalizedMessage());
        }
    }

    public void startSyncronisationProcess() {
        try{
            uploadCustomers();
            uploadDirectSales();
            uploadSales();
            uploadTasks();
            uploadOrders();
            progressDialog.incrementProgressBy(30);
            regionDao.deleteAll();
            districtDao.deleteAll();
            subcountyDao.deleteAll();
            customerContactDao.deleteAll();
            customerDao.deleteAll();
//            saleDao.deleteAll();
            downloadRegions();
            progressDialog.incrementProgressBy(10);
            downloadCustomers();
            progressDialog.incrementProgressBy(20);
            downloadTasks();
            progressDialog.incrementProgressBy(20);
            downloadProducts();
            downloadSummaryReports();

            progressDialog.incrementProgressBy(20);
        }catch (HttpClientErrorException e){
            Log.i("Error:============================================",e.getResponseBodyAsString());
            e.printStackTrace();
        }catch (HttpServerErrorException se){
            Log.i("Error:============================================",se.getResponseBodyAsString());
        }catch (Exception ex){
            ex.printStackTrace();
            parent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(parent.getApplicationContext(),
                            "The Syncronisation Process is Unable to continue,Please ensure that there is a network connection",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        Log.i("Synchroniser:", "=============================================done");
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

    public void downloadParishes() {
        updatePropgress("Downloading Parishes...");
        Parish[] parishes = place.downloadParishes();
        for (Parish parish : parishes) {
            parishDao.insert(parish);
        }
        downloadVillage();
    }

    public void downloadVillage() {
        updatePropgress("Downloading Villages...");
        Village[] villages = place.downloadVillages();
        for (Village village : villages) {
            villageDao.insert(village);
        }
    }

    public void downloadCustomersOld(){
        updatePropgress("Downloading Customers..");
        Customer[] customers = customerClient.downloadCustomers();
        for(Customer customer:customers){
            Long id = customerDao.insert(customer);
            saveCustomerContacts(customer.getCustomerContacts(), customer.getUuid());
        }
    }
    public void downloadCustomers(){
        updatePropgress("Downloading Customers..");
        Customer[] customers = customerClient.downloadCustomers();
        customerDao.insertInTx(customers);

        List<CustomerContact> contacts = new ArrayList<CustomerContact>();
        for(Customer customer:customers){
            List<CustomerContact> ccs = customer.getCustomerContacts();
            if (ccs != null) {
                for (CustomerContact cc : ccs) {
                    cc.setCustomerId(customer.getUuid());
                }
                contacts.addAll(ccs);
            }
        }
        customerContactDao.insertInTx(contacts);
    }

    public void saveCustomerContacts(List<CustomerContact> customerContacts,String customerId){
        for(CustomerContact customerContact:customerContacts){
            customerContact.setCustomerId(customerId);
            customerContactDao.insert(customerContact);
        }
    }

    public void downloadTasks(){
        updatePropgress("Downloading Tasks..");

        Task[] tasks = taskClient.downloadTasks();
        for(Task task:tasks){
            try{
                taskDao.insert(task);
                insertTaskOrders(task);
            }catch (Exception ex){
                //catch cases when task is a duplicate
            }
        }
    }
    private void insertTaskOrders(Task task){
        if(task.getLineItems()!=null){
            for(TaskOrder order:task.getLineItems()){
                order.setTaskId(task.getUuid());
                order.setTask(task);
                taskOrderDao.insert(order);
            }
        }
    }

    public void uploadTasks(){
        List<Task> taskList = taskDao.queryBuilder().where(TaskDao.Properties.Status.notEq("new")).list();
        if(!taskList.isEmpty()){
            updatePropgress("Uploading Tasks..");
        }
        for (Task task : taskList) {
            try{
                if(taskIsHistory(task)){
                    continue;
                }
                boolean uploaded = taskClient.uploadTask(task);
                if(uploaded){
                    //set all detailer calls to isHistroy
                    if (RestClient.role.equalsIgnoreCase(User.ROLE_DETAILER)) {
                        DetailerCall detailerCall = task.getDetailers().get(0);
                        detailerCall.setIsHistory(true);
                        detailerCallDao.update(detailerCall);
                    }else{
                        Sale sale = task.getSales().get(0);
                        sale.setIsHistory(true);
                        saleDao.update(sale);
                    }
                }
            }catch (Exception ex){
                //
            }
        }

    }

    private boolean taskIsHistory(Task task) {
        if(!task.getDetailers().isEmpty() &&task.getDetailers().get(0).getIsHistory()){
            return true;
        }else if(!task.getSales().isEmpty()&&task.getSales().get(0).getIsHistory()){
            return true;
        }
        return false;
    }

    public void uploadCustomers(){
        List<Customer> customersList = customerDao.queryBuilder().where(CustomerDao.Properties.IsDirty.eq(true)).list();
        if(!customersList.isEmpty()){
            updatePropgress("Uploading Customers...");
            boolean uploaded = customerClient.uploadCustomers(customersList.toArray(new Customer[customersList.size()]));
            if (uploaded) {
                customerDao.queryBuilder().where(CustomerDao.Properties.IsDirty.eq(true)).buildDelete();
            }
        }
    } 
    private void downloadProducts(){
        productDao.deleteAll();
        Product[] products = productClient.downloadProducts();
        for(Product product:products){
            productDao.insert(product);
        }
    }

    private void uploadSales(){
        List<Sale> saleList = saleDao.loadAll();
        if(!saleList.isEmpty()){
            updatePropgress("Uploading Sales...");
            boolean uploaded = salesClient.uploadSales(saleList.toArray(new Sale[saleList.size()]));
            if(uploaded){
                for(Sale sale:saleList){
                    sale.setIsHistory(true);
                    saleDao.update(sale);
                }
            }
        }
    }
    private void uploadDirectSales(){
        List<AdhockSale> saleList = adhockSaleDao.loadAll();
        if(!saleList.isEmpty()){
            updatePropgress("Uploading Sales...");
            boolean uploaded = salesClient.uploadDirectSale(saleList.toArray(new AdhockSale[saleList.size()]));
            if(uploaded){
                adhockSaleDao.deleteAll();
            }
        }
    }

    private void uploadOrders(){
        List<Order> orderList = orderDao.loadAll();
        if(!orderList.isEmpty()){
            updatePropgress("Uploading Orders...");
            boolean uploaded = salesClient.uploadOrders(orderList.toArray(new Order[orderList.size()]));
            if(uploaded){
                orderDao.deleteAll();
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

    private void updatePropgress(final String message){
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
            }
        });
    }

}
