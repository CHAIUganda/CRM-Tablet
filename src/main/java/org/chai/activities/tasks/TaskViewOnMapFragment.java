package org.chai.activities.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import de.greenrobot.dao.query.QueryBuilder;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.model.*;
import org.chai.rest.RestClient;
import org.chai.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by victor on 12/11/14.
 */
public class TaskViewOnMapFragment extends Fragment {
    private static View view;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private GoogleMap googleMap;
    private HashMap<String,Long> markers = new HashMap<String, Long>();
    private Spinner calenderSpinner;
    private AsyncTaskRunner runner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view = inflater.inflate(R.layout.task_map_layout, container, false);
        }catch (InflateException ex){

        }
        initialiseGreenDao();
        googleMap =   ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
      if(googleMap!=null){
          googleMap.setMyLocationEnabled(true);
          googleMap.getUiSettings().setZoomControlsEnabled(true);
          googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3733, 32.2903), 8.0f));
          List<Task> tasks = taskDao.loadAll();
          if(!tasks.isEmpty()){
              addTasksToMap(tasks, googleMap);
          }
          googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
              @Override
              public boolean onMarkerClick(Marker marker) {
                  Long taskId = markers.get(marker.getId());
                  Task task = taskDao.load(taskId);
                  if(task!= null){
                      marker.showInfoWindow();
                  }else{
                      Toast.makeText(getActivity(),"Task is unknown",Toast.LENGTH_LONG).show();
                  }
                  return false;
              }
          });
          googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
              @Override
              public void onInfoWindowClick(Marker marker) {
                  Long taskId = markers.get(marker.getId());
                  runner = new AsyncTaskRunner();
                  runner.execute(taskId);
                  marker.hideInfoWindow();
              }
          });
      }
        calenderSpinner = (Spinner)view.findViewById(R.id.map_filter_spinner);
        calenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)calenderSpinner.getSelectedItem();
                List<Task> taskList = loadTasksFromDb(item);
                googleMap.clear();
                addTasksToMap(taskList,googleMap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    private void addTasksToMap(List<Task> taskList, GoogleMap map){
        for(Task task:taskList){
            Customer customer = task.getCustomer();
            if(customer!=null){
                double latitude = customer.getLatitude();
                double longitude = customer.getLongitude();
                Log.i("Latitude============",latitude+"");
                Log.i("Longitude===========",longitude+"");
                if(longitude != 0&&latitude!= 0){
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title(task.getDescription());
                    Marker marker = map.addMarker(markerOptions);
                    markers.put(marker.getId(),task.getId());
                }
            }
        }

    }
    private List<Task> loadTasksFromDb(String dueDateString) {
        String[] choices = getResources().getStringArray(R.array.task_filters);

        int itemPosition = Utils.getItemPosition(dueDateString, choices);
        QueryBuilder<Task> taskQueryBuilder = taskDao.queryBuilder();
        List<Task> outstandingTasks=null;
        if(itemPosition==1){
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.lt(new Date()),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE)).list();
        }else{
            Date dueDateOffset = Utils.addToDate(new Date(),itemPosition);
            Date dueDatemax = Utils.addToDate(new Date(),itemPosition+1);
            Log.i("Due Date:",dueDateOffset.toString()+":max-"+dueDatemax.toString());
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.between(dueDateOffset, dueDatemax),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE)).list();
        }
        return outstandingTasks;
    }

    private void showForm(Long taskId){
        if (RestClient.role.equalsIgnoreCase(User.ROLE_SALES)) {
            CommercialFormFragment commercialFormActivity = new CommercialFormFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("taskId", taskId);
            commercialFormActivity.setArguments(bundle);
            ((BaseContainerFragment) getParentFragment()).replaceFragment(commercialFormActivity, true);
        } else {
            DetailersActivity detailersActivity = new DetailersActivity();
            Bundle bundle = new Bundle();
            bundle.putLong("taskId", taskId);
            detailersActivity.setArguments(bundle);
            ((BaseContainerFragment)getParentFragment()).replaceFragment(detailersActivity,true);
        }
        runner = null;
    }
    private class AsyncTaskRunner extends AsyncTask<Long, String, String>{

        private Long taskId;

        @Override
        protected String doInBackground(Long... longs) {
            taskId = longs[0];
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
           showForm(taskId);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        //check the state of the task
            runner.cancel(true);
    }
}
