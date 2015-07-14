package org.chai.activities.calls;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.activities.forms.MalariaFormActivity;
import org.chai.adapter.MalariaHistoryAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.MalariaDetail;
import org.chai.model.MalariaDetailDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 5/12/2015.
 */
public class MalariaHistoryFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private MalariaDetailDao malariaDetailDao;

    private List<MalariaDetail> items;
    MalariaHistoryAdapter adapter;

    AQuery aq;

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_history_fragment, container, false);
        initialiseGreenDao();

        listView = (ListView)view.findViewById(R.id.lst_items);
        aq = new AQuery(view);
        items = new ArrayList<>();
        items.addAll(malariaDetailDao.queryBuilder().orderDesc(MalariaDetailDao.Properties.DateOfSurvey).list());
        adapter = new MalariaHistoryAdapter(getActivity(), R.layout.history_item_row, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MalariaDetail detail = items.get(position);
                Intent i = new Intent(getActivity(), MalariaFormActivity.class);
                i.putExtra("detail_id", detail.getUuid());
                i.putExtra("task_id", detail.getTaskId());
                getActivity().startActivity(i);
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
            malariaDetailDao = daoSession.getMalariaDetailDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.findItem(R.id.action_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Utils.log("Query changed: " + newText);
                if(adapter != null){
                    adapter.getFilter().filter(newText);
                }
                return false;
            }

        });
    }
}
