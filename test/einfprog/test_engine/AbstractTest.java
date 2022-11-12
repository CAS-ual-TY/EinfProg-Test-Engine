package einfprog.test_engine;

import java.io.PrintWriter;

public abstract class AbstractTest
{
    private Runnable test;
    
    public AbstractTest(Runnable test)
    {
        this.test = test;
    }
    
    public abstract String[] getInput();
    
    public void run()
    {
        test.run();
    }
    
    public abstract boolean passes(PrintWriter errorCallback, String rawOutput);
}
