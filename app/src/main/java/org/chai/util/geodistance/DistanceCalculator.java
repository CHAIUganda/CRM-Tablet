package org.chai.util.geodistance;

/**
 * Created by victor on 2/5/15.
 */
public interface DistanceCalculator<T> {

    public Double calculateDistanceTo(T point);
}
