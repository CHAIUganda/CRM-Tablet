package org.chai.sync;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.chai.R;
import org.chai.activities.tasks.TasksMainActivity;
import org.chai.model.*;
import org.chai.rest.*;

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

    public CHAISynchroniser(Activity activity) {
        this.parent = activity;
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        initialiseGreenDao();
    }
    public CHAISynchroniser(Activity parent,ProgressDialog progressDialog){
        this.parent = parent;
        this.progressDialog = progressDialog;
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
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
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
        }
    }

    public void startSyncronisationProcess() {
        try{
            uploadCustomers();
            uploadTasks();
            regionDao.deleteAll();
            districtDao.deleteAll();
            subcountyDao.deleteAll();
            parishDao.deleteAll();
            villageDao.deleteAll();
            customerContactDao.deleteAll();
            customerDao.deleteAll();
            taskDao.deleteAll();
            downloadRegions();
            progressDialog.incrementProgressBy(30);
            downloadCustomers();
            progressDialog.incrementProgressBy(50);
            downloadTasks();
            progressDialog.incrementProgressBy(20);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Log.i("Synchroniser:","=============================================done");
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
        District[] districts = place.downloadDistricts();
        for (District district : districts) {
            districtDao.insert(district);
        }
        downloadSubcounties();
    }

    public void downloadSubcounties() {
        Subcounty[] subcounties = place.downloadSubcounties();
        for (Subcounty subcounty : subcounties) {
             subcountyDao.insert(subcounty);
        }
        downloadParishes();
    }

    public void downloadParishes() {
        Parish[] parishes = place.downloadParishes();
        for (Parish parish : parishes) {
            parishDao.insert(parish);
        }
        downloadVillage();
    }

    public void downloadVillage() {
        Village[] villages = place.downloadVillages();
        for (Village village : villages) {
            villageDao.insert(village);
        }
    }

    public void downloadCustomers(){
        updatePropgress("Downloading Customers..");
        Customer[] customers = customerClient.downloadCustomers();
        for(Customer customer:customers){
            Long id = customerDao.insert(customer);
            saveCustomerContacts(customer.getCustomerContacts(), id);
        }
    }

    public void saveCustomerContacts(List<CustomerContact> customerContacts,Long customerId){
        for(CustomerContact customerContact:customerContacts){
            customerContact.setCustomerId(customerId);
            customerContactDao.insert(customerContact);
        }
    }

    public void downloadTasks(){
        updatePropgress("Downloading Tasks..");
        Task[] tasks = taskClient.downloadTasks();
        for(Task task:tasks){
            taskDao.insert(task);
        }
    }

    public void uploadCustomers(){
        List<Customer> customersList = customerDao.queryBuilder().where(CustomerDao.Properties.IsDirty.eq(true)).list();
        if(!customersList.isEmpty()){
            updatePropgress("Uploading Customers..");
            boolean uploaded = customerClient.uploadCustomers(customersList.toArray(new Customer[customersList.size()]));
            if (uploaded) {
                customerDao.queryBuilder().where(CustomerDao.Properties.IsDirty.eq(true)).buildDelete();
            }
        }
    }

    public void uploadTasks(){
        List<Task> taskList = taskDao.queryBuilder().where(TaskDao.Properties.Status.eq("complete")).list();
        if(!taskList.isEmpty()){
            updatePropgress("Uploading Tasks..");
        }
        for (Task task : taskList) {
            boolean uploaded = taskClient.uploadTask(task);
            if (uploaded) {
                detailerCallDao.queryBuilder().where(DetailerCallDao.Properties.TaskId.eq(task.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
                taskDao.delete(task);
            }
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
