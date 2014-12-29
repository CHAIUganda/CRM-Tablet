package org.chai.activities.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.chai.R;
import org.chai.model.*;

import java.util.List;

/**
 * Created by victor on 12/11/14.
 */
public class TaskViewOnMapFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private GoogleMap googleMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_map_layout, container, false);
        initialiseGreenDao();
        googleMap =   ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
      if(googleMap!=null){
          googleMap.setMyLocationEnabled(true);
          googleMap.getUiSettings().setZoomControlsEnabled(true);
          List<Task> tasks = taskDao.loadAll();
          if(!tasks.isEmpty()){
              addTasksToMap(tasks, googleMap);
          }
      }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (googleMap != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).commit();
            googleMap = null;
        }
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
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude));
                    map.addMarker(marker);
                }
            }
        }

    }
}
