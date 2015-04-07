package org.chai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS
// KEEP INCLUDES - put your custom includes here
@JsonIgnoreProperties(ignoreUnknown = true)
// KEEP INCLUDES END
/**
 * Entity mapped to table SALE_DATA.
 */
public class SaleData implements BaseEntity {

    /** Not-null value. */
    private String uuid;
    private int quantity;
    private int price;
    private String adhockSaleId;


    // KEEP FIELDS - put your custom fields here
    /** Not-null value. */
    private String productId;
    @JsonIgnore
    private Boolean isDirty;
    @JsonIgnore
    private Integer syncronisationStatus;
    @JsonIgnore
    private String syncronisationMessage;
    @JsonIgnore
    private java.util.Date dateCreated;
    @JsonIgnore
    private java.util.Date lastUpdated;

    /** Used to resolve relations */
    @JsonIgnore
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @JsonIgnore
    private String saleId;
    @JsonIgnore
    private transient SaleDataDao myDao;

    @JsonIgnore
    private Sale sale;
    @JsonIgnore
    private String sale__resolvedKey;

    @JsonIgnore
    private Product product;
    @JsonIgnore
    private String product__resolvedKey;

    @JsonIgnore
    private AdhockSale adhockSale;
    @JsonIgnore
    private String adhockSale__resolvedKey;
    // KEEP FIELDS END

    public SaleData() {
    }

    public SaleData(String uuid) {
        this.uuid = uuid;
    }

    public SaleData(String uuid, int quantity, int price, String saleId, String adhockSaleId, String productId, Boolean isDirty, Integer syncronisationStatus, String syncronisationMessage, java.util.Date dateCreated, java.util.Date lastUpdated) {
        this.uuid = uuid;
        this.quantity = quantity;
        this.price = price;
        this.saleId = saleId;
        this.adhockSaleId = adhockSaleId;
        this.productId = productId;
        this.isDirty = isDirty;
        this.syncronisationStatus = syncronisationStatus;
        this.syncronisationMessage = syncronisationMessage;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSaleDataDao() : null;
    }

    /** Not-null value. */
    public String getUuid() {
        return uuid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getAdhockSaleId() {
        return adhockSaleId;
    }

    public void setAdhockSaleId(String adhockSaleId) {
        this.adhockSaleId = adhockSaleId;
    }

    /** Not-null value. */
    public String getProductId() {
        return productId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
    }

    public Integer getSyncronisationStatus() {
        return syncronisationStatus;
    }

    public void setSyncronisationStatus(Integer syncronisationStatus) {
        this.syncronisationStatus = syncronisationStatus;
    }

    public String getSyncronisationMessage() {
        return syncronisationMessage;
    }

    public void setSyncronisationMessage(String syncronisationMessage) {
        this.syncronisationMessage = syncronisationMessage;
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public java.util.Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /** To-one relationship, resolved on first access. */
    public Sale getSale() {
        String __key = this.saleId;
        if (sale__resolvedKey == null || sale__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SaleDao targetDao = daoSession.getSaleDao();
            Sale saleNew = targetDao.load(__key);
            synchronized (this) {
                sale = saleNew;
            	sale__resolvedKey = __key;
            }
        }
        return sale;
    }

    public void setSale(Sale sale) {
        synchronized (this) {
            this.sale = sale;
            saleId = sale == null ? null : sale.getUuid();
            sale__resolvedKey = saleId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Product getProduct() {
        String __key = this.productId;
        if (product__resolvedKey == null || product__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
            	product__resolvedKey = __key;
            }
        }
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new DaoException("To-one property 'productId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.product = product;
            productId = product.getUuid();
            product__resolvedKey = productId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public AdhockSale getAdhockSale() {
        String __key = this.adhockSaleId;
        if (adhockSale__resolvedKey == null || adhockSale__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AdhockSaleDao targetDao = daoSession.getAdhockSaleDao();
            AdhockSale adhockSaleNew = targetDao.load(__key);
            synchronized (this) {
                adhockSale = adhockSaleNew;
            	adhockSale__resolvedKey = __key;
            }
        }
        return adhockSale;
    }

    public void setAdhockSale(AdhockSale adhockSale) {
        synchronized (this) {
            this.adhockSale = adhockSale;
            adhockSaleId = adhockSale == null ? null : adhockSale.getUuid();
            adhockSale__resolvedKey = adhockSaleId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
