package einfprog.test_engine.params;

public class ParamTypeSet2 implements IParamTypeSet
{
    private Class<?>[] paramTypes;
    private Class<?> par1;
    private Class<?> par2;
    
    public ParamTypeSet2(Class<?>[] paramTypes)
    {
        assert paramTypes.length == 2;
        this.paramTypes = paramTypes;
        par1 = paramTypes[0];
        par2 = paramTypes[1];
    }
    
    public ParamTypeSet2(Class<?> par1, Class<?> par2)
    {
        this(new Class<?>[] {par1, par2});
    }
    
    @Override
    public Class<?>[] getParamTypes()
    {
        return paramTypes;
    }
}
