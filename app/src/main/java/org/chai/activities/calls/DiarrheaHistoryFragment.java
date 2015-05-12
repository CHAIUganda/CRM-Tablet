package org.chai.activities.calls;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.adapter.DiarrheaHistoryAdapter;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.DetailerCall;
import org.chai.model.DetailerCallDao;
import org.chai.util.MyApplication;
import org.chai.util.Utils;
import org.chai.util.migration.UpgradeOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zed on 5/12/2015.
 */
public class DiarrheaHistoryFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DetailerCallDao detailerCallDao;

    private List<DetailerCall> items;
    DiarrheaHistoryAdapter adapter;

    AQuery aq;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_history_fragment, container, false);
        initialiseGreenDao();

        listView = (ListView)view.findViewById(R.id.lst_items);
        aq = new AQuery(view);
        items = new ArrayList<DetailerCall>();
        items.addAll(detailerCallDao.loadAll());
        adapter = new DiarrheaHistoryAdapter(getActivity(), R.layout.history_item_row, items);
        listView.setAdapter(adapter);

        Utils.log("Got items -> " + items.size());

        return view;
    }

    private void initialiseGreenDao() {
        try {
            UpgradeOpenHelper helper = MyApplication.getDbOpenHelper();
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            detailerCallDao = daoSession.getDetailerCallDao();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Error initialising Database:" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
