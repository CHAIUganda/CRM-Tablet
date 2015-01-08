package org.chai.activities.calls;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.activities.tasks.TakeOrderFragment;
import org.chai.adapter.AdhockSalesAdapter;
import org.chai.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/8/15.
 */
public class AdhockSalesListFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AdhockSaleDao adhockSaleDao;

    private List<AdhockSale> adhockSales;
    private ListView salesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.order_main_list,container,false);
        initialiseGreenDao();
        adhockSales = new ArrayList<AdhockSale>();
        adhockSales.addAll(adhockSaleDao.loadAll());
        salesListView = (ListView)view.findViewById(R.id.orderslistview);
        salesListView.setAdapter(new AdhockSalesAdapter(getActivity(),adhockSales));


        salesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Order order =  (Order)adapterView.getItemAtPosition(position);
                TakeOrderFragment takeOrderFragment = new TakeOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("orderId",order.getId());
                takeOrderFragment.setArguments(bundle);
                ((BaseContainerFragment)getParentFragment()).replaceFragment(takeOrderFragment,true);
            }
        });
        registerForContextMenu(view.findViewById(R.id.orderslistview));
        return view;
    }


    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            adhockSaleDao = daoSession.getAdhockSaleDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
