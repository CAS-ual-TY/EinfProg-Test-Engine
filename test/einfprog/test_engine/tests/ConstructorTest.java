package einfprog.test_engine.tests;

import einfprog.test_engine.Util;
import einfprog.test_engine.base.IClassGetter;
import einfprog.test_engine.base.IConstuctorInvoker;
import einfprog.test_engine.base.TestBase;
import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConstructorTest<C, P extends IParamTypeSet> extends TestBase implements IConstuctorInvoker<C, P>
{
    public IClassGetter<C> clazz;
    public int modifiers;
    public P params;
    
    public Constructor<? extends C> constructor;
    
    public ConstructorTest(IClassGetter<C> clazz, int modifiers, P params)
    {
        this.clazz = clazz;
        this.modifiers = modifiers;
        this.params = params;
        constructor = null;
    }
    
    public ConstructorTest(IClassGetter<C> clazz, P params)
    {
        this(clazz, Modifier.PUBLIC, params);
    }
    
    public String constructorToString()
    {
        return Modifier.toString(modifiers) + " " + clazz.getClassName() + " (" + Arrays.stream(params.getParamTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")";
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
            
            constructor = clazz.getDeclaredConstructor(params.getParamTypes());
            
            int modifiers = constructor.getModifiers();
            if(constructor.getModifiers() != this.modifiers)
            {
                eWrongModifiers(modifiers, errorCallback);
                return false;
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
        errorCallback.println("Can not find constructor:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(constructorToString());
        Util.strongSpacer(errorCallback);
        
        clazz.appendFeedback(errorCallback);
    }
    
    public void eWrongModifiers(int modifiers, PrintWriter errorCallback)
    {
        errorCallback.println("Wrong signature for constructor:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(constructorToString());
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Found:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(Modifier.toString(modifiers) + " " + clazz.getClassName() + " (" + Arrays.stream(params.getParamTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
        Util.strongSpacer(errorCallback);
        
        clazz.appendFeedback(errorCallback);
    }
    
    @Override
    public <PP extends IParamSet<? extends P>> C invoke(PP params) throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        return constructor.newInstance(params.getParams());
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        clazz.appendFeedback(errorCallback);
    
        errorCallback.println("After invoking constructor:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(constructorToString());
        Util.strongSpacer(errorCallback);
    }
}
