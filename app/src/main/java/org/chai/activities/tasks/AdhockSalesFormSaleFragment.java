package org.chai.activities.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zed on 11/10/2015.
 */
public class AdhockSalesFormSaleFragment extends Fragment {
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
    private List<ProductGroup> groups;

    private ArrayList<String> brands;
    private ArrayList<String> sizes;
    private ArrayList<String> formulations;

    AdhockSalesFormActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = (AdhockSalesFormActivity)getActivity();
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
                    ((AdhockSalesFormActivity) getActivity()).pager.setCurrentItem(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allProducts = productDao.queryBuilder().orderAsc(ProductDao.Properties.Name).list();
        groups = productGroupDao.queryBuilder().orderAsc(ProductGroupDao.Properties.Name).list();

        ArrayList<ProductGroup> filteredGroups = new ArrayList<>();
        for(ProductGroup group: groups){
            if(group.getProducts().size() > 0){
                filteredGroups.add(group);
            }
        }

        groups = filteredGroups;

        ArrayList<Product> filtered = new ArrayList<>();
        for(Product p: allProducts){
            if(!p.getName().contains("Deleted Product")){
                filtered.add(p);
            }
        }

        allProducts = filtered;

        return view;
    }

    private void clearSales(){
        if(rows != null){
            try{
                for(View row : rows){
                    ((ViewGroup)row.getParent()).removeView(row);
                    parent.sales = new ArrayList<>();
                    rows = new ArrayList<>();
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

        ArrayList<SaleData> temp = new ArrayList<>();
        temp.addAll(parent.sales);

        rows = new ArrayList<>();
        parent.sales = new ArrayList<>();

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

        a.id(R.id.txt_quantity).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    sale.setQuantity(Integer.parseInt(s.toString()));
                }catch (Exception ex){
                    sale.setQuantity(0);
                }
                refreshOrderTotal();
            }
        });
        a.id(R.id.txt_unit_price).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    sale.setPrice(Integer.parseInt(s.toString()));
                } catch (Exception ex) {
                    sale.setPrice(0);
                }
                refreshOrderTotal();
            }
        });

        final Spinner spinner = (Spinner)row.findViewById(R.id.product);
        spinner.setAdapter(new ProductArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, allProducts.toArray(new Product[allProducts.size()])));

        final Spinner group = (Spinner)row.findViewById(R.id.group);
        final Spinner brand = (Spinner)row.findViewById(R.id.brand);
        final Spinner size = (Spinner)row.findViewById(R.id.size);
        final Spinner formulation = (Spinner)row.findViewById(R.id.formulation);

        group.setAdapter(new ProductGroupAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, groups.toArray(new ProductGroup[groups.size()])));
        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(group.getTag() != null || sale.getProductId() == null){
                    ProductGroup g = ((ProductGroupAdapter) group.getAdapter()).getItem(position);
                    setBrands(brand, g);
                }
                group.setTag(1);
                ((TextView)group.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(brand.getTag() != null || sale.getProductId() == null){
                    ProductGroup g = (ProductGroup)group.getSelectedItem();
                    String selectedBrand = parent.getAdapter().getItem(position).toString();
                    setSizes(size, selectedBrand, g);
                }
                brand.setTag(1);
                ((TextView)brand.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(size.getTag() != null || sale.getProductId() == null){
                    ProductGroup g = (ProductGroup)group.getSelectedItem();
                    String selectedBrand = brand.getSelectedItem().toString();
                    String selectedSize = parent.getAdapter().getItem(position).toString();
                    setFormulations(formulation, selectedBrand, selectedSize, g);
                }
                size.setTag(1);
                ((TextView)size.getChildAt(0)).setTextSize(10);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        formulation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(formulation.getTag() != null || sale.getProductId() == null){
                    formulations = new ArrayList<>();
                    ProductGroup g = (ProductGroup)group.getSelectedItem();
                    List<Product> products = g.getProducts();
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

                formulation.setTag(1);
                ((TextView)formulation.getChildAt(0)).setTextSize(10);
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
                a.id(R.id.txt_unit_price).text(Integer.toString(sale.getPrice()));
            }

            int index = 0;
            if(sale.getProductId() != null){
                ProductGroup g = sale.getProduct().getProductGroup();

                for(int i = 0; i < group.getAdapter().getCount(); i++){
                    String s = ((ProductGroup)group.getAdapter().getItem(i)).getName();
                    if(s.equalsIgnoreCase(sale.getProduct().getProductGroup().getName())){
                        group.setSelection(i);
                        break;
                    }
                }

                setBrands(brand, g);
                setSizes(size, sale.getProduct().getName(), g);
                setFormulations(formulation, sale.getProduct().getName(), sale.getProduct().getUnitOfMeasure(), g);

                for(int i = 0; i < brand.getAdapter().getCount(); i++){
                    String s = brand.getAdapter().getItem(i).toString();
                    if(s.equalsIgnoreCase(sale.getProduct().getName())){
                        brand.setSelection(i);
                        break;
                    }
                }

                for(int i = 0; i < size.getAdapter().getCount(); i++){
                    String s = size.getAdapter().getItem(i).toString();
                    if(s.equalsIgnoreCase(sale.getProduct().getUnitOfMeasure())){
                        size.setSelection(i);
                        break;
                    }
                }

                for(int i = 0; i < formulation.getAdapter().getCount(); i++){
                    String s = formulation.getAdapter().getItem(i).toString();
                    if(s.equalsIgnoreCase(sale.getProduct().getFormulation())){
                        formulation.setSelection(i);
                        break;
                    }
                }

                for(Product p: allProducts){
                    if(p.getUuid().equalsIgnoreCase(sale.getProductId())){
                        index = allProducts.indexOf(p);
                        break;
                    }
                }

                spinner.setSelection(index);
            }
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

    private void setBrands(Spinner spinner, ProductGroup group){
        List<Product> products = group.getProducts();
        brands = new ArrayList<>();
        for (Product p : products) {
            if (!brands.contains(p.getName()) && !p.getName().contains("Deleted Product")) {
                brands.add(p.getName());
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brands.toArray(new String[brands.size()])));
    }

    private void setSizes(Spinner spinner, String brand, ProductGroup group){
        List<Product> products = group.getProducts();
        sizes = new ArrayList<>();
        for(Product p: products){
            if(p.getUnitOfMeasure() != null){
                if(p.getName().equalsIgnoreCase(brand) && !sizes.contains(p.getUnitOfMeasure())){
                    sizes.add(p.getUnitOfMeasure());
                }
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizes.toArray(new String[sizes.size()])));
    }

    private void setFormulations(Spinner spinner, String brand, String size, ProductGroup group){
        List<Product> products = group.getProducts();
        formulations = new ArrayList<>();
        for(Product p: products){
            if(p.getFormulation() != null && p.getUnitOfMeasure() != null){
                if(p.getName().equalsIgnoreCase(brand) && p.getUnitOfMeasure().equalsIgnoreCase(size) && !formulations.contains(p.getFormulation())){
                    formulations.add(p.getFormulation());
                }
            }
        }
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, formulations.toArray(new String[formulations.size()])));
    }

    private void refreshOrderTotal(){
        int total = 0;
        for(SaleData item : parent.sales){
            total += (item.getQuantity() * item.getPrice());
        }
        String totalStr = "Total: UGX " + NumberFormat.getNumberInstance(Locale.US).format(total);
        aq.id(R.id.txt_order_total).text(totalStr);
        //setTitle(totalStr);
    }
}
