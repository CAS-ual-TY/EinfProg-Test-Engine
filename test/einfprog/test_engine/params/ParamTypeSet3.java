package einfprog.test_engine.params;

public class ParamTypeSet3 implements IParamTypeSet
{
    private Class<?>[] paramTypes;
    private Class<?> par1;
    private Class<?> par2;
    private Class<?> par3;
    
    public ParamTypeSet3(Class<?>[] paramTypes)
    {
        assert paramTypes.length == 3;
        this.paramTypes = paramTypes;
        par1 = paramTypes[0];
        par2 = paramTypes[1];
        par3 = paramTypes[2];
    }
    
    public ParamTypeSet3(Class<?> par1, Class<?> par2, Class<?> par3)
    {
        this(new Class<?>[] {par1, par2, par3});
    }
    
    @Override
    public Class<?>[] getParamTypes()
    {
        return paramTypes;
    }
}
