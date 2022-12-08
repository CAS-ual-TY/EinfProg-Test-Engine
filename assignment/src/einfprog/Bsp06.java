package einfprog;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Bsp06
{
    /**
     * Returns the string representation of the given time series. E.g., with a time series of length 2
     * the string is "[v1, v2]".
     *
     * @param ts a time series
     * @return the time series as string
     */
    public static String toString(double[] ts)
    {
        return "[" + Arrays.stream(ts).mapToObj(String::valueOf).collect(Collectors.joining(", ")) + "]";
    }
    
    /**
     * Returns the minimal value in the time series.
     *
     * @param ts a time series
     * @return the minimal value
     */
    public static double getMin(double[] ts)
    {
        return Arrays.stream(ts).min().orElse(0);
    }
    
    /**
     * Returns the maximal value in the time series.
     *
     * @param ts a time series
     * @return the maximal value
     */
    public static double getMax(double[] ts)
    {
        return Arrays.stream(ts).max().orElse(0);
    }
    
    /**
     * Returns the mean value of the time series.
     *
     * @param ts a time series
     * @return the mean value
     */
    public static double getMean(double[] ts)
    {
        return Arrays.stream(ts).average().orElse(0);
    }
    
    /**
     * Returns the sample standard deviation of the time series, which is given by
     * s^2 = 1/(n-1)sum[(xi - m)^2], where s is the sample standard deviation, n is the number of values of the time series, xi is a single value, and m is the mean of the time series.
     *
     * @param ts a time series
     * @return the standard deviation
     */
    public static double getStandardDeviation(double[] ts)
    {
        double mean = getMean(ts);
        double sum = Arrays.stream(ts).map(xi -> xi - mean).map(xi -> xi * xi).sum();
        return Math.sqrt(1D / (ts.length - 1) * sum);
    }
    
    /**
     * Returns the root mean square error of two time series a and b, which is given by
     * RMSE^2 = (1/n)sum[(ai - bi)^2], where RMSE is the root mean square error, n is the number of
     * values of the time series, ai and bi are values of the two time series at the same point in time. If the two time series are of different length, the RMSE is calculated over the common points in time.
     *
     * @param tsa a time series
     * @param tsb another time series
     * @return RMSE
     */
    public static double getRMSE(double[] tsa, double[] tsb)
    {
        double[] ts = new double[Math.min(tsa.length, tsb.length)];
        for(int i = 0; i < ts.length; i++)
        {
            ts[i] = tsa[i] - tsb[i];
        }
        double sum = Arrays.stream(ts).map(xi -> xi * xi).sum();
        return Math.sqrt(1D / ts.length * sum);
    }
    
    /**
     * Returns the points in time of all local minima. A local minimum at a point t is defined as a value, which is smaller than the values at t-1 and t+1. If a point does not have two neighbors, it cannot be a local minimum.
     *
     * @param ts a time series
     * @return the points in time of all local minima, null, if there is no local minimum
     */
    public static int[] getLocalMinima(double[] ts)
    {
        int[] min = new int[0];
        
        for(int i = 1; i < ts.length - 1; i++)
        {
            if(ts[i] < ts[i - 1] && ts[i] < ts[i + 1])
            {
                int[] newMin = new int[min.length + 1];
                newMin[min.length] = i;
                
                System.arraycopy(min, 0, newMin, 0, min.length);
                min = newMin;
            }
        }
        
        return min.length == 0 ? null : min;
    }
    
    /**
     * Returns the points in time of all local maxima. A local maximum at a point t is defined as a value, which is larger than the values at t-1 and t+1. If a point does not have two neighbors, it cannot be a local maximum.
     *
     * @param ts a time series
     * @return the points in time of all local minima, null, if there is no local minimum
     */
    public static int[] getLocalMaxima(double[] ts)
    {
        int[] min = new int[0];
        
        for(int i = 1; i < ts.length - 1; i++)
        {
            if(ts[i] > ts[i - 1] && ts[i] > ts[i + 1])
            {
                int[] newMin = new int[min.length + 1];
                newMin[min.length] = i;
                
                System.arraycopy(min, 0, newMin, 0, min.length);
                min = newMin;
            }
        }
        
        return min.length == 0 ? null : min;
    }
    
    /**
     * Returns the moving average of the time series over a window size. The average at a point in time is given by the average of the previous values in the window (including the current point in time). If the number of previous values is less than the window size, then all these values are taken into account. E.g., at the start of the time series there is only one value (and no previous values), hence the window size is reduced to 1.
     *
     * @param ts   a time series
     * @param size the window size (number of values for the average)
     * @return the moving average in a new array
     */
    public static double[] getMovingAverage(double[] ts, int size)
    {
        double[] ma = new double[ts.length];
        
        for(int i = 0; i < ts.length; i++)
        {
            double sum = ts[i];
            
            for(int j = Math.max(0, i + 1 - size); j < i; j++)
            {
                sum += ts[j];
            }
            
            ma[i] = sum / Math.min(i + 1, size);
        }
        
        return ma;
    }
}
