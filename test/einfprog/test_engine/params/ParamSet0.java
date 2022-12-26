package einfprog.test_engine.params;

public class ParamSet0 extends ParamTypeSet0 implements IParamSet<ParamTypeSet0>
{
    private Object[] params;
    
    public ParamSet0()
    {
        super();
        params = new Object[] {};
    }
    
    @Override
    public Object[] getParams()
    {
        return params;
    }
    
    public <V> V func(ParamSet0Func<V> func)
    {
        return func.func();
    }
    
    public interface ParamSet0Func<V>
    {
        V func();
    }
    
    public interface ParamSet0Cons extends ParamSet0Func<Void>
    {
        void cons();
        
        @Override
        default Void func()
        {
            cons();
            return null;
        }
    }
}
