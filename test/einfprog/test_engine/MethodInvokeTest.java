package einfprog.test_engine;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MethodInvokeTest<T, C>
{
    private MethodTest<T, C> methodTest;
    private C instance;
    private String returnValueString;
    private Predicate<T> acceptedReturnValue;
    private Object[] methodParams;
    
    public MethodInvokeTest(MethodTest<T, C> methodTest, C instance, String returnValueString, Predicate<T> acceptedReturnValue, Object... methodParams)
    {
        this.methodTest = methodTest;
        this.instance = instance;
        this.returnValueString = returnValueString;
        this.acceptedReturnValue = acceptedReturnValue;
        this.methodParams = methodParams;
    }
    
    public MethodInvokeTest(MethodTest<T, C> methodTest, String returnValueString, Predicate<T> acceptedReturnValue, Object... methodParams)
    {
        this(methodTest, null, returnValueString, acceptedReturnValue, methodParams);
    }
    
    public MethodInvokeTest(MethodTest<T, C> methodTest, C instance, T acceptedReturnValue, Object... methodParams)
    {
        this(methodTest, instance, acceptedReturnValue != null ? acceptedReturnValue.toString() : "null", acceptedReturnValue != null ? (acceptedReturnValue.getClass().isArray() ? (x -> x.getClass().isArray() && Arrays.equals((Object[]) acceptedReturnValue, (Object[]) x)) : acceptedReturnValue::equals) : (Objects::isNull), methodParams);
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
    
    public Class<C> getMethodClass()
    {
        return methodTest.getMethodClass();
    }
    
    public Object[] getMethodParams()
    {
        return methodParams;
    }
    
    public String getMethodName()
    {
        return methodTest.getMethodName();
    }
    
    public Class<?>[] getMethodParamsTypes()
    {
        return methodTest.getMethodParamsTypes();
    }
    
    public String getMethodCall()
    {
        return getMethodClass().getSimpleName() + "." + getMethodName() + "(" + Arrays.stream(methodParams).map(o -> o != null ? (o instanceof String ? "\"" + o.toString() + "\"" : o.toString()) : "null").collect(Collectors.joining(", ")) + ")";
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
            
            if((method.getReturnType() == void.class && value != null) || !acceptedReturnValue.test(value))
            {
                errorCallback.println("Wrong return value when calling method \"" + getMethodName() + "\" in class \"" + getMethodClass().getSimpleName() + "\":");
                Util.strongSpacer(errorCallback);
                errorCallback.println("Expected: " + returnValueString);
                Util.weakSpacer(errorCallback);
                errorCallback.println("Found: " + value);
                
                Util.strongSpacer(errorCallback);
                
                appendParams(errorCallback);
                
                return false;
            }
        }
        catch(InvocationTargetException e)
        {
            errorCallback.println("Exception thrown when calling method \"" + getMethodName() + "\" in class \"" + getMethodClass().getSimpleName() + "\":");
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
            
            return false;
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public void appendParams(PrintWriter errorCallback)
    {
        if(methodParams.length > 0)
        {
            
            errorCallback.println("With the following parameters: ");
            Util.weakSpacer(errorCallback);
            for(int i = 0; i < methodParams.length; i++)
            {
                if(methodParams[i] == null)
                {
                    errorCallback.println("null");
                }
                else
                {
                    Class<?> type = methodTest.getMethodParamsTypes()[i];
                    String param = methodParams[i].toString();
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
