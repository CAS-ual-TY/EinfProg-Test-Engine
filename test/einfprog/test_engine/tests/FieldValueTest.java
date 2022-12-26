package einfprog.test_engine.tests;

import einfprog.test_engine.Util;
import einfprog.test_engine.base.IClassGetter;
import einfprog.test_engine.base.IInstanceGetter;
import einfprog.test_engine.base.IValueGetter;
import einfprog.test_engine.base.TestBase;

import java.io.PrintWriter;

public class FieldValueTest<T, C> extends TestBase implements IValueGetter<T>
{
    public FieldTest<C, T> fieldTest;
    public IInstanceGetter<C> instance;
    
    public T value;
    
    public FieldValueTest(FieldTest<C, T> fieldTest, IInstanceGetter<C> instance)
    {
        this.fieldTest = fieldTest;
        this.instance = instance;
        value = null;
    }
    
    public FieldValueTest(IClassGetter<C> clazz, IInstanceGetter<C> instance, String fieldName, int modifiers, Class<? extends T> fieldType)
    {
        this(new FieldTest<>(clazz, fieldName, modifiers, fieldType), instance);
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        if(!fieldTest.test(errorCallback) || !instance.test(errorCallback))
        {
            return false;
        }
        
        try
        {
            value = fieldTest.invoke(instance.getInstance());
            return true;
        }
        catch(IllegalAccessException e)
        {
            Util.error(errorCallback, e);
            return false;
        }
    }
    
    @Override
    public T getValue()
    {
        return value;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        fieldTest.appendFeedback(errorCallback);
        instance.appendFeedback(errorCallback);
    }
}
