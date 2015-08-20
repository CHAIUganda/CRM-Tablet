package org.chai.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import org.chai.R;
import org.chai.adapter.ProductListAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 8/20/2015.
 */
public class ProductsActivity extends BaseActivity {
    Toolbar toolbar;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ProductDao productDao;

    private List<Product> products;
    private ProductListAdapter adapter;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_activity);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        products = new ArrayList<>();

        listView = (ListView)findViewById(R.id.lst_products);

        super.setUpDrawer(toolbar);

        initialiseGreenDao();

        loadDataFromDb();
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            productDao = daoSession.getProductDao();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadDataFromDb() {
        products.clear();
        products.addAll(productDao.loadAll());
        adapter = new ProductListAdapter(this, products);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
