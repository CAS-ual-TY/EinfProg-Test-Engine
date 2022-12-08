package einfprog;

import java.util.Arrays;

/** This is a small library for time series operations.
 *
 * @author Helmut A. Mayer
 * @version 0.1
 * @since December 2022
 *
 */
public class Bsp06P
{
    
    public static void main (String[] args) {
        
        /* Part 1 */
        
        double[] ts = {1, 0, -1, 0, 2, 0, -2, 0, 3, 0, -3, 0};
        double[] tsb = {1, 0, -1, 0, 2, 0, -2, 0, 3, 0, -3, 1};
        
        System.out.println("ts = " + toString(ts));
        
        System.out.println("Min = " + getMin(ts));
        System.out.println("Max = " + getMax(ts));
        System.out.println("Mean = " + getMean(ts));
        System.out.println("StDev = " + getStandardDeviation(ts));
        System.out.println("RMSE = " + getRMSE(ts, tsb));
        
        /* Part 2 */
        
        int min[] = getLocalMinima(ts);
        System.out.println("Local Minima: " + Arrays.toString(min));
        int max[] = getLocalMaxima(tsb);
        System.out.println("Local Maxima: " + Arrays.toString(max));
        double[] ma = getMovingAverage(ts, 5);
        System.out.println("Moving Average = " + toString(ma));
    }
    
    
    /** Returns the minimal value in the time series.
     *
     * @param ts	a time series
     *
     * @return	the minimal value
     *
     */
    public static double getMin(double[] ts) {
        
        double min = ts[0];
        
        for (int i = 1; i < ts.length; i++) {
            if (ts[i] < min)
                min = ts[i];
        }
        return min;
    }
    
    
    /** Returns the maximal value in the time series.
     *
     * @param ts	a time series
     *
     * @return	the maximal value
     *
     */
    public static double getMax(double[] ts) {
        
        double max = ts[0];
        
        for (int i = 1; i < ts.length; i++) {
            if (ts[i] > max)
                max = ts[i];
        }
        return max;
    }
    
    
    /** Shifts the values of the time series.
     *
     * @param ts	a time series
     * @param dy the value to be added
     *
     */
    public static void shiftY(double[] ts, double dy) {
        
        for (int i = 0; i < ts.length; i++)
            ts[i] += dy;
    }
    
    
    /** Returns the mean value of the time series.
     *
     * @param ts	a time series
     *
     * @return	the mean value
     *
     */
    public static double getMean(double[] ts) {
        
        double sum = 0.0;
        
        for (int i = 0; i < ts.length; i++)
            sum += ts[i];
        
        return sum / ts.length;
    }
    
    
    /** Returns the sample standard deviation of the time series, which is given by
     * s^2 = 1/(n-1)sum[(xi - m)^2], where s is the sample standard deviation, n is the number of values of the time series, xi is a single value, and m is the mean of the time series.
     *
     * @param ts	a time series
     *
     * @return	the standard deviation
     *
     */
    public static double getStandardDeviation(double[] ts) {
        
        double sum = 0.0;
        double m = getMean(ts);
        
        for (int i = 0; i < ts.length; i++) {
            double diff = ts[i] - m;
            sum += diff * diff;
        }
        return Math.sqrt(sum / (ts.length - 1));
    }
    
    
    /** Returns the root mean square error of two time series a and b, which is given by
     * RMSE^2 = (1/n)sum[(ai - bi)^2], where RMSE is the root mean square error, n is the number of
     * values of the time series, ai and bi are values of the two time series at the same point in time. If the two time series are of different length, the RMSE is calculated over the common points in time.
     *
     * @param tsa	a time series
     * @param tsb	another time series
     *
     * @return	RMSE
     *
     */
    public static double getRMSE(double[] tsa, double[] tsb) {
        
        int length = Math.min(tsa.length, tsb.length);
        double sum = 0.0;
        
        for (int i = 0; i < length; i++) {
            double diff = tsa[i] - tsb[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum / length);
    }
    
    
    /** Returns the points in time of all local minima. A local minimum at a point t is defined as a value, which is smaller than the values at t-1 and t+1. If a point does not have two neighbors, it cannot be a local minimum.
     *
     * @param ts	a time series
     *
     * @return	the points in time of all local minima, null, if there is no local minimum
     *
     */
    public static int[] getLocalMinima(double[] ts) {
        
        int[] min = new int[ts.length / 2];		// it cannot be longer
        int count = 0;
        int i = 1;
        
        while (i < ts.length - 1) {
            if (ts[i] < ts[i - 1] && ts[i] < ts[i + 1]) {
                min[count++] = i;
                i += 2;																			// neighbor cannot be minimum
            }
            else
                ++i;
        }
        return trim(min, count);
    }
    
    
    /** Returns the points in time of all local maxima. A local maximum at a point t is defined as a value, which is larger than the values at t-1 and t+1. If a point does not have two neighbors, it cannot be a local maximum.
     *
     * @param ts	a time series
     *
     * @return	the points in time of all local minima, null, if there is no local minimum
     *
     */
    public static int[] getLocalMaxima(double[] ts) {
        
        int[] max = new int[ts.length / 2];		// it cannot be longer
        int count = 0;
        int i = 1;
        
        while (i < ts.length - 1) {
            if (ts[i] > ts[i - 1] && ts[i] > ts[i + 1]) {
                max[count++] = i;
                i += 2;																			// neighbor cannot be maximum
            }
            else
                ++i;
        }
        
        return trim(max, count);
    }
    
    
    /** Returns the moving average of the time series over a window size. The average at a point in time is given by the average of the previous values in the window (including the current point in time). If the number of previous values is less than the window size, then all these values are taken into account. E.g., at the start of the time series there is only one value (and no previous values), hence the window size is reduced to 1.
     *
     * @param ts	a time series
     * @param size	the window size (number of values for the average)
     *
     * @return	the moving average in a new array
     *
     */
    public static double[] getMovingAverage(double[] ts, int size) {
        
        double[] ma = new double[ts.length];
        double sum = 0.0;
        int count = 0;
        
        for (int i = 0; i < ma.length; i++) {
            sum += ts[i];												// add value in window
            if (count == size)
                sum -= ts[i - size];							// drop old value outside window
            else
                ++count;
            ma[i] = sum / count;
        }
        return ma;
    }
    
    
    /** Returns the string representation of the given time series. E.g., with a time series of length 2
     * the string is "[v1, v2]".
     *
     * @param ts	a time series
     *
     * @return	the time series as string
     *
     */
    public static String toString(double[] ts) {
        
        if (ts == null)
            return "null";
        
        String s = "[";
        
        for (int i = 0; i < ts.length; i++)
            s += ts[i] + ", ";
        
        if (s.length() > 1)											// accounts for dim 0
            s = s.substring(0, s.length() - 2);
        s += "]";
        
        return s;
    }
    
    
    /** Trims an array to the given length. If the new length is larger or equal to the array length,
     * the array is returned without change.
     *
     * @param a				an array
     * @param length		the new trimmed length
     *
     * @return	a new trimmed array with the original values, null, if problems
     *
     */
    private static int[] trim(int[] a, int length) {
        
        if (a == null || length <= 0)
            return null;
        if (a.length <= length)
            return a;
        
        int[] b = new int[length];
        
        for (int i = 0; i < b.length; i++)			// copy
            b[i] = a[i];
        
        return b;
    }
    
}
