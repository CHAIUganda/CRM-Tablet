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
import org.chai.model.SaleData;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 5/3/2015.
 */
public class SalesFormSaleFragment extends Fragment {
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

    SalesFormActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (SalesFormActivity)getActivity();
        initialiseGreenDao();
        view = inflater.inflate(R.layout.sales_form_sale_fragment, container, false);
        aq = new AQuery(view);
        rowContainer = (LinearLayout)view.findViewById(R.id.ln_rows_container);

        aq.id(R.id.btn_add_row).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(new SaleData());
            }
        });

        aq.id(R.id.spn_were_sales_made).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    clearSales();
                    ((SalesFormActivity) getActivity()).pager.setCurrentItem(3);
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

        return view;
    }

    private void clearSales(){
        if(rows != null){
            try{
                for(View row : rows){
                    ((ViewGroup)row.getParent()).removeView(row);
                    parent.sales = new ArrayList<SaleData>();
                    rows = new ArrayList<View>();
                }
            }catch (Exception ex){

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

        ArrayList<SaleData> temp = new ArrayList<SaleData>();
        temp.addAll(parent.sales);

        rows = new ArrayList<View>();
        parent.sales = new ArrayList<SaleData>();

        for(int i = 0; i < temp.size(); i++){
            addRow(temp.get(i));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SaleData sale;
        Product product;
        int productIndex;
        for(View row : rows){
            AQuery a = new AQuery(row);
            productIndex = a.id(R.id.product).getSelectedItemPosition();
            product = allProducts.get(productIndex);
            String quantity = a.id(R.id.txt_quantity).getText().toString();
            String unitPrice = a.id(R.id.txt_unit_price).getText().toString();

            sale = parent.sales.get(rows.indexOf(row));
            if(!quantity.isEmpty()){
                sale.setQuantity(Integer.parseInt(quantity));
            }
            if(!unitPrice.isEmpty()){
                sale.setPrice(Integer.parseInt(unitPrice));
            }
            sale.setProductId(product.getUuid());
        }
    }

    private void addRow(final SaleData sale){
        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row = inflator.inflate(R.layout.sale_form_multidropdown_row, null);
        AQuery a = new AQuery(row);
        final ImageView remove = (ImageView)row.findViewById(R.id.btn_remove_row);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows.remove(row);
                parent.sales.remove(sale);

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
                        }else{
                            spinner.setSelection(0);
                        }
                    }else{
                        spinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try{
            if(sale.getQuantity() != 0){
                a.id(R.id.txt_quantity).text(Integer.toString(sale.getQuantity()));
            }
            if(sale.getPrice() != 0){
                a.id(R.id.txt_unit_price).text(Integer.toString(sale.getQuantity()));
            }
            int index = 0;
            if(sale.getProductId() != null){
                for(Product p: products){
                    if(p.getUuid().equalsIgnoreCase(sale.getProductId())){
                        index = products.indexOf(p);
                        break;
                    }
                }
            }
            spinner.setSelection(index);
        }catch(Exception ex){
            Utils.log("Error loading row -> " + ex.getMessage());
        }
        rowContainer.addView(row);
        rows.add(row);
        parent.sales.add(sale);
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
        String salesMade = aq.id(R.id.spn_were_sales_made).getSelectedItem().toString();
        if(salesMade.isEmpty()){
            Toast.makeText(getActivity(), "Please select wether customer made any sales", Toast.LENGTH_LONG).show();
            return false;
        }

        if(parent.sales.size() == 0 && salesMade.equalsIgnoreCase("Yes")){
            Toast.makeText(getActivity(), "Please enter Zinc and ORS sales made", Toast.LENGTH_LONG).show();
            return false;
        }

        int i = 1;
        SaleData sale;
        Product product;
        int productIndex;
        String quantity, price;

        for(View row : rows){
            AQuery a = new AQuery(row);
            productIndex = a.id(R.id.product).getSelectedItemPosition();
            product = allProducts.get(productIndex);
            quantity = a.id(R.id.txt_quantity).getText().toString();
            if(quantity.isEmpty()){
                Toast.makeText(getActivity(), "Please enter sale quanity on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }
            price = a.id(R.id.txt_unit_price).getText().toString();
            if(price.isEmpty()){
                Toast.makeText(getActivity(), "Please enter unit price on row " + i, Toast.LENGTH_LONG).show();
                return false;
            }

            sale = parent.sales.get(rows.indexOf(row));
            sale.setQuantity(Integer.parseInt(quantity));
            sale.setProduct(product);
            sale.setProductId(product.getUuid());
            sale.setPrice(Integer.parseInt(price));

            i++;
        }

        return true;
    }
}
