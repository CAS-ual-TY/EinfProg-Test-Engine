package einfprog.test_engine.base;

import einfprog.test_engine.Util;

import java.io.PrintWriter;

public class InstanceGetter<C> extends TestBase implements IInstanceGetter<C>
{
    public C instance;
    public String instToString;
    
    public InstanceGetter(C instance, String instToString)
    {
        this.instance = instance;
        this.instToString = instToString;
    }
    
    public InstanceGetter(C instance)
    {
        this(instance, instance.toString());
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        return true;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        errorCallback.println("Of instance:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(instance.getClass().getSimpleName() + ": " + instToString);
        Util.strongSpacer(errorCallback);
    }
    
    @Override
    public C getInstance()
    {
        return instance;
    }
}
