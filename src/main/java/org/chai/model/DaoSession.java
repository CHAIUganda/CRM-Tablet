package org.chai.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import org.chai.model.Region;
import org.chai.model.District;
import org.chai.model.Subcounty;
import org.chai.model.Parish;
import org.chai.model.Village;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.Product;
import org.chai.model.Order;
import org.chai.model.Task;
import org.chai.model.Promotion;
import org.chai.model.PromotionalItem;
import org.chai.model.Sale;
import org.chai.model.User;

import org.chai.model.RegionDao;
import org.chai.model.DistrictDao;
import org.chai.model.SubcountyDao;
import org.chai.model.ParishDao;
import org.chai.model.VillageDao;
import org.chai.model.CustomerDao;
import org.chai.model.CustomerContactDao;
import org.chai.model.ProductDao;
import org.chai.model.OrderDao;
import org.chai.model.TaskDao;
import org.chai.model.PromotionDao;
import org.chai.model.PromotionalItemDao;
import org.chai.model.SaleDao;
import org.chai.model.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig regionDaoConfig;
    private final DaoConfig districtDaoConfig;
    private final DaoConfig subcountyDaoConfig;
    private final DaoConfig parishDaoConfig;
    private final DaoConfig villageDaoConfig;
    private final DaoConfig customerDaoConfig;
    private final DaoConfig customerContactDaoConfig;
    private final DaoConfig productDaoConfig;
    private final DaoConfig orderDaoConfig;
    private final DaoConfig taskDaoConfig;
    private final DaoConfig promotionDaoConfig;
    private final DaoConfig promotionalItemDaoConfig;
    private final DaoConfig saleDaoConfig;
    private final DaoConfig userDaoConfig;

    private final RegionDao regionDao;
    private final DistrictDao districtDao;
    private final SubcountyDao subcountyDao;
    private final ParishDao parishDao;
    private final VillageDao villageDao;
    private final CustomerDao customerDao;
    private final CustomerContactDao customerContactDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;
    private final TaskDao taskDao;
    private final PromotionDao promotionDao;
    private final PromotionalItemDao promotionalItemDao;
    private final SaleDao saleDao;
    private final UserDao userDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        regionDaoConfig = daoConfigMap.get(RegionDao.class).clone();
        regionDaoConfig.initIdentityScope(type);

        districtDaoConfig = daoConfigMap.get(DistrictDao.class).clone();
        districtDaoConfig.initIdentityScope(type);

        subcountyDaoConfig = daoConfigMap.get(SubcountyDao.class).clone();
        subcountyDaoConfig.initIdentityScope(type);

        parishDaoConfig = daoConfigMap.get(ParishDao.class).clone();
        parishDaoConfig.initIdentityScope(type);

        villageDaoConfig = daoConfigMap.get(VillageDao.class).clone();
        villageDaoConfig.initIdentityScope(type);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        customerContactDaoConfig = daoConfigMap.get(CustomerContactDao.class).clone();
        customerContactDaoConfig.initIdentityScope(type);

        productDaoConfig = daoConfigMap.get(ProductDao.class).clone();
        productDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        taskDaoConfig.initIdentityScope(type);

        promotionDaoConfig = daoConfigMap.get(PromotionDao.class).clone();
        promotionDaoConfig.initIdentityScope(type);

        promotionalItemDaoConfig = daoConfigMap.get(PromotionalItemDao.class).clone();
        promotionalItemDaoConfig.initIdentityScope(type);

        saleDaoConfig = daoConfigMap.get(SaleDao.class).clone();
        saleDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        regionDao = new RegionDao(regionDaoConfig, this);
        districtDao = new DistrictDao(districtDaoConfig, this);
        subcountyDao = new SubcountyDao(subcountyDaoConfig, this);
        parishDao = new ParishDao(parishDaoConfig, this);
        villageDao = new VillageDao(villageDaoConfig, this);
        customerDao = new CustomerDao(customerDaoConfig, this);
        customerContactDao = new CustomerContactDao(customerContactDaoConfig, this);
        productDao = new ProductDao(productDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);
        taskDao = new TaskDao(taskDaoConfig, this);
        promotionDao = new PromotionDao(promotionDaoConfig, this);
        promotionalItemDao = new PromotionalItemDao(promotionalItemDaoConfig, this);
        saleDao = new SaleDao(saleDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Region.class, regionDao);
        registerDao(District.class, districtDao);
        registerDao(Subcounty.class, subcountyDao);
        registerDao(Parish.class, parishDao);
        registerDao(Village.class, villageDao);
        registerDao(Customer.class, customerDao);
        registerDao(CustomerContact.class, customerContactDao);
        registerDao(Product.class, productDao);
        registerDao(Order.class, orderDao);
        registerDao(Task.class, taskDao);
        registerDao(Promotion.class, promotionDao);
        registerDao(PromotionalItem.class, promotionalItemDao);
        registerDao(Sale.class, saleDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        regionDaoConfig.getIdentityScope().clear();
        districtDaoConfig.getIdentityScope().clear();
        subcountyDaoConfig.getIdentityScope().clear();
        parishDaoConfig.getIdentityScope().clear();
        villageDaoConfig.getIdentityScope().clear();
        customerDaoConfig.getIdentityScope().clear();
        customerContactDaoConfig.getIdentityScope().clear();
        productDaoConfig.getIdentityScope().clear();
        orderDaoConfig.getIdentityScope().clear();
        taskDaoConfig.getIdentityScope().clear();
        promotionDaoConfig.getIdentityScope().clear();
        promotionalItemDaoConfig.getIdentityScope().clear();
        saleDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public RegionDao getRegionDao() {
        return regionDao;
    }

    public DistrictDao getDistrictDao() {
        return districtDao;
    }

    public SubcountyDao getSubcountyDao() {
        return subcountyDao;
    }

    public ParishDao getParishDao() {
        return parishDao;
    }

    public VillageDao getVillageDao() {
        return villageDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public CustomerContactDao getCustomerContactDao() {
        return customerContactDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public PromotionDao getPromotionDao() {
        return promotionDao;
    }

    public PromotionalItemDao getPromotionalItemDao() {
        return promotionalItemDao;
    }

    public SaleDao getSaleDao() {
        return saleDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
