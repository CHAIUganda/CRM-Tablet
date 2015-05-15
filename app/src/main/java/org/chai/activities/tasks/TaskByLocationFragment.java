package org.chai.activities.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.activities.forms.MalariaFormActivity;
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.adapter.TaskListAdapter;
import org.chai.model.CustomerDao;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.District;
import org.chai.model.DistrictDao;
import org.chai.model.Subcounty;
import org.chai.model.SubcountyDao;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.model.User;
import org.chai.rest.RestClient;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by victor on 12/8/14.
 */
public class TaskByLocationFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private SubcountyDao subcountyDao;
    private DistrictDao districtDao;

    TaskListAdapter adapter;
    private ListView listView;

    private Spinner districtSpinner;
    private Spinner subcountySpinner;

    private List<Task> items;

    AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_by_location, container, false);
        initialiseGreenDao();
        aq = new AQuery(view);

        listView = (ListView) view.findViewById(R.id.location_tasks_list_view);
        districtSpinner = (Spinner) view.findViewById(R.id.task_location_district_spinner);
        subcountySpinner = (Spinner)view.findViewById(R.id.task_location_subcounty_spinner);

        List<Subcounty> subcountiesList = subcountyDao.loadAll();
        List<District> districtList = districtDao.loadAll();

        districtSpinner.setAdapter(new DistrictArrayAdapter(getActivity(),R.id.task_location_district_spinner,districtList.toArray(new District[districtList.size()])));
        subcountySpinner.setAdapter(new SubcountyArrayAdapter(getActivity(), R.id.task_location_subcounty_spinner, subcountiesList.toArray(new Subcounty[subcountiesList.size()])));

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String districtId = ((District) districtSpinner.getSelectedItem()).getUuid();
                List<Subcounty> subcountyList = subcountyDao.queryBuilder().where(SubcountyDao.Properties.DistrictId.eq(districtId)).list();
                subcountySpinner.setAdapter(new SubcountyArrayAdapter(getActivity(), R.id.task_location_subcounty_spinner, subcountyList.toArray(new Subcounty[subcountyList.size()])));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subcountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String subcountyId = ((Subcounty) subcountySpinner.getSelectedItem()).getUuid();

                Query query = taskDao.queryBuilder().where(new WhereCondition.StringCondition("T.'"+TaskDao.Properties.Status.columnName+"' != '"+ HomeActivity.STATUS_COMPLETE+"' and T.'"+TaskDao.Properties.Status.columnName+"' != '"+HomeActivity.STATUS_CANCELLED+"' and T.'"+TaskDao.Properties.
                        CustomerId.columnName + "' IN " + "(SELECT " + CustomerDao.Properties.Uuid.columnName + " FROM " + CustomerDao.TABLENAME + " C WHERE C.'" + CustomerDao.Properties.SubcountyId.columnName + "' = '" + subcountyId+"')")).build();
                items = query.list();
                adapter = new TaskListAdapter(getActivity(), items);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(items.size() == 0){
                    aq.id(R.id.location_tasks_list_view).gone();
                    aq.id(R.id.txt_no_tasks).visible();
                }else{
                    aq.id(R.id.location_tasks_list_view).visible();
                    aq.id(R.id.txt_no_tasks).gone();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task task = (Task) adapterView.getItemAtPosition(position);

                Intent i = new Intent();

                i.putExtra("task_id", task.getUuid());
                i.putExtra("id", task.getCustomerId());

                if(RestClient.getRole().equalsIgnoreCase(User.ROLE_SALES)){
                    i.setClass(getActivity(), SalesFormActivity.class);
                }else{
                    i.setClass(getActivity(), MalariaFormActivity.class);
                }

                getActivity().startActivity(i);
            }
        });

        registerForContextMenu(view.findViewById(R.id.location_tasks_list_view));

        return view;
    }

    private void initialiseGreenDao() {
        try {
             UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
            subcountyDao = daoSession.getSubcountyDao();
            districtDao = daoSession.getDistrictDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        int m = -1;
        if(RestClient.getRole().equalsIgnoreCase(User.ROLE_DETAILER)){
            m = R.menu.detailer_task_menu;
        }else{
            m = R.menu.sale_task_menu;
        }
        inflater.inflate(m, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        int position = (int) info.id;
        Task task = items.get(position);
        Utils.log("Context menu selected -> " + menuItem.getItemId());
        switch (menuItem.getItemId()) {
            case R.id.cancel_task:
                askBeforeDelete(position).show();
                break;
            case R.id.detail_malaria:
                Intent i = new Intent(getActivity(), MalariaFormActivity.class);
                i.putExtra("id", task.getCustomerId());
                i.putExtra("task_id", task.getUuid());
                startActivity(i);
                break;
            case R.id.detail_sale:
                Intent in = new Intent(getActivity(), SalesFormActivity.class);
                in.putExtra("id", task.getCustomerId());
                in.putExtra("task_id", task.getUuid());
                startActivity(in);
                break;
        }
        return true;
    }

    private AlertDialog.Builder askBeforeDelete(final int position) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View promptView = inflater.inflate(R.layout.task_cancel_prompt,null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(promptView);

        dialog.setTitle("Cancel Task")
                .setMessage("Are you sure you want to cancel this task?")
                .setPositiveButton("Cancel Task", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String reason = ((EditText) promptView.findViewById(R.id.promptDialogUserInput)).getText().toString();
                        Task task = items.get(position);
                        task.setStatus(TaskMainFragment.STATUS_CANCELLED);
                        task.setDescription(task.getDescription() + "(" + reason + ")");
                        taskDao.update(task);
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }
}