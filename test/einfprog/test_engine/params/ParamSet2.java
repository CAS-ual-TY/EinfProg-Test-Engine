package einfprog.test_engine.params;

import einfprog.test_engine.Util;

import java.util.Arrays;

public class ParamSet2<A, B> extends ParamTypeSet2 implements IParamSet<ParamTypeSet2>
{
    private Object[] params;
    private A par1;
    private B par2;
    
    public ParamSet2(Object[] params)
    {
        super(Arrays.stream(params).map(par -> Util.unboxClass(par.getClass())).toArray(Class<?>[]::new));
        assert params.length == 2;
        this.params = params;
        par1 = (A) params[0];
        par2 = (B) params[1];
    }
    
    public ParamSet2(A par1, B par2)
    {
        this(new Object[] {par1, par2});
    }
    
    @Override
    public Object[] getParams()
    {
        return params;
    }
    
    public <V> V func(ParamSet2Func<A, B, V> func)
    {
        return func.func(par1, par2);
    }
    
    public interface ParamSet2Func<A, B, V>
    {
        V func(A par1, B par2);
    }
    
    public interface ParamSet2Cons<A, B> extends ParamSet2Func<A, B, Void>
    {
        void cons(A par1, B par2);
        
        @Override
        default Void func(A par1, B par2)
        {
            cons(par1, par2);
            return null;
        }
    }
}
