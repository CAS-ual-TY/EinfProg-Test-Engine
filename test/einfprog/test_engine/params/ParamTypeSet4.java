package einfprog.test_engine.params;

public class ParamTypeSet4 implements IParamTypeSet
{
    private Class<?>[] paramTypes;
    private Class<?> par1;
    private Class<?> par2;
    private Class<?> par3;
    private Class<?> par4;
    
    public ParamTypeSet4(Class<?>[] paramTypes)
    {
        assert paramTypes.length == 4;
        this.paramTypes = paramTypes;
        par1 = paramTypes[0];
        par2 = paramTypes[1];
        par3 = paramTypes[2];
        par4 = paramTypes[3];
    }
    
    public ParamTypeSet4(Class<?> par1, Class<?> par2, Class<?> par3, Class<?> par4)
    {
        this(new Class<?>[] {par1, par2, par3, par4});
    }
    
    @Override
    public Class<?>[] getParamTypes()
    {
        return paramTypes;
    }
}
