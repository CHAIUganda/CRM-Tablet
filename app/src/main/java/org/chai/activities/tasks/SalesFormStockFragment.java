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
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.model.StokeData;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
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

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ProductDao productDao;

    private List<Product> products;

    SalesFormActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (SalesFormActivity)getActivity();

        if(view == null){
            view = inflater.inflate(R.layout.sales_form_stock_fragment, container, false);
        }else{
            ((ViewGroup) view.getParent()).removeView(view);
        }
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
                    clearStocks();
                    ((SalesFormActivity) getActivity()).pager.setCurrentItem(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        products = productDao.loadAll();

        if(parent.sale.getUuid() != null){
            populateFields();
        }

        return view;
    }

    private void clearStocks(){
        for(View row : rows){
            ((ViewGroup)row.getParent()).removeView(row);
            parent.stocks = new ArrayList<StokeData>();
            rows = new ArrayList<View>();
        }
    }

    private void populateFields(){
        if(parent.sale.getDoYouStockOrsZinc() != null){
            if(parent.sale.getDoYouStockOrsZinc() == true){
                aq.id(R.id.spn_do_you_stock_ors_and_zinc).setSelection(1);
            }else{
                aq.id(R.id.spn_do_you_stock_ors_and_zinc).setSelection(2);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(rows != null){
            for(View row: rows){
                ((ViewGroup)row.getParent()).removeView(row);
            }
        }

        ArrayList<StokeData> temp = new ArrayList<StokeData>();
        temp.addAll(parent.stocks);

        rows = new ArrayList<View>();
        parent.stocks = new ArrayList<StokeData>();

        for(int i = 0; i < temp.size(); i++){
            addRow(temp.get(i));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StokeData stock;
        Product product;
        int productIndex;
        try{
            for(View row : rows){
                AQuery a = new AQuery(row);
                productIndex = a.id(R.id.product).getSelectedItemPosition();
                product = products.get(productIndex);
                String quantity = a.id(R.id.txt_quantity).getText().toString();

                stock = parent.stocks.get(rows.indexOf(row));
                if(!quantity.isEmpty()){
                    stock.setQuantity(Integer.parseInt(quantity));
                }
                stock.setProductId(product.getUuid());
            }
        }catch(Exception ex){

        }
    }

    private void addRow(final StokeData stock){
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.stock_form_row, null);

        AQuery a = new AQuery(row);

        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows.remove(row);
                parent.stocks.remove(stock);
                LinearLayout parent = (LinearLayout) v.getParent();
                LinearLayout root = (LinearLayout) parent.getParent();
                LinearLayout top = (LinearLayout) root.getParent();
                rowContainer.removeView(top);
            }
        });

        Spinner spinner = (Spinner)row.findViewById(R.id.product);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item, products.toArray(new Product[products.size()])));

        try{
            if(stock.getQuantity() != 0){
                a.id(R.id.txt_quantity).text(Integer.toString(stock.getQuantity()));
            }
            int index = 0;
            if(stock.getProductId() != null){
                for(Product p: products){
                    if(p.getUuid().equalsIgnoreCase(stock.getProductId())){
                        index = products.indexOf(p);
                        break;
                    }
                }
            }
            spinner.setSelection(index);
        }catch(Exception ex){
            Utils.log("Error while adding row -> " + ex.getMessage());
        }

        rowContainer.addView(row);
        rows.add(row);
        parent.stocks.add(stock);
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            productDao = daoSession.getProductDao();
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

        if(parent.stocks.size() == 0 && stocksZincOrs.equalsIgnoreCase("Yes")){
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
            stock = parent.stocks.get(rows.indexOf(row));
            stock.setQuantity(Integer.parseInt(quantity));
            stock.setProduct(product);
            stock.setProductId(product.getUuid());

            i++;
        }

        SalesFormActivity ac = (SalesFormActivity)getActivity();
        ac.sale.setDoYouStockOrsZinc(stocksZincOrs.equalsIgnoreCase("Yes"));

        return true;
    }
}
