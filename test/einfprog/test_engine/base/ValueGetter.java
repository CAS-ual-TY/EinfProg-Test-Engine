package einfprog.test_engine.base;

import java.io.PrintWriter;

public class ValueGetter<T> extends TestBase implements IValueGetter<T>
{
    private T value;
    
    public ValueGetter(T value)
    {
        this.value = value;
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        return true;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
    
    }
    
    @Override
    public T getValue()
    {
        return value;
    }
}
