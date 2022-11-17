package einfprog.test_engine;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodInvokeTest<T, C>
{
    private MethodTest<T, C> methodTest;
    private C instance;
    private T acceptedReturnValue;
    private Object[] methodParams;
    
    public MethodInvokeTest(MethodTest<T, C> methodTest, C instance, T acceptedReturnValue, Object... methodParams)
    {
        this.methodTest = methodTest;
        this.instance = instance;
        this.acceptedReturnValue = acceptedReturnValue;
        this.methodParams = methodParams;
    }
    
    public MethodInvokeTest(MethodTest<T, C> methodTest, T acceptedReturnValue, Object... methodParams)
    {
        this(methodTest, null, acceptedReturnValue, methodParams);
    }
    
    public MethodInvokeTest(Class<C> clazz, C instance, String methodName, int methodModifiers, T acceptedReturnValue, Object... methodParams)
    {
        this(new MethodTest<>(clazz, instance, methodName, methodModifiers, acceptedReturnValue != null ? Util.unboxClass(acceptedReturnValue.getClass()) : void.class, Arrays.stream(methodParams).map(Object::getClass).map(Util::unboxClass).toArray(Class<?>[]::new)),
                instance, acceptedReturnValue, methodParams);
    }
    
    public MethodInvokeTest(Class<C> clazz, C instance, String methodName, T acceptedReturnValue, Object... methodParams)
    {
        this(new MethodTest<>(clazz, instance, methodName, acceptedReturnValue != null ? Util.unboxClass(acceptedReturnValue.getClass()) : void.class, Arrays.stream(methodParams).map(Object::getClass).map(Util::unboxClass).toArray(Class<?>[]::new)),
                instance, acceptedReturnValue, methodParams);
    }
    
    public MethodInvokeTest(Class<C> clazz, String methodName, T acceptedReturnValue, Object... methodParams)
    {
        this(clazz, null, methodName, acceptedReturnValue, methodParams);
    }
    
    public String getMethodName()
    {
        return methodTest.getMethodName();
    }
    
    public boolean testValue(PrintWriter errorCallback)
    {
        try
        {
            if(!methodTest.hasMethod(errorCallback))
            {
                return false;
            }
            
            Method method = methodTest.getMethod();
            
            T value = (T) method.invoke(instance, methodParams);
            
            if((method.getReturnType() != void.class && acceptedReturnValue == null && value != null) || (acceptedReturnValue != null && !acceptedReturnValue.equals(value)))
            {
                errorCallback.println("Wrong return value of method \"" + method.getName() + "\"" + " in class \"" + method.getDeclaringClass().getSimpleName() + "\":");
                Util.strongSpacer(errorCallback);
                errorCallback.println("Expected: " + acceptedReturnValue);
                Util.weakSpacer(errorCallback);
                errorCallback.println("Found: " + value);
                Util.strongSpacer(errorCallback);
                return false;
            }
        }
        catch(IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
