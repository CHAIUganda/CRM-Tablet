package org.chai.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by victor on 12/8/14.
 */
public class HomeTabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    public HomeTabListener(Activity activity,String tag,Class<T> clazz) {
        this.mActivity = activity;
        mTag = tag;
        mClass = clazz;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
      if(mFragment == null){
          mFragment = Fragment.instantiate(mActivity,mClass.getName());
          fragmentTransaction.add(android.R.id.content,mFragment,mTag);
      }else{
          fragmentTransaction.attach(mFragment);
      }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(mFragment!=null){
            fragmentTransaction.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
