package einfprog.test_engine;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodTest<T, C>
{
    private Class<C> clazz;
    private C instance;
    private String methodName;
    private int methodModifiers;
    private Class<?> methodReturnType;
    private Class<?>[] methodParamsTypes;
    
    private Method method;
    
    public MethodTest(Class<C> clazz, C instance, String methodName, int methodModifiers, Class<?> methodReturnType, Class<?>... methodParamsTypes)
    {
        assert !clazz.isArray();
        assert Modifier.isStatic(methodModifiers) == (instance == null);
        this.clazz = clazz;
        this.instance = instance;
        this.methodName = methodName;
        this.methodModifiers = methodModifiers;
        this.methodReturnType = methodReturnType;
        this.methodParamsTypes = methodParamsTypes;
    }
    
    public MethodTest(Class<C> clazz, C instance, String methodName, Class<?> methodReturnType, Class<?>... methodParamsTypes)
    {
        this(clazz, instance, methodName, Modifier.PUBLIC | (instance == null ? Modifier.STATIC : 0), methodReturnType, methodParamsTypes);
    }
    
    public MethodTest(Class<C> clazz, String methodName, Class<?> methodReturnType, Class<?>... methodParamsTypes)
    {
        this(clazz, null, methodName, methodReturnType, methodParamsTypes);
    }
    
    public Class<C> getMethodClass()
    {
        return clazz;
    }
    
    public C getMethodInstance()
    {
        return instance;
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    
    public int getMethodModifiers()
    {
        return methodModifiers;
    }
    
    public Class<?> getMethodReturnType()
    {
        return methodReturnType;
    }
    
    public Class<?>[] getMethodParamsTypes()
    {
        return methodParamsTypes;
    }
    
    public Method getMethod()
    {
        return method;
    }
    
    public boolean hasMethod(PrintWriter errorCallback)
    {
        try
        {
            method = clazz.getDeclaredMethod(methodName, methodParamsTypes);
            
            int modifiers = method.getModifiers();
            
            if(method.getModifiers() != methodModifiers)
            {
                errorCallback.println("Wrong signature of method \"" + methodName + "\"" + " in class \"" + clazz.getSimpleName() + "\":");
                Util.strongSpacer(errorCallback);
                errorCallback.println("Expected: " + Modifier.toString(methodModifiers));
                Util.weakSpacer(errorCallback);
                errorCallback.println("Found: " + Modifier.toString(method.getModifiers()));
                Util.strongSpacer(errorCallback);
                return false;
            }
            
            if(method.getReturnType() != methodReturnType)
            {
                errorCallback.println("Wrong return type of method \"" + methodName + "\"" + " in class \"" + clazz.getSimpleName() + "\":");
                Util.strongSpacer(errorCallback);
                errorCallback.println("Expected: " + methodReturnType.getSimpleName());
                Util.weakSpacer(errorCallback);
                errorCallback.println("Found: " + method.getReturnType().getSimpleName());
                Util.strongSpacer(errorCallback);
                return false;
            }
            
            return true;
        }
        catch(NoSuchMethodException e)
        {
            errorCallback.println("Can not find method \"" + methodName + "\"" + " in class \"" + clazz.getSimpleName() + "\":");
            Util.strongSpacer(errorCallback);
            errorCallback.println("Expected:");
            errorCallback.println("  (This is to be done:)");
            Util.weakSpacer(errorCallback);
            errorCallback.println(Modifier.toString(methodModifiers) + " " + methodReturnType.getSimpleName() + " " + methodName + "(" + Arrays.stream(methodParamsTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
            Util.strongSpacer(errorCallback);
            return false;
        }
    }
}
