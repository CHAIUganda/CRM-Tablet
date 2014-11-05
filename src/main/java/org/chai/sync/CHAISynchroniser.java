package org.chai.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.chai.model.*;
import org.chai.rest.*;

import java.util.List;

/**
 * Created by victor on 11/3/14.
 */
public class CHAISynchroniser {

    private Context context;
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

    public CHAISynchroniser(Context context) {
        this.context = context;
        place = new Place();
        customerClient = new CustomerClient();
        productClient = new ProductClient();
        taskClient = new TaskClient();
        initialiseGreenDao();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "chai-crm-db", null);
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
        } catch (Exception ex) {
            Log.d("Error=====================================", ex.getLocalizedMessage());
        }
    }

    public void startSyncronisationProcess() {
        try{
            regionDao.deleteAll();
            districtDao.deleteAll();
            subcountyDao.deleteAll();
            parishDao.deleteAll();
            villageDao.deleteAll();
            customerContactDao.deleteAll();
            customerDao.deleteAll();
            taskDao.deleteAll();
            downloadRegions();
            downloadCustomers();
            downloadTasks();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Log.i("Synchroniser:","=============================================done");
    }

    public void downloadRegions() {
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
        Customer[] customers = customerClient.downloadCustomers();
        for(Customer customer:customers){
            Long id = customerDao.insert(customer);
            saveCustomerContacts(customer.getCustomerContacts(),id);
        }
    }

    public void saveCustomerContacts(List<CustomerContact> customerContacts,Long customerId){
        for(CustomerContact customerContact:customerContacts){
            customerContact.setCustomerId(customerId);
            customerContactDao.insert(customerContact);
        }
    }

    public void downloadTasks(){
        Task[] tasks = taskClient.downloadTasks();
        for(Task task:tasks){
            taskDao.insert(task);
        }
    }

}
