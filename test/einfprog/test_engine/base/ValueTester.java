package einfprog.test_engine.base;

import einfprog.test_engine.Util;

import java.io.PrintWriter;
import java.util.function.Predicate;

public class ValueTester<T> extends TestBase implements IValueTester<T>
{
    private IValueGetter<T> value;
    private Predicate<T> predicate;
    private String expected;
    
    public ValueTester(IValueGetter<T> value, Predicate<T> predicate, String expected)
    {
        this.value = value;
        this.predicate = predicate;
        this.expected = expected;
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        if(!value.test(errorCallback))
        {
            return false;
        }
        
        T value = this.value.getValue();
        
        if(predicate.test(value))
        {
            return true;
        }
        else
        {
            eException(value, errorCallback);
            return false;
        }
    }
    
    public void eException(T value, PrintWriter errorCallback)
    {
        errorCallback.println("Wrong value:");
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Found:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(value);
        
        Util.strongSpacer(errorCallback);
        
        errorCallback.println("Expected:");
        Util.weakSpacer(errorCallback);
        errorCallback.println(expected);
        
        Util.strongSpacer(errorCallback);
        
        this.value.appendFeedback(errorCallback);
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        value.appendFeedback(errorCallback);
    }
}
