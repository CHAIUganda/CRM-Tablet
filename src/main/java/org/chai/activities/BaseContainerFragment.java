package org.chai.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import org.chai.R;

/**
 * Created by victor on 12/26/14.
 */
public class BaseContainerFragment extends Fragment {

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container_framelayout, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

    public static boolean handleBackPressed(FragmentManager fm)
    {
        if(fm.getFragments() != null){
            for(Fragment frag : fm.getFragments()){
                if(frag != null && frag.isVisible() && frag instanceof BaseContainerFragment){
                    if(((BaseContainerFragment)frag).onBackPressed()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    protected boolean onBackPressed()
    {
        FragmentManager fm = getChildFragmentManager();
        if(handleBackPressed(fm)){
            return true;
        }
        else if(getUserVisibleHint() && fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
            return true;
        }
        return false;
    }
    public void resetFragment(int id,BaseContainerFragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.replace(id,fragment);
        transaction.commit();

    }
}
