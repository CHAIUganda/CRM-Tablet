package org.chai.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.AdhockSale;
import org.chai.model.Customer;
import org.chai.model.CustomerContact;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.SubcountyDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zed on 11/10/2015.
 */
public class AdhockSaleHistoryAdapter extends ArrayAdapter<AdhockSale>{
    private int lastPosition = -1;

    private ArrayList<AdhockSale> originalItems;
    private ArrayList<AdhockSale> filteredItems;
    private HistoryFilter historyFilter;
    SimpleDateFormat f;

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SubcountyDao subcountyDao;

    public AdhockSaleHistoryAdapter(Context context, int resource, List<AdhockSale> items) {
        super(context, resource, items);

        originalItems = new ArrayList<>();
        originalItems.addAll(items);

        filteredItems = new ArrayList<>();
        filteredItems.addAll(items);

        f = new SimpleDateFormat("EE, d MMM yyyy h:m a");

        initialiseGreenDao();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null){
            LayoutInflater inflator = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflator.inflate(R.layout.history_item_row, null);
        }

        AdhockSale s = getItem(position);
        Customer c = s.getCustomer();
        CustomerContact contact = c.getCustomerContacts().get(0);

        AQuery aq = new AQuery(row);
        aq.id(R.id.txt_customer_name).text(c.getOutletName());
        Date d = s.getLastUpdated();

        if(d != null){
            aq.id(R.id.txt_time).text(f.format(d));
        }
        try{
            aq.id(R.id.txt_customer_contact).text(contact.getContact() + " - " + c.getSubcounty().getName() + " | " + c.getSubcounty().getDistrict().getName());
            aq.id(R.id.img_segment).gone();
        }catch (Exception ex){
            aq.id(R.id.txt_customer_contact).gone();
            Utils.log("Error getting customer details");
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        return row;
    }

    @Override
    public Filter getFilter() {
        if(historyFilter == null){
            historyFilter = new HistoryFilter();
        }

        return historyFilter;
    }

    private class HistoryFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = originalItems;
                results.count = originalItems.size();
            }else{
                List<AdhockSale> nFilteredList = new ArrayList<>();
                for(AdhockSale m : originalItems){
                    Customer c = m.getCustomer();
                    if(c != null){
                        if(c.getOutletName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))){
                            nFilteredList.add(m);
                        }else if(c.getSubcounty() != null){
                            if(c.getSubcounty().getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))){
                                nFilteredList.add(m);
                            }else if(c.getSubcounty().getDistrict() != null){
                                if(c.getSubcounty().getDistrict().getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                                    nFilteredList.add(m);
                                }
                            }
                        }
                    }
                }

                results.values = nFilteredList;
                results.count = nFilteredList.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<AdhockSale> filtered = (List<AdhockSale>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0; i < filtered.size(); i++){
                add(filtered.get(i));
            }
            notifyDataSetChanged();
        }
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            subcountyDao = daoSession.getSubcountyDao();
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
