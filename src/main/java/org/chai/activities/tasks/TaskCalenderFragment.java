package org.chai.activities.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.adapter.TaskListAdapter;
import org.chai.model.*;
import org.chai.rest.RestClient;
import org.chai.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by victor on 12/8/14.
 */
public class TaskCalenderFragment extends Fragment {
    public final static String STATUS_NEW = "new", STATUS_COMPLETE = "complete", STATUS_CANCELLED = "cancelled";
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    TaskListAdapter taskListAdapter;
    private ListView listView;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_calender_fragment, container, false);
        initialiseGreenDao();
        taskListAdapter = new TaskListAdapter(getActivity(), loadTasksFromDb((getResources().getStringArray(R.array.task_filters))[0]));
        listView = (ListView) view.findViewById(R.id.calender_tasks_list_view);
        spinner = (Spinner) view.findViewById(R.id.task_filter_spinner);
        listView.setAdapter(taskListAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = (String) spinner.getSelectedItem(); 
                taskListAdapter = new TaskListAdapter(getActivity(), loadTasksFromDb(item));
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
                if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
                    CommercialFormFragment commercialFormActivity = new CommercialFormFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("taskId", ((Task) adapterView.getItemAtPosition(position)).getId());
                    commercialFormActivity.setArguments(bundle);
                    ((BaseContainerFragment)getParentFragment()).replaceFragment(commercialFormActivity, true);
                }else{
                    DetailersActivity detailersActivity = new DetailersActivity();
                    Bundle bundle = new Bundle();
                    bundle.putLong("taskId", ((Task) adapterView.getItemAtPosition(position)).getId());
                    detailersActivity.setArguments(bundle);
                    ((BaseContainerFragment)getParentFragment()).replaceFragment(detailersActivity,true);
                }

            }
        });
        return view;
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<Task> loadTasksFromDb(String dueDateString) {
        String[] choices = getResources().getStringArray(R.array.task_filters);

        int itemPosition = Utils.getItemPosition(dueDateString,choices);
        QueryBuilder<Task> taskQueryBuilder = taskDao.queryBuilder();
        List<Task> outstandingTasks=null;
        if(itemPosition==1){
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.lt(Utils.addToDate(new Date(),0)),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE)).list();
        }else{
            Date dueDateOffset = Utils.addToDate(new Date(),itemPosition);
            Date dueDatemax = Utils.addToDate(new Date(),itemPosition+1);
            Log.i("Due Date:",dueDateOffset.toString()+":max-"+dueDatemax.toString());
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.between(dueDateOffset, dueDatemax),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE)).list();
        }
        return outstandingTasks;
    } 
}
