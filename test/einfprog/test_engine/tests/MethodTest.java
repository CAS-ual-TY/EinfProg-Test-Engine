package einfprog.test_engine.tests;

import einfprog.test_engine.Util;
import einfprog.test_engine.base.IClassGetter;
import einfprog.test_engine.base.IMethodInvoker;
import einfprog.test_engine.base.TestBase;
import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodTest<T, C, P extends IParamTypeSet> extends TestBase implements IMethodInvoker<T, C, P>
{
    public IClassGetter<C> clazz;
    public String methodName;
    public int methodModifiers;
    public Class<?> methodReturnType;
    public P params;
    
    public Method method;
    
    public MethodTest(IClassGetter<C> clazz, String methodName, int methodModifiers, Class<?> methodReturnType, P params)
    {
        this.clazz = clazz;
        this.methodName = methodName;
        this.methodModifiers = methodModifiers;
        this.methodReturnType = methodReturnType;
        this.params = params;
        method = null;
    }
    
    public MethodTest(IClassGetter<C> clazz, String methodName, Class<?> methodReturnType, P params)
    {
        this(clazz, methodName, Modifier.PUBLIC, methodReturnType, params);
    }
    
    public MethodTest(IClassGetter<C> clazz, String methodName, P params)
    {
        this(clazz, methodName, void.class, params);
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        if(!clazz.test(errorCallback))
        {
            return false;
        }
        
        try
        {
            Class<? extends C> clazz = this.clazz.findClass();
            method = clazz.getDeclaredMethod(methodName, params.getParamTypes());
            
            int modifiers = method.getModifiers();
            if(method.getModifiers() != methodModifiers)
            {
                eWrongModifiers(modifiers, errorCallback);
                return false;
            }
            
            Class<?> returnType = method.getReturnType();
            if(returnType != methodReturnType)
            {
                eWrongReturnType(returnType, errorCallback);
                return false;
            }
            
            if(Modifier.isPrivate(modifiers))
            {
                method.setAccessible(true);
            }
            
            return true;
        }
        catch(NoSuchMethodException e)
        {
            eNotFound(errorCallback);
            return false;
        }
    }
    
    public void eNotFound(PrintWriter errorCallback)
    {
        errorCallback.println("Can not find method:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(methodToString());
        Util.strongSpacer(errorCallback);
        
        clazz.appendFeedback(errorCallback);
    }
    
    public void eWrongModifiers(int modifiers, PrintWriter errorCallback)
    {
        errorCallback.println("Wrong signature for method:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(methodToString());
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Found:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(Modifier.toString(modifiers) + " " + methodReturnType.getSimpleName() + " " + methodName + " (" + Arrays.stream(params.getParamTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
        Util.strongSpacer(errorCallback);
    }
    
    public void eWrongReturnType(Class<?> type, PrintWriter errorCallback)
    {
        errorCallback.println("Wrong return type of method:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(methodToString());
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Found:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(Modifier.toString(methodModifiers) + " " + type.getSimpleName() + " " + methodName + "(" + Arrays.stream(params.getParamTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
        
        Util.strongSpacer(errorCallback);
    }
    
    @Override
    public <PP extends IParamSet<? extends P>> T invoke(C instance, PP params) throws InvocationTargetException, IllegalAccessException
    {
        return (T) method.invoke(instance, params.getParams());
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        errorCallback.println("After calling method:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(methodToString());
        Util.strongSpacer(errorCallback);
    }
    
    public String methodToString()
    {
        return Modifier.toString(methodModifiers) + " " + methodReturnType.getSimpleName() + " " + methodName + "(" + Arrays.stream(params.getParamTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")";
    }
}
