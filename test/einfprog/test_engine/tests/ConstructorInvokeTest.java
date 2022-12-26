package einfprog.test_engine.tests;

import einfprog.test_engine.Settings;
import einfprog.test_engine.Util;
import einfprog.test_engine.base.IClassGetter;
import einfprog.test_engine.base.IInstanceGetter;
import einfprog.test_engine.base.TestBase;
import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInvokeTest<C, P extends IParamTypeSet> extends TestBase implements IInstanceGetter<C>
{
    public ConstructorTest<C, P> constructorTest;
    public IParamSet<? extends P> params;
    
    public C instance;
    
    public ConstructorInvokeTest(ConstructorTest<C, P> constructorTest, IParamSet<? extends P> params)
    {
        this.constructorTest = constructorTest;
        this.params = params;
        instance = null;
    }
    
    public ConstructorInvokeTest(IClassGetter<C> clazz, int modifiers, IParamSet<? extends P> params)
    {
        this(new ConstructorTest<>(clazz, modifiers, params.castToTypes()), params);
    }
    
    public String constructorToString()
    {
        return constructorTest.constructorToString();
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        if(!constructorTest.test(errorCallback))
        {
            return false;
        }
        
        try
        {
            instance = constructorTest.invoke(params);
            return true;
        }
        catch(InvocationTargetException e)
        {
            eException(e, errorCallback);
            return false;
        }
        catch(IllegalAccessException | InstantiationException e)
        {
            Util.error(errorCallback, e);
            return false;
        }
    }
    
    public void eException(InvocationTargetException e, PrintWriter errorCallback)
    {
        errorCallback.println("Exception thrown when invoking constructor:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Exception:");
        Util.weakSpacer(errorCallback);
        if(Settings.PRINT_STACKTRACE_ON_ERROR)
        {
            e.getTargetException().printStackTrace(errorCallback);
        }
        else
        {
            errorCallback.println(e.getTargetException().getMessage());
        }
        
        Util.strongSpacer(errorCallback);
        
        appendFeedback(errorCallback);
    }
    
    @Override
    public C getInstance()
    {
        return instance;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        constructorTest.appendFeedback(errorCallback);
        appendParams(errorCallback);
    }
    
    public void appendParams(PrintWriter errorCallback)
    {
        if(params.getParams().length > 0)
        {
            errorCallback.println("With constructor parameters: ");
            Util.weakSpacer(errorCallback);
            
            for(int i = 0; i < params.getParams().length; i++)
            {
                if(params.getParams()[i] == null)
                {
                    errorCallback.println("null");
                }
                else
                {
                    Class<?> type = params.getParamTypes()[i];
                    String param = Util.objectToString(params.getParams()[i]);
                    if(type == String.class)
                    {
                        param = "\"" + param + "\"";
                    }
                    errorCallback.println(type.getSimpleName() + ": " + param);
                }
            }
            
            Util.strongSpacer(errorCallback);
        }
    }
}
