package einfprog.test_engine.base;

import einfprog.test_engine.Util;

import java.io.PrintWriter;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValueTester<T> extends TestBase implements IValueTester<T>
{
    private IValueGetter<T> value;
    private Predicate<T> predicate;
    private Function<T, String> toStringFunc;
    private String expected;
    
    public ValueTester(IValueGetter<T> value, Predicate<T> predicate, Function<T, String> toStringFunc, String expected)
    {
        this.value = value;
        this.predicate = predicate;
        this.toStringFunc = toStringFunc;
        this.expected = expected;
    }
    
    public ValueTester(IValueGetter<T> value, Function<T, String> toStringFunc, T expectedValue)
    {
        this(value, v -> Util.objectsEquals(expectedValue, v), toStringFunc, toStringFunc.apply(expectedValue));
    }
    
    public ValueTester(IValueGetter<T> value, T expectedValue)
    {
        this(value, Util::objectToString, expectedValue);
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
        errorCallback.println(toStringFunc.apply(value));
        
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
