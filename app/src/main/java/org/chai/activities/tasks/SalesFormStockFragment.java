package org.chai.activities.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.model.AdhockSale;
import org.chai.model.AdhockSaleDao;
import org.chai.model.Customer;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.model.SaleDataDao;
import org.chai.model.StokeData;
import org.chai.model.StokeDataDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 5/3/2015.
 */
public class SalesFormStockFragment extends Fragment{
    AQuery aq;
    View view;
    LinearLayout rowContainer;
    ArrayList<View> rows;
    ArrayList<StokeData> stocks;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AdhockSaleDao adhockSaleDao;
    private SaleDataDao saleDataDao;
    private StokeDataDao stokeDataDao;
    private ProductDao productDao;
    private CustomerDao customerDao;

    private AdhockSale saleInstance;
    private Customer salesCustomer;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sales_form_stock_fragment, container, false);
        aq = new AQuery(view);
        rowContainer = (LinearLayout)view.findViewById(R.id.ln_rows_container);
        initialiseGreenDao();

        aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(new StokeData());
            }
        });

        aq.id(R.id.spn_do_you_stock_ors_and_zinc).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    ((SalesFormActivity) getActivity()).pager.setCurrentItem(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rows = new ArrayList<View>();
        stocks = new ArrayList<StokeData>();

        products = productDao.loadAll();

        return view;
    }

    private void addRow(final StokeData stock){
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.stock_form_row, null);

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows.remove(row);
                stocks.remove(stock);
                LinearLayout parent = (LinearLayout) v.getParent();
                LinearLayout root = (LinearLayout) parent.getParent();
                LinearLayout top = (LinearLayout) root.getParent();
                rowContainer.removeView(top);
            }
        });

        Spinner spinner = (Spinner)row.findViewById(R.id.product);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, products.toArray(new Product[products.size()])));

        rowContainer.addView(row);
        rows.add(row);
        stocks.add(stock);
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            adhockSaleDao = daoSession.getAdhockSaleDao();
            saleDataDao = daoSession.getSaleDataDao();
            productDao = daoSession.getProductDao();
            customerDao = daoSession.getCustomerDao();
            stokeDataDao = daoSession.getStokeDataDao();
        } catch (Exception ex) {
            Log.d("Error", ex.getLocalizedMessage());
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean saveFields(){
        String stocksZincOrs = aq.id(R.id.spn_do_you_stock_ors_and_zinc).getSelectedItem().toString();
        if(stocksZincOrs.isEmpty()){
            Toast.makeText(getActivity(), "Please select wether customer stocks ORS and Zinc", Toast.LENGTH_LONG).show();
            return false;
        }

        if(stocks.size() == 0 && stocksZincOrs.equalsIgnoreCase("Yes")){
            Toast.makeText(getActivity(), "Please enter Zinc and ORS stock", Toast.LENGTH_LONG).show();
            return false;
        }

        int i = 1;
        StokeData stock;
        Product product;
        int productIndex;
        for(View row : rows){
            AQuery a = new AQuery(row);
            productIndex = a.id(R.id.product).getSelectedItemPosition();
            product = products.get(productIndex);
            String quantity = a.id(R.id.txt_quantity).getText().toString();
            if(quantity.isEmpty()){
                Toast.makeText(getActivity(), "Please enter stock quanity on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }
            stock = stocks.get(rows.indexOf(row));
            stock.setQuantity(Integer.parseInt(quantity));
            stock.setProduct(product);
            stock.setProductId(product.getUuid());

            i++;
        }

        return true;
    }
}
