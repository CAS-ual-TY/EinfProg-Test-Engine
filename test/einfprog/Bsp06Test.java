package einfprog;

import einfprog.test_engine.params.ParamSet1;
import einfprog.test_engine.params.ParamSet2;
import einfprog.test_engine.Engine;
import einfprog.test_engine.TestMaker;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Bsp06Test
{
    private double getMin(double[] ts) {
        
        double min = ts[0];
        
        for (int i = 1; i < ts.length; i++) {
            if (ts[i] < min)
                min = ts[i];
        }
        return min;
    }
    
    private double getMax(double[] ts) {
        
        double max = ts[0];
        
        for (int i = 1; i < ts.length; i++) {
            if (ts[i] > max)
                max = ts[i];
        }
        return max;
    }
    
    private void shiftY(double[] ts, double dy) {
        
        for (int i = 0; i < ts.length; i++)
            ts[i] += dy;
    }
    
    private double getMean(double[] ts) {
        
        double sum = 0.0;
        
        for (int i = 0; i < ts.length; i++)
            sum += ts[i];
        
        return sum / ts.length;
    }
    
    private double getStandardDeviation(double[] ts) {
        
        double sum = 0.0;
        double m = getMean(ts);
        
        for (int i = 0; i < ts.length; i++) {
            double diff = ts[i] - m;
            sum += diff * diff;
        }
        return Math.sqrt(sum / (ts.length - 1));
    }
    
    private double getRMSE(double[] tsa, double[] tsb) {
        
        int length = Math.min(tsa.length, tsb.length);
        double sum = 0.0;
        
        for (int i = 0; i < length; i++) {
            double diff = tsa[i] - tsb[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum / length);
    }
    
    private int[] getLocalMinima(double[] ts) {
        
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
    
    private int[] getLocalMaxima(double[] ts) {
        
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
    
    private double[] getMovingAverage(double[] ts, int size) {
        
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
    
    private String toString(double[] ts) {
        
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
    
    private int[] trim(int[] a, int length) {
        
        if (a == null || length <= 0)
            return null;
        if (a.length <= length)
            return a;
        
        int[] b = new int[length];
        
        for (int i = 0; i < b.length; i++)			// copy
            b[i] = a[i];
        
        return b;
    }
    
    @Test
    public void testMain()
    {
        double[][] simpleInputs = new double[][] {{1D}, {1D, 2D}, {5D, 4D, 7D, 10D, 0D}};
        
        for(double[] input : simpleInputs)
        {
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("toString", String.class, new ParamSet1<>(input))
                    .testValue(toString(input))
                    .runTest();
        }
        
        double[][] moreInputs = new double[6][];
        
        for(int i = 0; i < moreInputs.length; i++)
        {
            int length = (i + 1) * 2;
            double[] input = new double[length];
            Random rand = new Random(length + 7865768);
            
            for(int j = 0; j < input.length; j++)
            {
                input[j] = (rand.nextInt(41) - 20) * 0.5D;
            }
            
            moreInputs[i] = input;
        }
        
        for(double[] input : moreInputs)
        {
            
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("getMin", double.class, new ParamSet1<>(input))
                    .testValue(getMin(input))
                    .runTest();
        }
        
        for(double[] input : moreInputs)
        {
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("getMax", double.class, new ParamSet1<>(input))
                    .testValue(getMax(input))
                    .runTest();
        }
        
        for(double[] input : moreInputs)
        {
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("getMean", double.class, new ParamSet1<>(input))
                    .testValue(getMean(input))
                    .runTest();
        }
        
        for(double[] input : moreInputs)
        {
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("getStandardDeviation", double.class, new ParamSet1<>(input))
                    .testValue(getStandardDeviation(input))
                    .runTest();
        }
        
        for(double[] input1 : moreInputs)
        {
            for(double[] input2 : moreInputs)
            {
                TestMaker.builder()
                        .withClass("einfprog.Bsp06")
                        .statically()
                        .callMethod("getRMSE", double.class, new ParamSet2<>(input1, input2))
                        .testValue(getRMSE(input1, input2))
                        .runTest();
            }
        }
    }
    
    @Test
    public void testErweiterung()
    {
        Engine.ENGINE.requiresDoesNotFail(this::testMain, "Part 1 must pass first.");
        
        double[][] moreInputs = new double[11][];
        
        for(int i = 0; i < moreInputs.length; i++)
        {
            int length = i + 2;
            double[] input = new double[length];
            Random rand = new Random(length + 7865768);
            
            for(int j = 0; j < input.length; j++)
            {
                input[j] = (rand.nextInt(41) - 20) * 0.5D;
            }
            
            moreInputs[(i + moreInputs.length - 1) % moreInputs.length] = input;
        }
        
        for(double[] input : moreInputs)
        {
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("getLocalMinima", int[].class, new ParamSet1<>(input))
                    .testValue(getLocalMinima(input))
                    .runTest();
        }
        
        for(double[] input : moreInputs)
        {
            TestMaker.builder()
                    .withClass("einfprog.Bsp06")
                    .statically()
                    .callMethod("getLocalMaxima", int[].class, new ParamSet1<>(input))
                    .testValue(getLocalMaxima(input))
                    .runTest();
        }
        
        for(double[] input : moreInputs)
        {
            for(int window = 1; window < input.length + 3; window += 3)
            {
                TestMaker.builder()
                        .withClass("einfprog.Bsp06")
                        .statically()
                        .callMethod("getMovingAverage", double[].class, new ParamSet2<>(input, window))
                        .testValue(getMovingAverage(input, window))
                        .runTest();
            }
        }
    }
}
