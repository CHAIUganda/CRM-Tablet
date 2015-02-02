package org.chai.activities.tasks;

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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import de.greenrobot.dao.query.QueryBuilder;
import org.chai.R;
import org.chai.activities.BaseContainerFragment;
import org.chai.model.*;
import org.chai.rest.RestClient;
import org.chai.util.GPSTracker;
import org.chai.util.Utils;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.SimpleLocationOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.*;

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
    private HashMap<String,String> markers = new HashMap<String, String>();
    private Spinner calenderSpinner;
    private AsyncTaskRunner runner;


    private int MAP_DEFAULT_ZOOM = 8;
    private double MAP_DEFAULT_LATITUDE =0.3136;
    private double MAP_DEFAULT_LONGITUDE = 32.5811;

    private MapView mapView;
    private IMapController mapController;
    private MyLocationNewOverlay mMyLocationOverlay;
    private ItemizedIconOverlay<OverlayItem> currentLocationOverlay;
    private DefaultResourceProxyImpl resourceProxy;
    private ArrayList<OverlayItem> items;
    private GPSTracker gpsTracker;


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

        gpsTracker = new GPSTracker(getActivity());
        if(gpsTracker.canGetLocation()){
            MAP_DEFAULT_LATITUDE = gpsTracker.getLatitude();
            MAP_DEFAULT_LONGITUDE = gpsTracker.getLongitude();
        }else{
            gpsTracker.showSettingsAlert();
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
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "chai-crm-db", null);
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
            if(customer!=null){
                double latitude = customer.getLatitude();
                double longitude = customer.getLongitude();
                Log.i("Latitude============",latitude+"");
                Log.i("Longitude===========",longitude+"");
                if(longitude != 0&&latitude!= 0){
                    OverlayItem taskMarker = new OverlayItem(task.getUuid(), task.getDescription(), new GeoPoint(latitude, longitude));
                    Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.drugstore);
                    taskMarker.setMarker(myCurrentLocationMarker);
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
        }
        this.mapView.invalidate();

    }

    private List<Task> loadTasksFromDb(String dueDateString) {
        String[] choices = getResources().getStringArray(R.array.task_filters);

        int itemPosition = Utils.getItemPosition(dueDateString, choices);
        QueryBuilder<Task> taskQueryBuilder = taskDao.queryBuilder();
        List<Task> outstandingTasks=null;
        if(itemPosition==1){
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.lt(new Date()),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE)).list();
        } else if (itemPosition > 0 && itemPosition < 6) {
            itemPosition=itemPosition-1;
            Date dueDateOffset = Utils.addToDate(new Date(),itemPosition);
            Date dueDatemax = Utils.addToDate(new Date(),itemPosition+1);
            Log.i("Due Date:",dueDateOffset.toString()+":max-"+dueDatemax.toString());
            outstandingTasks = taskQueryBuilder.where(TaskDao.Properties.DueDate.between(dueDateOffset, dueDatemax),TaskDao.Properties.Status.notEq(TaskMainFragment.STATUS_COMPLETE)).list();
        }else{
            outstandingTasks = taskDao.loadAll();
        }
        return outstandingTasks;
    }

    private void showForm(String taskId){
        if (RestClient.role.equalsIgnoreCase(User.ROLE_SALES)) {
            SaleslFormFragment commercialFormActivity = new SaleslFormFragment();
            Bundle bundle = new Bundle();
            bundle.putString("taskId", taskId);
            commercialFormActivity.setArguments(bundle);
            ((BaseContainerFragment) getParentFragment()).replaceFragment(commercialFormActivity, true);
        } else {
            DetailersActivity detailersActivity = new DetailersActivity();
            Bundle bundle = new Bundle();
            bundle.putString("taskId", taskId);
            detailersActivity.setArguments(bundle);
            ((BaseContainerFragment)getParentFragment()).replaceFragment(detailersActivity,true);
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
