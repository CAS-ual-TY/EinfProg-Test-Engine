package einfprog.test_engine.tests;

import einfprog.test_engine.Settings;
import einfprog.test_engine.Util;
import einfprog.test_engine.base.*;
import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class MethodInvokeTest<T, C, P extends IParamTypeSet> extends TestBase implements IValueGetter<T>
{
    public MethodTest<T, C, P> methodTest;
    public IInstanceGetter<C> instance;
    public IParamSet<? extends P> methodParams;
    
    public T value;
    
    public MethodInvokeTest(MethodTest<T, C, P> methodTest, IInstanceGetter<C> instance, IParamSet<? extends P> methodParams)
    {
        this.methodTest = methodTest;
        this.instance = instance;
        this.methodParams = methodParams;
        value = null;
    }
    
    public MethodInvokeTest(MethodTest<T, C, P> methodTest, IParamSet<? extends P> methodParams)
    {
        this(methodTest, new StaticInstance<>(methodTest.clazz), methodParams);
    }
    
    public MethodInvokeTest(IClassGetter<C> clazz, IInstanceGetter<C> instance, String methodName, int methodModifiers, Class<? extends T> returnValueType, IParamSet<? extends P> methodParams)
    {
        this(new MethodTest<>(clazz, methodName, methodModifiers, returnValueType, methodParams.castToTypes()),
                instance, methodParams);
    }
    
    public MethodInvokeTest(IClassGetter<C> clazz, String methodName, int methodModifiers, Class<T> returnValueType, IParamSet<P> methodParams)
    {
        this(clazz, new StaticInstance<>(clazz), methodName, methodModifiers | Modifier.STATIC, returnValueType, methodParams);
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        if(!methodTest.test(errorCallback) || !instance.test(errorCallback))
        {
            return false;
        }
        
        try
        {
            value = methodTest.invoke(instance.getInstance(), methodParams);
            return true;
        }
        catch(InvocationTargetException e)
        {
            eException(e, errorCallback);
            return false;
        }
        catch(IllegalAccessException e)
        {
            Util.error(errorCallback, e);
            return false;
        }
    }
    
    public void eException(InvocationTargetException e, PrintWriter errorCallback)
    {
        errorCallback.println("Exception thrown:");
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
    public T getValue()
    {
        return value;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        methodTest.appendFeedback(errorCallback);
        appendParams(errorCallback);
        instance.appendFeedback(errorCallback);
    }
    
    public void appendParams(PrintWriter errorCallback)
    {
        if(methodParams.getParams().length > 0)
        {
            errorCallback.println("With method parameters: ");
            Util.weakSpacer(errorCallback);
            
            for(int i = 0; i < methodParams.getParams().length; i++)
            {
                if(methodParams.getParams()[i] == null)
                {
                    errorCallback.println("null");
                }
                else
                {
                    Class<?> type = methodParams.getParamTypes()[i];
                    String param = Util.objectToString(methodParams.getParams()[i]);
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
