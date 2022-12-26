package einfprog.test_engine.params;

public class ParamTypeSet0 implements IParamTypeSet
{
    private Class<?>[] paramTypes;
    
    public ParamTypeSet0()
    {
        paramTypes = new Class<?>[] {};
    }
    
    @Override
    public Class<?>[] getParamTypes()
    {
        return paramTypes;
    }
}
