package org.chai.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import org.chai.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private Context context;

    public SampleData(Context context) {
        this.context = context;
        initialiseGreenDao();
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            customerDao = daoSession.getCustomerDao();
            customerContactDao = daoSession.getCustomerContactDao();
            regionDao = daoSession.getRegionDao();
            districtDao = daoSession.getDistrictDao();
            subcountyDao = daoSession.getSubcountyDao();
        } catch (Exception ex) {
            //
        }
    }

    public void createBaseData() {
        try {
            Region region = new Region(null, UUID.randomUUID().toString(), "Central");
            long regionId = regionDao.insert(region);
            District district = new District(null, UUID.randomUUID().toString(), "Kampla", regionId);
            long districtId = districtDao.insert(district);
            Subcounty subcounty = new Subcounty(null, UUID.randomUUID().toString(), "Kamuokya", districtId);
            long subcountyId = subcountyDao.insert(subcounty);
            generateSampleCustomers(subcountyId);

        } catch (Exception ex) {
            Toast.makeText(context, "error in createbaseData:" + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void generateSampleCustomers(long subcountyId) {

        Customer customer1 = new Customer(null);
        customer1.setSysid(UUID.randomUUID().toString());
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
        customer1.setNumberOfProducts(10);
        customer1.setRestockFrequency(2);
        customer1.setTurnOver(3143.345);
        customer1.setTenureStartDate(new Date());
        customer1.setTenureEndDate(new Date());
        customer1.setSubcountyId(subcountyId);
        long customerId1 = customerDao.insert(customer1);

        Customer customer2 = new Customer(null);
        customer2.setSysid(UUID.randomUUID().toString());
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
        customer2.setNumberOfProducts(10);
        customer2.setRestockFrequency(2);
        customer2.setTurnOver(3143.345);
        customer2.setTenureStartDate(new Date());
        customer2.setTenureEndDate(new Date());
        customer2.setSubcountyId(subcountyId);
        long customerId2 = customerDao.insert(customer2);

        Customer customer3 = new Customer(null);
        customer3.setSysid(UUID.randomUUID().toString());
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
        customer3.setNumberOfProducts(10);
        customer3.setRestockFrequency(2);
        customer3.setTurnOver(3143.345);
        customer3.setTenureStartDate(new Date());
        customer3.setTenureEndDate(new Date());
        customer3.setSubcountyId(subcountyId);
        long customerId3 = customerDao.insert(customer3);

        generateSampleCustomerContacts(customerId1, customerId2, customerId3);


    }

    public void generateSampleCustomerContacts(long customer1Id, long customer2Id, long customer3Id) {

        CustomerContact customerContact = new CustomerContact(null);
        customerContact.setSysid(UUID.randomUUID().toString());
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
        customerContact2.setSysid(UUID.randomUUID().toString());
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
        customerContact3.setSysid(UUID.randomUUID().toString());
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

}
