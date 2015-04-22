package org.chai.activities.calls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.activities.tasks.AdhockSaleFragment;
import org.chai.adapter.AdhockSalesAdapter;
import org.chai.model.AdhockSale;
import org.chai.model.AdhockSaleDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

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
    private AdhockSalesAdapter salesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.order_main_list,container,false);
        initialiseGreenDao();
        adhockSales = new ArrayList<AdhockSale>();
        adhockSales.addAll(adhockSaleDao.loadAll());
        salesListView = (ListView)view.findViewById(R.id.orderslistview);
        salesAdapter = new AdhockSalesAdapter(getActivity(), adhockSales);
        salesListView.setAdapter(salesAdapter);


        salesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AdhockSale sale =  (AdhockSale)adapterView.getItemAtPosition(position);
                AdhockSaleFragment makeAdhockSaleFragment = new AdhockSaleFragment();
                Bundle bundle = new Bundle();
                bundle.putString("saleId",sale.getUuid());
                makeAdhockSaleFragment.setArguments(bundle);
                ((BaseContainerFragment)getParentFragment()).replaceFragment(makeAdhockSaleFragment,true);
            }
        });
        registerForContextMenu(view.findViewById(R.id.orderslistview));
        return view;
    }


    private void initialiseGreenDao() {
        try {
             UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            adhockSaleDao = daoSession.getAdhockSaleDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
      public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.delete_context_menu, menu);

    }


    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete_menu_item:
                try{
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int position = (int) info.id;
                   askBeforeDelete(position).show();
                }catch (Exception ex){

                }
                return true;

        }
        return super.onContextItemSelected(menuItem);
    }

    private AlertDialog askBeforeDelete(final int position)
    {
        AlertDialog dialog =new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to Delete the selected Item")
                .setIcon(R.drawable.delete_icon)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        adhockSaleDao.delete(adhockSales.get(position));
                        adhockSales.remove(position);
                        salesAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return dialog;

    }


}
