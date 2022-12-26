package einfprog.test_engine.base;

import java.io.PrintWriter;

public class InstanceGetter<C> extends TestBase implements IInstanceGetter<C>
{
    private C instance;
    
    public InstanceGetter(C instance)
    {
        this.instance = instance;
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        return true;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
    
    }
    
    @Override
    public C getInstance()
    {
        return instance;
    }
}
