package org.chai.activities.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import de.greenrobot.dao.query.QueryBuilder;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.adapter.TaskListAdapter;
import org.chai.model.*;
import org.chai.rest.RestClient;
import org.chai.util.Utils;

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
    private List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_calender_fragment, container, false);
        initialiseGreenDao();
        taskList = loadTasksFromDb((getResources().getStringArray(R.array.task_filters))[0]);
        taskListAdapter = new TaskListAdapter(getActivity(), taskList);
        listView = (ListView) view.findViewById(R.id.calender_tasks_list_view);
        spinner = (Spinner) view.findViewById(R.id.task_filter_spinner);
        listView.setAdapter(taskListAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = (String) spinner.getSelectedItem();
                taskList.clear();
                taskList = loadTasksFromDb(item);
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
                if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
                    SaleslFormFragment commercialFormActivity = new SaleslFormFragment();
                    Bundle bundle = new Bundle();
                    Task itemAtPosition = (Task) adapterView.getItemAtPosition(position);
                    bundle.putString("taskId", itemAtPosition.getUuid());
                    commercialFormActivity.setArguments(bundle);
                    ((BaseContainerFragment)getParentFragment()).replaceFragment(commercialFormActivity, true);
                }else{
                    DetailersActivity detailersActivity = new DetailersActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", ((Task) adapterView.getItemAtPosition(position)).getUuid());
                    detailersActivity.setArguments(bundle);
                    ((BaseContainerFragment)getParentFragment()).replaceFragment(detailersActivity,true);
                }

            }
        });

        registerForContextMenu(view.findViewById(R.id.calender_tasks_list_view));
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
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.lt(Utils.addToDate(new Date(),0)),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_CANCELLED)).list();
        }else{
            if(itemPosition>0){
                itemPosition=itemPosition-1;
            }
            Date dueDateOffset = Utils.addToDate(new Date(),itemPosition);
            Date dueDatemax = Utils.addToDate(new Date(),itemPosition+1);
            Log.i("Due Date:",dueDateOffset.toString()+":max-"+dueDatemax.toString());
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.between(dueDateOffset, dueDatemax),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_CANCELLED)).list();
        }
        return outstandingTasks;
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
