package org.chai.activities.tasks;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import de.greenrobot.dao.query.QueryBuilder;
import org.chai.R;
import org.chai.adapter.TaskListAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Task;
import org.chai.model.TaskDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by victor on 10/15/14.
 */
public class TasksMainActivity extends Activity {

    public final static String STATUS_NEW = "new", STATUS_COMPLETE = "complete",STATUS_CANCELLED = "cancelled";
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    TaskListAdapter taskListAdapter;
    ExpandableListView expandableListView;
    List<String> listDataHeader;
    HashMap<String, List<Task>> taskList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_main);
        initialiseGreenDao();
        try {
            //load data
            loadTasksFromDb();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        expandableListView = (ExpandableListView) findViewById(R.id.task_list_main_view);
//        taskListAdapter = new TaskListAdapter(this, taskList);

        expandableListView.setAdapter(taskListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                String selectedHeader = listDataHeader.get(groupPosition);
                Task selectedChild = taskList.get(selectedHeader).get(childPosition);
                Intent intent = new Intent(getApplicationContext(), DetailersActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("taskId", selectedChild.getUuid());
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }
        });
    }

    private void initialiseGreenDao() {
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "chai-crm-db", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
        } catch (Exception ex) {
            Toast.makeText(this, "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadTasksFromDb() {
        listDataHeader = new ArrayList<String>();
        taskList = new HashMap<String, List<Task>>();

        listDataHeader.add("New task");
        listDataHeader.add("Outstanding");
        listDataHeader.add("Scheduled");

        //query all new tasks
        List<Task> newTasks = taskDao.queryBuilder().where(TaskDao.Properties.Status.eq(STATUS_NEW)).list();
        QueryBuilder<Task> taskQueryBuilder = taskDao.queryBuilder();
        List<Task> outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.lt(new Date()),TaskDao.Properties.Status.eq(STATUS_NEW)).list();
        List<Task> scheduledTasks = taskDao.queryBuilder().where(TaskDao.Properties.Status.eq(STATUS_CANCELLED)).list();

        taskList.put(listDataHeader.get(0), newTasks);
        taskList.put(listDataHeader.get(1), outstandingTasks);
        taskList.put(listDataHeader.get(2), scheduledTasks);

    }

}