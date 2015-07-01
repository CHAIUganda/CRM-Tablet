package org.chai.activities.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
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

import org.chai.R;
import org.chai.activities.HomeActivity;
import org.chai.activities.forms.MalariaFormActivity;
import org.chai.model.Customer;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.Task;
import org.chai.model.TaskDao;
import org.chai.model.User;
import org.chai.rest.RestClient;
import org.chai.util.GPSSettingsDialog;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import fr.quentinklein.slt.LocationTracker;

/**
 * Created by victor on 12/11/14.
 */
public class TaskViewOnMapFragment extends Fragment {
    public static int MAX_RADIUS_IN_KM = 5;
    private static View view;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private TaskDao taskDao;
    private HashMap<String,String> markers = new HashMap<String, String>();
    private Spinner calenderSpinner;
    private AsyncTaskRunner runner;


    private int MAP_DEFAULT_ZOOM = 8;
    private double MAP_DEFAULT_LATITUDE =0.3417;
    private double MAP_DEFAULT_LONGITUDE = 32.5811;
    private int MAX_TASKS_TO_SHOW_ON_MAP = 100;

    /*private double MAP_DEFAULT_LATITUDE = 0.7190105;
    private double MAP_DEFAULT_LONGITUDE = 31.4345434;*/

    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay mMyLocationOverlay;
    private ItemizedIconOverlay<OverlayItem> currentLocationOverlay;
    private DefaultResourceProxyImpl resourceProxy;
    private ArrayList<OverlayItem> items;

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

        mapView = (MapView)view.findViewById(R.id.mapview);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);

        resourceProxy = new DefaultResourceProxyImpl(getActivity());

        this.mMyLocationOverlay = new MyLocationNewOverlay(getActivity(),mapView);
        this.mMyLocationOverlay.enableMyLocation();
        this.mMyLocationOverlay.setDrawAccuracyEnabled(true);

        mapController = this.mapView.getController();

        this.mapView.getOverlays().add(mMyLocationOverlay);

        if(LocationTracker.getLocation() != null){
            MAP_DEFAULT_LATITUDE = LocationTracker.getLocation().getLatitude();
            MAP_DEFAULT_LONGITUDE = LocationTracker.getLocation().getLongitude();
        }else{
            new GPSSettingsDialog().show(getActivity().getSupportFragmentManager(), "gps_settings");
        }

        GeoPoint currentLocation = new GeoPoint(MAP_DEFAULT_LATITUDE, MAP_DEFAULT_LONGITUDE);
        mapController.setZoom(MAP_DEFAULT_ZOOM);
        mapController.animateTo(currentLocation);
        calenderSpinner = (Spinner)view.findViewById(R.id.map_filter_spinner);
        calenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)calenderSpinner.getSelectedItem();
                Log.i("selected:",item);
                List<Task> taskList = loadTasksFromDb(item);
                addTasksToMap2(taskList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    private void initialiseGreenDao() {
        try {
             UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            taskDao = daoSession.getTaskDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addTasksToMap2(List<Task> taskList){
        items = new ArrayList<OverlayItem>();
        for(Task task:taskList){
            Customer customer = task.getCustomer();
            if(customer!=null&&customer.getLatitude()!=null){
                double latitude = customer.getLatitude();
                double longitude = customer.getLongitude();
                Log.i("Latitude============",latitude+"");
                Log.i("Longitude===========",longitude+"");
                if(longitude != 0&&latitude!= 0){
                    OverlayItem taskMarker = new OverlayItem(task.getUuid(), task.getDescription(), new GeoPoint(latitude, longitude));
                    if(task.getType().equalsIgnoreCase(HomeActivity.TASK_TYPE_ORDER)){
                        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.drugstore_order);
                        taskMarker.setMarker(myCurrentLocationMarker);
                    }else{
                        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.drugstore);
                        taskMarker.setMarker(myCurrentLocationMarker);
                    }
                    items.add(taskMarker);
                    markers.put(taskMarker.getTitle(),task.getUuid());
                }
            }
        }
        currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(getActivity(),item.getSnippet(),Toast.LENGTH_LONG).show();
                        return true;
                    }

                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        String taskId = markers.get(item.getTitle());
                        runner = new AsyncTaskRunner();
                        runner.execute(taskId);
                        return true;
                    }
                }, resourceProxy);
        this.mapView.getOverlays().clear();
        this.mapView.getOverlays().add(this.currentLocationOverlay);
        //FIX ME should get center point for all these points
        if(!items.isEmpty()){
            this.mapController.animateTo(items.get(0).getPoint());
        }else{
            this.mapController.animateTo(new GeoPoint(MAP_DEFAULT_LATITUDE,MAP_DEFAULT_LONGITUDE));
        }
        this.mapView.invalidate();

    }

    private List<Task> loadTasksFromDb(String dueDateString) {
        String[] choices = getResources().getStringArray(R.array.task_filters);

        int itemPosition = Utils.getItemPosition(dueDateString, choices);
        QueryBuilder<Task> taskQueryBuilder = taskDao.queryBuilder();
        List<Task> outstandingTasks=null;
        if(itemPosition==1){
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.lt(new Date()),TaskDao.Properties.Status.notEq(HomeActivity.STATUS_COMPLETE)).orderAsc(TaskDao.Properties.Description).list();
        } else if (itemPosition >= 0 && itemPosition < 6) {
            itemPosition = itemPosition==0?itemPosition:itemPosition - 1;
            Date dueDateOffset = Utils.addToDateOffset(new Date(), itemPosition);
            Date dueDatemax = Utils.addToDateMax(new Date(), itemPosition + 1);
            Log.i("Due Date:",dueDateOffset.toString()+":max-"+dueDatemax.toString());
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.between(dueDateOffset, dueDatemax),TaskDao.Properties.Status.notEq(HomeActivity.STATUS_COMPLETE)).orderAsc(TaskDao.Properties.Description).list();
        } else if (itemPosition == 6) {
            //nearby tasks
            List list = taskQueryBuilder.where(TaskDao.Properties.Status.notEq(HomeActivity.STATUS_COMPLETE),TaskDao.Properties.Status.notEq(HomeActivity.STATUS_CANCELLED)).orderAsc(TaskDao.Properties.Description).list();
            outstandingTasks = Utils.orderAndFilterUsingRealDistanceTo(new GeoPoint(LocationTracker.getLocation().getLatitude(), LocationTracker.getLocation().getLongitude()), list,MAX_RADIUS_IN_KM);

        } else if (itemPosition == 7) {
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.Status.notEq(HomeActivity.STATUS_COMPLETE),TaskDao.Properties.Status.notEq(HomeActivity.STATUS_CANCELLED)).orderAsc(TaskDao.Properties.Description).list();
        }
        return outstandingTasks;
    }



    private void showForm(String taskId){
        if(RestClient.getRole().equalsIgnoreCase(User.ROLE_SALES)){
            Intent i = new Intent(getActivity(), SalesFormActivity.class);
            i.putExtra("task_id", taskId);
            getActivity().startActivity(i);
        }else{
            Intent i = new Intent(getActivity(), MalariaFormActivity.class);
            i.putExtra("task_id", taskId);
            getActivity().startActivity(i);
        }
        runner = null;
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String>{

        private String taskId;

        @Override
        protected String doInBackground(String... strings) {
            taskId = strings[0];
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
           showForm(taskId);
        }
    }

}
