package org.chai.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.chai.model.*;
import org.chai.util.migration.UpgradeOpenHelper;

/**
 * Created by victor on 10/16/14.
 */
public class SampleData {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CustomerDao customerDao;
    private CustomerContactDao customerContactDao;
    private RegionDao regionDao;
    private DistrictDao districtDao;
    private SubcountyDao subcountyDao;
    private TaskDao taskDao;
    private SaleDao saleDao;
    private SaleDataDao saleDataDao;
    private Context context;

    public SampleData(Context context) {
        this.context = context;
        initialiseGreenDao();
    }


    private void initialiseGreenDao() {
        try {
             UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            customerContactDao = daoSession.getCustomerContactDao();
            regionDao = daoSession.getRegionDao();
            districtDao = daoSession.getDistrictDao();
            subcountyDao = daoSession.getSubcountyDao();
            taskDao = daoSession.getTaskDao();
            saleDao = daoSession.getSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
        } catch (Exception ex) {
            //
        }
    }
    /*
        public void createBaseData() {
            try {
                List<Region> regions = regionDao.loadAll();
                if(regions.isEmpty()){
                    Region region = new Region(null, UUID.randomUUID().toString(), "Central");
                    long regionId = regionDao.insert(region);
                    District district = new District(null, UUID.randomUUID().toString(), "Kampla", regionId);
                    long districtId = districtDao.insert(district);
                    Subcounty subcounty = new Subcounty(null, UUID.randomUUID().toString(), "Kamuokya", districtId);
                    Subcounty subcounty2 = new Subcounty(null, UUID.randomUUID().toString(), "Kiwatule", districtId);

                    long subcountyId = subcountyDao.insert(subcounty);
                    subcountyDao.insert(subcounty2);
                    generateSampleCustomers(subcountyId);

                    long customerId = insertSampleCustomer(subcountyId,"Diva medical center");
                    insertSampleContact(customerId,"Kamugisha James");
                    insertSampleTasks(customerId);
                }

            } catch (Exception ex) {
                Toast.makeText(context, "error in createbaseData:" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }

        public void generateSampleCustomers(long subcountyId) {

            Customer customer1 = new Customer(null);
            customer1.setUuid(UUID.randomUUID().toString());
            customer1.setOutletName("Byaruhanga Drug shop");
            customer1.setDescriptionOfOutletLocation("Near Kyebando Super market");
            customer1.setLatitude(0.4183211563884709);
            customer1.setLongitude(31.221599949502696);
            customer1.setOutletType("Retail");
            customer1.setOutletSize("Medium");
            customer1.setSplit("Urban");
            customer1.setOpeningHours("Early Morning");
            customer1.setMajoritySourceOfSupply("Kampala,kamuokya");
            customer1.setKeyWholeSalerName("Byaruhanga");
            customer1.setKeyWholeSalerContact("0777505033");
            customer1.setBuildingStructure("Permanent");
            customer1.setEquipment("Trays");
            customer1.setNumberOfEmployees(10);
            customer1.setNumberOfBranches(5);
            customer1.setNumberOfCustomersPerDay(50);
            customer1.setNumberOfProducts("less than 10");
            customer1.setRestockFrequency(2);
            customer1.setTurnOver("Less than 500,000");
            customer1.setTenureLengthYears(3);
            customer1.setTenureLengthMonths(6);
            customer1.setSubcountyId(subcountyId);
            customer1.setParish("Buranga");
            customer1.setVillage("kyonyo");
            long customerId1 = customerDao.insert(customer1);

            Customer customer2 = new Customer(null);
            customer2.setUuid(UUID.randomUUID().toString());
            customer2.setOutletName("Mugenyi Drug shop");
            customer2.setDescriptionOfOutletLocation("Masaka Town");
            customer2.setLatitude(0.4183211563884709);
            customer2.setLongitude(31.221599949502696);
            customer2.setOutletType("Retail");
            customer2.setOutletSize("Medium");
            customer2.setSplit("Urban");
            customer2.setOpeningHours("Early Morning");
            customer2.setMajoritySourceOfSupply("Kampala,kamuokya");
            customer2.setKeyWholeSalerName("Byaruhanga");
            customer2.setKeyWholeSalerContact("0777505033");
            customer2.setBuildingStructure("Permanent");
            customer2.setEquipment("Trays");
            customer2.setNumberOfEmployees(10);
            customer2.setNumberOfBranches(5);
            customer2.setNumberOfCustomersPerDay(50);
            customer2.setNumberOfProducts("less than 10");
            customer2.setRestockFrequency(2);
            customer2.setTurnOver("Less than 500,000");
            customer2.setTenureLengthYears(3);
            customer2.setTenureLengthMonths(6);
            customer2.setVillageId(subcountyId);
            customer2.setParish("Buranga");
            customer2.setVillage("kyonyo");
            long customerId2 = customerDao.insert(customer2);

            Customer customer3 = new Customer(null);
            customer3.setUuid(UUID.randomUUID().toString());
            customer3.setOutletName("Imwota Drug shop");
            customer3.setDescriptionOfOutletLocation("Opposite Natete market");
            customer3.setLatitude(0.4183211563884709);
            customer3.setLongitude(31.221599949502696);
            customer3.setOutletType("Retail");
            customer3.setOutletSize("Medium");
            customer3.setSplit("Urban");
            customer3.setOpeningHours("Early Morning");
            customer3.setMajoritySourceOfSupply("Kampala,kamuokya");
            customer3.setKeyWholeSalerName("Byaruhanga");
            customer3.setKeyWholeSalerContact("0777505033");
            customer3.setBuildingStructure("Permanent");
            customer3.setEquipment("Trays");
            customer3.setNumberOfEmployees(10);
            customer3.setNumberOfBranches(5);
            customer3.setNumberOfCustomersPerDay(50);
            customer3.setNumberOfProducts("less than 10");
            customer3.setRestockFrequency(2);
            customer3.setTurnOver("Less than 500,000");
            customer3.setTenureLengthYears(3);
            customer3.setTenureLengthMonths(6);
            customer3.setSubcountyId(subcountyId);
            customer3.setParish("Buranga");
            customer3.setVillage("kyonyo");
            long customerId3 = customerDao.insert(customer3);

            generateSampleCustomerContacts(customerId1, customerId2, customerId3);


        }

        public void generateSampleCustomerContacts(long customer1Id, long customer2Id, long customer3Id) {

            CustomerContact customerContact = new CustomerContact(null);
            customerContact.setUuid(UUID.randomUUID().toString());
            customerContact.setContact("(256) 363-1791");
            customerContact.setName("Mugenyi Peter Bala");
            customerContact.setTypeOfContact("key");
            customerContact.setGender("Male");
            customerContact.setRole("Doctor");
            customerContact.setQualification("University");
            customerContact.setNetworkOrAssociation("Kampala Medics");
            customerContact.setGraduationYear(2013);
            customerContact.setCustomerId(customer1Id);

            CustomerContact customerContact2 = new CustomerContact();
            customerContact2.setUuid(UUID.randomUUID().toString());
            customerContact2.setContact("(256) 239-2354");
            customerContact2.setName("Owamukama Eliasaph");
            customerContact2.setTypeOfContact("key");
            customerContact2.setGender("Male");
            customerContact2.setRole("Doctor");
            customerContact2.setQualification("University");
            customerContact2.setNetworkOrAssociation("Kampala Medics");
            customerContact2.setGraduationYear(2013);
            customerContact2.setCustomerId(customer2Id);

            CustomerContact customerContact3 = new CustomerContact(null);
            customerContact3.setUuid(UUID.randomUUID().toString());
            customerContact3.setContact("(256) 620-7136");
            customerContact3.setName("Nasasira Unity");
            customerContact3.setTypeOfContact("key");
            customerContact3.setGender("Female");
            customerContact3.setRole("Doctor");
            customerContact3.setQualification("University");
            customerContact3.setNetworkOrAssociation("Makerere Young Doctors");
            customerContact3.setGraduationYear(2008);
            customerContact3.setCustomerId(customer3Id);

            customerContactDao.insert(customerContact);
            customerContactDao.insert(customerContact2);
            customerContactDao.insert(customerContact3);
        }

        public void insertSampleTasks(long customer1Id){
            Task task1 = new Task(null,UUID.randomUUID().toString(),"deliver order to sentumbwe","New task","high",new Date(),customer1Id);
            Task task2 = new Task(null,UUID.randomUUID().toString(),"deliver order to John","New task","high",new Date(),customer1Id);
            Task task3 = new Task(null,UUID.randomUUID().toString(),"deliver order to JOhn","New task","high",new Date(),customer1Id);
            Task task4 = new Task(null,UUID.randomUUID().toString(),"deliver order to Mary","Outstanding","high",new Date(),customer1Id);
            Task task5 = new Task(null,UUID.randomUUID().toString(),"deliver order to Sarah","Scheduled","high",new Date(),customer1Id);

            taskDao.insert(task1);
            taskDao.insert(task2);
            taskDao.insert(task3);
            taskDao.insert(task4);
            taskDao.insert(task5);


        }

        public long insertSampleCustomer(long subcountyId,String name){
            Customer customer1 = new Customer(null);
            customer1.setUuid(UUID.randomUUID().toString());
            customer1.setOutletName(name);
            customer1.setDescriptionOfOutletLocation("Wandegeya");
            customer1.setLatitude(0.4183211563884709);
            customer1.setLongitude(31.221599949502696);
            customer1.setOutletType("Retail");
            customer1.setOutletSize("Medium");
            customer1.setSplit("Urban");
            customer1.setOpeningHours("Early Morning");
            customer1.setMajoritySourceOfSupply("Kampala,kamuokya");
            customer1.setKeyWholeSalerName("Kayondo Ronald");
            customer1.setKeyWholeSalerContact("0777505033");
            customer1.setBuildingStructure("Permanent");
            customer1.setEquipment("Trays");
            customer1.setNumberOfEmployees(10);
            customer1.setNumberOfBranches(5);
            customer1.setNumberOfCustomersPerDay(50);
            customer1.setNumberOfProducts("less than 10");
            customer1.setRestockFrequency(2);
            customer1.setTurnOver("Less than 500,000");
            customer1.setTenureLengthYears(3);
            customer1.setTenureLengthMonths(6);
            customer1.setSubcountyId(subcountyId);
            customer1.setParish("Buranga");
            customer1.setVillage("kyonyo");
            long customerId1 = customerDao.insert(customer1);

            return customerId1;
        }

        public long insertSampleContact(long customerId,String name){
            CustomerContact customerContact = new CustomerContact(null);
            customerContact.setUuid(UUID.randomUUID().toString());
            customerContact.setContact("(256) 363-1791");
            customerContact.setName(name);
            customerContact.setTypeOfContact("key");
            customerContact.setGender("Male");
            customerContact.setRole("Doctor");
            customerContact.setQualification("University");
            customerContact.setNetworkOrAssociation("Kampala Medics");
            customerContact.setGraduationYear(2013);
            customerContact.setCustomerId(customerId);

            return customerContactDao.insert(customerContact);
        }

    */
}
