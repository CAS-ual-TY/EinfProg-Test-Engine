package einfprog.test_engine.base;

import einfprog.test_engine.Util;

import java.io.PrintWriter;

public class ClassGetter<C> extends TestBase implements IClassGetter<C>
{
    private Class<? extends C> clazz;
    
    public ClassGetter(Class<? extends C> clazz)
    {
        this.clazz = clazz;
    }
    
    @Override
    public Class<? extends C> findClass()
    {
        return clazz;
    }
    
    @Override
    public String getClassName()
    {
        return clazz.getSimpleName();
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        return true;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        errorCallback.println("In class:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(clazz.getCanonicalName());
        Util.strongSpacer(errorCallback);
    }
}
