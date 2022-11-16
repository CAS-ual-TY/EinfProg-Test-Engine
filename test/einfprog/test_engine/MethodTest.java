package einfprog.test_engine;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodTest<T, C>
{
    private Class<C> clazz;
    private C instance;
    private String methodName;
    private Class<?> methodReturnType;
    private T acceptedReturnValue;
    private Class<?>[] methodParamsTypes;
    private Object[] methodParams;
    
    private Method m;
    
    public MethodTest(Class<C> clazz, C instance, String methodName, Class<?> methodReturnType, T acceptedReturnValue, Class<?>[] methodParamsTypes, Object[] methodParams)
    {
        assert methodParamsTypes.length == methodParams.length;
        assert !clazz.isArray();
        this.clazz = clazz;
        this.instance = instance;
        this.methodName = methodName;
        this.methodReturnType = methodReturnType;
        this.acceptedReturnValue = acceptedReturnValue;
        this.methodParamsTypes = methodParamsTypes;
        this.methodParams = methodParams;
    }
    
    public MethodTest(Class<C> clazz, C instance, String methodName, Class<?> methodReturnType, T acceptedReturnValue, Object[] methodParams)
    {
        this(clazz, instance, methodName, methodReturnType, acceptedReturnValue, Arrays.stream(methodParams).map(Object::getClass).map(Util::unboxClass).toArray(Class<?>[]::new), methodParams);
    }
    
    public MethodTest(Class<C> clazz, String methodName, Class<?> methodReturnType, T acceptedReturnValue, Object... methodParams)
    {
        this(clazz, null, methodName, methodReturnType, acceptedReturnValue, methodParams);
    }
    
    public MethodTest(Class<C> clazz, String methodName, T acceptedReturnValue, Object... methodParams)
    {
        this(clazz, null, methodName, acceptedReturnValue != null ? Util.unboxClass(acceptedReturnValue.getClass()) : void.class, acceptedReturnValue, methodParams);
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    
    public boolean hasMethod(PrintWriter errorCallback)
    {
        try
        {
            m = clazz.getMethod(methodName, methodParamsTypes);
            
            if(m.getReturnType() != methodReturnType)
            {
                Util.strongSpacer(errorCallback);
                errorCallback.println("Wrong return type of method \"" + methodName + "\"" + " in class \"" + clazz.getSimpleName() + "\":");
                Util.strongSpacer(errorCallback);
                errorCallback.println("Expected: " + methodReturnType.getSimpleName());
                Util.weakSpacer(errorCallback);
                errorCallback.println("Found: " + m.getReturnType().getSimpleName());
                Util.strongSpacer(errorCallback);
                return false;
            }
            
            return true;
        }
        catch(NoSuchMethodException e)
        {
            Util.strongSpacer(errorCallback);
            errorCallback.println("Can not find method \"" + methodName + "\"" + " in class \"" + clazz.getSimpleName() + "\":");
            Util.strongSpacer(errorCallback);
            errorCallback.println("Expected:");
            errorCallback.println("  (This is to be done:)");
            Util.weakSpacer(errorCallback);
            errorCallback.println("public" + " " + (instance == null ? "static" : "") + " " + (methodReturnType != null ? methodReturnType.getSimpleName() : "void") + " " + methodName + "(" + Arrays.stream(methodParamsTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
            Util.strongSpacer(errorCallback);
            return false;
        }
    }
    
    public boolean testValue(PrintWriter errorCallback)
    {
        try
        {
            T value = (T) m.invoke(instance, methodParams);
            
            if((methodReturnType != void.class && acceptedReturnValue == null && value != null) || (acceptedReturnValue != null && !acceptedReturnValue.equals(value)))
            {
                Util.strongSpacer(errorCallback);
                errorCallback.println("Wrong return value of method \"" + methodName + "\"" + " in class \"" + clazz.getSimpleName() + "\":");
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
