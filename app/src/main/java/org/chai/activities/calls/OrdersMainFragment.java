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
import org.chai.activities.tasks.TakeOrderFragment;
import org.chai.adapter.OrderListAdapter;
import org.chai.model.*;
import org.chai.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 1/6/15.
 */
public class OrdersMainFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private OrderDao orderDao;

    private List<Order> orderList = null;
    private ListView orderListView;
    private OrderListAdapter orderListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.order_main_list,container,false);
        initialiseGreenDao();
        orderList = new ArrayList<Order>();
        orderList.addAll(orderDao.loadAll());
        orderListView = (ListView)view.findViewById(R.id.orderslistview);
        orderListAdapter = new OrderListAdapter(getActivity(), orderList);
        orderListView.setAdapter(orderListAdapter);


        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Order order =  (Order)adapterView.getItemAtPosition(position);
                TakeOrderFragment takeOrderFragment = new TakeOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("orderId",order.getUuid());
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
            orderDao = daoSession.getOrderDao();
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
                        orderDao.delete(orderList.get(position));
                        orderList.remove(position);
                        orderListAdapter.notifyDataSetChanged();
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
