package einfprog.test_engine.params;

public class ParamTypeSet1 implements IParamTypeSet
{
    private Class<?>[] paramTypes;
    private Class<?> par1;
    
    public ParamTypeSet1(Class<?>[] paramTypes)
    {
        assert paramTypes.length == 1;
        this.paramTypes = paramTypes;
        par1 = paramTypes[0];
    }
    
    public ParamTypeSet1(Class<?> par1)
    {
        this(new Class<?>[] {par1});
    }
    
    @Override
    public Class<?>[] getParamTypes()
    {
        return paramTypes;
    }
}
