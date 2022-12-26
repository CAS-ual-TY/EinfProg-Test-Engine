package einfprog.test_engine.params;

import einfprog.test_engine.Util;

import java.util.Arrays;

public class ParamSet3<A, B, C> extends ParamTypeSet3 implements IParamSet<ParamTypeSet3>
{
    private Object[] params;
    private A par1;
    private B par2;
    private C par3;
    
    public ParamSet3(Object[] params)
    {
        super(Arrays.stream(params).map(par -> Util.unboxClass(par.getClass())).toArray(Class<?>[]::new));
        assert params.length == 3;
        this.params = params;
        par1 = (A) params[0];
        par2 = (B) params[1];
        par3 = (C) params[2];
    }
    
    public ParamSet3(A par1, B par2, C par3)
    {
        this(new Object[] {par1, par2, par3});
    }
    
    @Override
    public Object[] getParams()
    {
        return params;
    }
    
    public <V> V func(ParamSet3Func<A, B, C, V> func)
    {
        return func.func(par1, par2, par3);
    }
    
    public interface ParamSet3Func<A, B, C, V>
    {
        V func(A par1, B par2, C par3);
    }
    
    public interface ParamSet3Cons<A, B, C> extends ParamSet3Func<A, B, C, Void>
    {
        void cons(A par1, B par2, C par3);
        
        @Override
        default Void func(A par1, B par2, C par3)
        {
            cons(par1, par2, par3);
            return null;
        }
    }
}
