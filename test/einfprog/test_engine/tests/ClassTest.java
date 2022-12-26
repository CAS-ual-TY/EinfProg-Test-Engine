package einfprog.test_engine.tests;

import einfprog.test_engine.Util;
import einfprog.test_engine.base.IClassGetter;
import einfprog.test_engine.base.TestBase;

import java.io.PrintWriter;

public class ClassTest<C> extends TestBase implements IClassGetter<C>
{
    public String clazz;
    
    public Class<C> theClass;
    
    public ClassTest(String clazz)
    {
        this.clazz = clazz;
        theClass = null;
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        try
        {
            theClass = (Class<C>) Class.forName(clazz);
            return true;
        }
        catch(ClassNotFoundException e)
        {
            eClassNotFound(errorCallback);
            return false;
        }
    }
    
    public void eClassNotFound(PrintWriter errorCallback)
    {
        errorCallback.println("Can not find class:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(theClass.getCanonicalName());
        Util.strongSpacer(errorCallback);
    }
    
    @Override
    public Class<C> findClass()
    {
        return theClass;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        errorCallback.println("In class:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(theClass.getCanonicalName());
        Util.strongSpacer(errorCallback);
    }
    
    @Override
    public String getClassName()
    {
        String[] split = clazz.split("\\.");
        
        if(split.length == 0)
        {
            return "???";
        }
        else
        {
            return split[split.length - 1];
        }
    }
}
