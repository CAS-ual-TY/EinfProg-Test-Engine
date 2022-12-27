package einfprog.test_engine.params;

import einfprog.test_engine.Util;

import java.util.Arrays;

public class ParamSet1<A> extends ParamTypeSet1 implements IParamSet<ParamTypeSet1>
{
    private Object[] params;
    private A par1;
    
    public ParamSet1(Class<?>[] paramTypes, Object[] params)
    {
        super(paramTypes);
        assert params.length == 1;
        this.params = params;
        par1 = (A) params[0];
    }
    
    public ParamSet1(Object[] params)
    {
        this(Arrays.stream(params).map(par -> Util.unboxClass(par.getClass())).toArray(Class<?>[]::new), params);
    }
    
    public ParamSet1(Class<?> par1Class, A par1)
    {
        this(new Class<?>[] {par1Class}, new Object[] {par1});
    }
    
    public ParamSet1(A par1)
    {
        this(new Object[] {par1});
    }
    
    @Override
    public Object[] getParams()
    {
        return params;
    }
    
    public <V> V func(ParamSet1Func<A, V> func)
    {
        return func.func(par1);
    }
    
    public interface ParamSet1Func<A, V>
    {
        V func(A par1);
    }
    
    public interface ParamSet1Cons<A> extends ParamSet1Func<A, Void>
    {
        void cons(A par1);
        
        @Override
        default Void func(A par1)
        {
            cons(par1);
            return null;
        }
    }
}
