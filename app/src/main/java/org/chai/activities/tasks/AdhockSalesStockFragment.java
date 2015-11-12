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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.adapter.ProductArrayAdapter;
import org.chai.adapter.ProductGroupAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Product;
import org.chai.model.ProductDao;
import org.chai.model.ProductGroup;
import org.chai.model.ProductGroupDao;
import org.chai.model.StokeData;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 11/10/2015.
 */
public class AdhockSalesStockFragment extends Fragment {
    AQuery aq;
    View view;
    LinearLayout rowContainer;
    ArrayList<View> rows;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ProductGroupDao productGroupDao;
    private ProductDao productDao;

    private List<Product> allProducts;
    private List<Product> products;
    private List<ProductGroup> groups;

    private ArrayList<String> brands;
    private ArrayList<String> sizes;
    private ArrayList<String> formulations;

    AdhockSalesFormActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (AdhockSalesFormActivity)getActivity();

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
                    ((AdhockSalesFormActivity) getActivity()).pager.setCurrentItem(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        products = productDao.queryBuilder().orderAsc(ProductDao.Properties.Name).list();
        groups = productGroupDao.queryBuilder().orderAsc(ProductGroupDao.Properties.Name).list();

        ArrayList<ProductGroup> filteredGroups = new ArrayList<>();
        for(ProductGroup group: groups){
            if(group.getProducts().size() > 0){
                filteredGroups.add(group);
            }
        }

        groups = filteredGroups;

        ArrayList<Product> filtered = new ArrayList<>();
        for(Product p: products){
            if(!p.getName().contains("Deleted Product")){
                filtered.add(p);
            }
        }

        products = filtered;
        allProducts = filtered;

        if(parent.sale.getUuid() != null){
            populateFields();
        }

        return view;
    }

    private void clearStocks(){
        if(rows != null){
            try{
                for(View row : rows){
                    ((ViewGroup)row.getParent()).removeView(row);
                    parent.stocks = new ArrayList<>();
                    rows = new ArrayList<>();
                }
            }catch (Exception ex){

            }
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

        ArrayList<StokeData> temp = new ArrayList<>();
        temp.addAll(parent.stocks);

        rows = new ArrayList<>();
        parent.stocks = new ArrayList<>();

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
                product = allProducts.get(productIndex);
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

        final View row = inflator.inflate(R.layout.sale_stock_form_row, null);

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

        final Spinner spinner = (Spinner)row.findViewById(R.id.product);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, allProducts.toArray(new Product[allProducts.size()])));

        final Spinner brand = (Spinner)row.findViewById(R.id.brand);
        final Spinner size = (Spinner)row.findViewById(R.id.size);
        final Spinner formulation = (Spinner)row.findViewById(R.id.formulation);

        Spinner group = (Spinner)row.findViewById(R.id.group);
        group.setAdapter(new ProductGroupAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, groups.toArray(new ProductGroup[groups.size()])));
        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductGroup g = ((ProductGroupAdapter) parent.getAdapter()).getItem(position);
                products = g.getProducts();
                brands = new ArrayList<>();
                for (Product p : products) {
                    if (!brands.contains(p.getName()) && !p.getName().contains("Deleted Product")) {
                        brands.add(p.getName());
                    }
                }
                brand.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brands.toArray(new String[brands.size()])));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sizes = new ArrayList<>();
                String selectedBrand = parent.getAdapter().getItem(position).toString();
                for(Product p: products){
                    if(p.getUnitOfMeasure() != null){
                        if(p.getName().equalsIgnoreCase(selectedBrand) && !sizes.contains(p.getUnitOfMeasure())){
                            sizes.add(p.getUnitOfMeasure());
                        }
                    }
                }
                size.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizes.toArray(new String[sizes.size()])));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formulations = new ArrayList<>();
                String selectedBrand = brand.getSelectedItem().toString();
                String selectedSize = parent.getAdapter().getItem(position).toString();
                for(Product p: products){
                    if(p.getFormulation() != null && p.getUnitOfMeasure() != null){
                        if(p.getName().equalsIgnoreCase(selectedBrand) && p.getUnitOfMeasure().equalsIgnoreCase(selectedSize) && !formulations.contains(p.getFormulation())){
                            formulations.add(p.getFormulation());
                        }
                    }
                }
                formulation.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, formulations.toArray(new String[formulations.size()])));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        formulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formulations = new ArrayList<>();
                String selectedBrand = "";
                try{
                    selectedBrand = brand.getSelectedItem().toString();
                }catch (Exception ex){

                }
                String selectedSize = "";
                try{
                    selectedSize = size.getSelectedItem().toString();
                }catch (Exception ex){

                }
                String selectedFormulation = "";
                try{
                    selectedFormulation = parent.getAdapter().getItem(position).toString();
                }catch (Exception ex){

                }
                for (Product p : products) {
                    if (p.getFormulation() != null && p.getUnitOfMeasure() != null && p.getFormulation() != null) {
                        if (p.getName().equalsIgnoreCase(selectedBrand) && p.getUnitOfMeasure().equalsIgnoreCase(selectedSize) && selectedFormulation.equalsIgnoreCase(p.getFormulation())) {
                            int index = 0;
                            for(Product pp: allProducts){
                                if(pp.getUuid().equalsIgnoreCase(p.getUuid())){
                                    index = allProducts.indexOf(p);
                                    break;
                                }
                            }

                            spinner.setSelection(index);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            productGroupDao = daoSession.getProductGroupDao();
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

        AdhockSalesFormActivity ac = (AdhockSalesFormActivity)getActivity();
        ac.sale.setDoYouStockOrsZinc(stocksZincOrs.equalsIgnoreCase("Yes"));

        return true;
    }
}
