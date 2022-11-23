package einfprog.test_engine;

import java.io.PrintWriter;

public abstract class OutputTest
{
    private Runnable test;
    
    public OutputTest(Runnable test)
    {
        this.test = test;
    }
    
    public abstract String[] getInput();
    
    public void run()
    {
        test.run();
    }
    
    public abstract boolean passes(PrintWriter errorCallback, String rawOutput);
    
    public abstract void onError(Throwable e, PrintWriter errorCallback);
}
