package org.chai.activities.calls;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.activities.tasks.TakeOrderFragment;
import org.chai.adapter.OrderListAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Order;
import org.chai.model.OrderDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

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
//        orderListView.setItemsCanFocus(true);
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
//                displayPopupWindow(view,"Testing");
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

    private void displayPopupWindow(View anchorView,String message) {
        PopupWindow popup = new PopupWindow(getActivity());
        View layout = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
        popup.setContentView(layout);
        TextView popupText = (TextView)layout.findViewById(R.id.popupTxt);
        popupText.setText(message);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
    }


}
