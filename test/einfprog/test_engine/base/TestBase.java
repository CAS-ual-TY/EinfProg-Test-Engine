package einfprog.test_engine.base;

import java.io.PrintWriter;

public abstract class TestBase implements IFeedback
{
    public boolean cached;
    public boolean result;
    
    public boolean written;
    
    protected TestBase()
    {
        cached = false;
        written = false;
    }
    
    @Override
    public boolean test(PrintWriter errorCallback)
    {
        if(!cached)
        {
            cached = true;
            result = doTest(errorCallback);
        }
        
        return result;
    }
    
    public abstract boolean doTest(PrintWriter errorCallback);
    
    @Override
    public void appendFeedback(PrintWriter errorCallback)
    {
        if(!written)
        {
            written = true;
            doAppendFeedback(errorCallback);
        }
    }
    
    public abstract void doAppendFeedback(PrintWriter errorCallback);
}
