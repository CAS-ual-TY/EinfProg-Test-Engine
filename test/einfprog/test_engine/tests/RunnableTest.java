package einfprog.test_engine.tests;

import einfprog.test_engine.base.IFeedback;
import einfprog.test_engine.base.TestBase;

import java.io.PrintWriter;

public class RunnableTest extends TestBase implements IFeedback
{
    public Runnable runnable;
    public String onError;
    
    public RunnableTest(Runnable runnable, String onError)
    {
        this.runnable = runnable;
        this.onError = onError;
    }
    
    public RunnableTest(Runnable runnable)
    {
        this(runnable, null);
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        if(onError != null)
        {
            errorCallback.println(onError);
        }
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        try
        {
            runnable.run();
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace(errorCallback);
            return false;
        }
    }
}
