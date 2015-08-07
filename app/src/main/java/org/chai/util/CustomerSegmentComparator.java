package org.chai.util;

import org.chai.model.Task;

import java.util.Comparator;

/**
 * Created by Zed on 7/28/2015.
 */
public class CustomerSegmentComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        try{
            return t1.getCustomer().getSegment().compareTo(t2.getCustomer().getSegment());
        }catch (Exception ex){
            Utils.log("Error comparing -> " + ex.getMessage());
        }

        return -1;
    }
}
