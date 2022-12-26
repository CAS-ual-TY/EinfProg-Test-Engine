package einfprog.test_engine.params;

import einfprog.test_engine.Util;

import java.util.Arrays;

public class ParamSet4<A, B, C, D> extends ParamTypeSet4 implements IParamSet<ParamTypeSet4>
{
    private Object[] params;
    private A par1;
    private B par2;
    private C par3;
    private D par4;
    
    public ParamSet4(Object[] params)
    {
        super(Arrays.stream(params).map(par -> Util.unboxClass(par.getClass())).toArray(Class<?>[]::new));
        assert params.length == 4;
        this.params = params;
        par1 = (A) params[0];
        par2 = (B) params[1];
        par3 = (C) params[2];
        par4 = (D) params[3];
    }
    
    public ParamSet4(A par1, B par2, C par3, D par4)
    {
        this(new Object[] {par1, par2, par3, par4});
    }
    
    @Override
    public Object[] getParams()
    {
        return params;
    }
    
    public <V> V func(ParamSet4Func<A, B, C, D, V> func)
    {
        return func.func(par1, par2, par3, par4);
    }
    
    public interface ParamSet4Func<A, B, C, D, V>
    {
        V func(A par1, B par2, C par3, D par4);
    }
    
    public interface ParamSet4Cons<A, B, C, D> extends ParamSet4Func<A, B, C, D, Void>
    {
        void cons(A par1, B par2, C par3, D par4);
        
        @Override
        default Void func(A par1, B par2, C par3, D par4)
        {
            cons(par1, par2, par3, par4);
            return null;
        }
    }
}
