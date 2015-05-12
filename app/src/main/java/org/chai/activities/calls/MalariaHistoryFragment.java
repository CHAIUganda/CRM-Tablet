package org.chai.activities.calls;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.chai.R;
import org.chai.model.DaoMaster;
import org.chai.model.DaoSession;
import org.chai.model.MalariaDetailDao;
import org.chai.util.MyApplication;
import org.chai.util.migration.UpgradeOpenHelper;

/**
 * Created by Zed on 5/12/2015.
 */
public class MalariaHistoryFragment extends Fragment {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private MalariaDetailDao malariaDetailDao;

    AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_history_fragment, container, false);
        initialiseGreenDao();
        aq = new AQuery(view);



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
}
