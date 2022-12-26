package einfprog.test_engine.tests;

import einfprog.test_engine.Util;
import einfprog.test_engine.base.IClassGetter;
import einfprog.test_engine.base.IFieldInvoker;
import einfprog.test_engine.base.TestBase;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldTest<C, T> extends TestBase implements IFieldInvoker<C, T>
{
    public IClassGetter<C> clazz;
    public String fieldName;
    public int modifiers;
    public Class<?> fieldType;
    
    public Field field;
    
    public FieldTest(IClassGetter<C> clazz, String fieldName, int modifiers, Class<?> fieldType)
    {
        this.clazz = clazz;
        this.fieldName = fieldName;
        this.modifiers = modifiers;
        this.fieldType = fieldType;
        field = null;
    }
    
    public FieldTest(IClassGetter<C> clazz, String fieldName, Class<?> fieldType)
    {
        this(clazz, fieldName, Modifier.PUBLIC, fieldType);
    }
    
    public String fieldToString()
    {
        return Modifier.toString(modifiers) + " " + fieldType.getSimpleName() + " " + fieldName;
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
            
            field = clazz.getDeclaredField(fieldName);
            
            int modifiers = field.getModifiers();
            if(field.getModifiers() != this.modifiers)
            {
                eWrongModifiers(modifiers, errorCallback);
                return false;
            }
            
            Class<?> type = field.getType();
            if(type != fieldType)
            {
                eWrongType(type, errorCallback);
                return false;
            }
            
            if(Modifier.isPrivate(modifiers))
            {
                field.setAccessible(true);
            }
            
            return true;
        }
        catch(NoSuchFieldException e)
        {
            eNotFound(errorCallback);
            return false;
        }
    }
    
    public void eNotFound(PrintWriter errorCallback)
    {
        errorCallback.println("Can not find field:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(fieldToString());
        Util.strongSpacer(errorCallback);
        
        clazz.appendFeedback(errorCallback);
    }
    
    public void eWrongModifiers(int modifiers, PrintWriter errorCallback)
    {
        errorCallback.println("Wrong signature for field:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(fieldToString());
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Found:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(Modifier.toString(modifiers) + " " + fieldType.getSimpleName() + " " + fieldName);
        Util.strongSpacer(errorCallback);
        
        clazz.appendFeedback(errorCallback);
    }
    
    public void eWrongType(Class<?> type, PrintWriter errorCallback)
    {
        errorCallback.println("Wrong return type of method:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(fieldToString());
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Found:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(Modifier.toString(modifiers) + " " + type.getSimpleName() + " " + fieldName);
        
        Util.strongSpacer(errorCallback);
    }
    
    @Override
    public T invoke(C instance) throws IllegalAccessException
    {
        return (T) field.get(instance);
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        clazz.appendFeedback(errorCallback);
        
        errorCallback.println("When accessing field:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(fieldToString());
        Util.strongSpacer(errorCallback);
    }
}
