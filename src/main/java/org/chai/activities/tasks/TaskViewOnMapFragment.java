package org.chai.activities.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.model.*;
import org.chai.rest.RestClient;

import java.util.HashMap;
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
    private HashMap<String,Long> markers = new HashMap<String, Long>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.task_map_layout, container, false);
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
                      if(RestClient.role.equalsIgnoreCase(User.ROLE_SALES)){
                          CommercialFormActivity commercialFormActivity = new CommercialFormActivity();
                          Bundle bundle = new Bundle();
                          bundle.putLong("taskId",taskId);
                          commercialFormActivity.setArguments(bundle);
                          ((BaseContainerFragment)getParentFragment()).replaceFragment(commercialFormActivity,true);
                      }else{
                          Bundle bundle = new Bundle();
                          bundle.putLong("taskId",taskId);
                          Intent intent = new Intent(getActivity(), DetailersActivity.class);
                          intent.putExtras(bundle);
                          startActivity(intent);
                      }
                  }else{
                      Toast.makeText(getActivity(),"Task is unknown",Toast.LENGTH_LONG).show();
                  }
                  return false;
              }
          });
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
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latitude,longitude));
                    Marker marker = map.addMarker(markerOptions);
                    markers.put(marker.getId(),task.getId());
                }
            }
        }

    }
}
