package org.chai.activities.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.adapter.SubcountyArrayAdapter;
import org.chai.adapter.TaskListAdapter;
import org.chai.model.*;
import org.chai.rest.RestClient;

import java.util.List;

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
    TaskListAdapter taskListAdapter;
    private ListView listView;
    private Spinner districtSpinner;
    private Spinner subcountySpinner;
    private List<Task> taskList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_by_location, container, false);
        initialiseGreenDao();
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

                Query query = taskDao.queryBuilder().where(new WhereCondition.StringCondition("T.'"+TaskDao.Properties.Status.columnName+"' != '"+TaskMainFragment.STATUS_COMPLETE+"' and T.'"+TaskDao.Properties.Status.columnName+"' != '"+TaskMainFragment.STATUS_CANCELLED+"' and T.'"+TaskDao.Properties.
                        CustomerId.columnName + "' IN " + "(SELECT " + CustomerDao.Properties.Uuid.columnName + " FROM " + CustomerDao.TABLENAME + " C WHERE C.'" + CustomerDao.Properties.SubcountyId.columnName + "' = '" + subcountyId+"')")).build();
                taskList = query.list();
                taskListAdapter = new TaskListAdapter(getActivity(), taskList);
                listView.setAdapter(taskListAdapter);
                taskListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (RestClient.role.equalsIgnoreCase(User.ROLE_SALES)) {
                    SaleslFormFragment commercialFormActivity = new SaleslFormFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", ((Task) adapterView.getItemAtPosition(position)).getUuid());
                    commercialFormActivity.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(commercialFormActivity, true);
                } else {
                    DetailersActivity detailersActivity = new DetailersActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", ((Task) adapterView.getItemAtPosition(position)).getUuid());
                    detailersActivity.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(detailersActivity, true);
                }
            }
        });
        registerForContextMenu(view.findViewById(R.id.location_tasks_list_view));
        return view;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
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
        inflater.inflate(R.menu.cancel_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.cancel_task_menu_item:
                try {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int position = (int) info.id;
                    askBeforeDelete(position).show();
                } catch (Exception ex) {

                }
                return true;
        }
        return super.onContextItemSelected(menuItem);
    }

    private AlertDialog.Builder askBeforeDelete(final int position) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View promptView = inflater.inflate(R.layout.task_cancel_prompt,null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(promptView);

        dialog.setTitle("Cancel Task")
                .setMessage("Are you sure you want to cancel this task?")
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Cancel Task", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String reason = ((EditText) promptView.findViewById(R.id.promptDialogUserInput)).getText().toString();
                        Task task = taskList.get(position);
                        task.setStatus(TaskMainFragment.STATUS_CANCELLED);
                        task.setDescription(task.getDescription() + "(" + reason + ")");
                        taskDao.update(task);
                        taskList.remove(position);
                        taskListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return dialog;

    }
}
