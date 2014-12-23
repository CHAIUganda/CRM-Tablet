package org.chai.activities.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import org.chai.adapter.DistrictArrayAdapter;
import org.chai.adapter.ParishArrayAdapter;
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
                Long districtId = ((District) districtSpinner.getSelectedItem()).getId();
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
                Long subcountyId = ((Subcounty) subcountySpinner.getSelectedItem()).getId();

                Query query = taskDao.queryBuilder().where(new WhereCondition.StringCondition("T.'"+TaskDao.Properties.
                        CustomerId.columnName + "' IN " + "(SELECT " + CustomerDao.Properties.Id.columnName + " FROM " + CustomerDao.TABLENAME + " C WHERE C.'" + CustomerDao.Properties.SubcountyId.columnName + "' = " + subcountyId+")")).build();
                List<Task> taskList = query.list();
                listView.setAdapter(new TaskListAdapter(getActivity(),taskList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(RestClient.role.equalsIgnoreCase(User.ROLE_DETAILER)){
                    Intent intent = new Intent(getActivity(), DetailersActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("taskId", ((Task) adapterView.getItemAtPosition(position)).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putLong("taskId", ((Task) adapterView.getItemAtPosition(position)).getId());
                    Intent intent = new Intent(getActivity(), CommercialFormActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
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
            subcountyDao = daoSession.getSubcountyDao();
            districtDao = daoSession.getDistrictDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
