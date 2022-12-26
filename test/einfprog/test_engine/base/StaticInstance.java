package einfprog.test_engine.base;

import einfprog.test_engine.params.IParamTypeSet;

import java.io.PrintWriter;

public class StaticInstance<T, C, P extends IParamTypeSet> extends TestBase implements IInstanceGetter<C>
{
    public IClassGetter<C> clazz;
    
    public StaticInstance(IClassGetter<C> clazz)
    {
        this.clazz = clazz;
    }
    
    @Override
    public boolean doTest(PrintWriter errorCallback)
    {
        return true;
    }
    
    @Override
    public void doAppendFeedback(PrintWriter errorCallback)
    {
        clazz.appendFeedback(errorCallback);
    }
    
    @Override
    public C getInstance()
    {
        return null;
    }
}
