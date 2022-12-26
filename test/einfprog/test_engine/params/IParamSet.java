package einfprog.test_engine.params;

public interface IParamSet<PP extends IParamTypeSet> extends IParamTypeSet
{
    Object[] getParams();
    
    default PP castToTypes()
    {
        return (PP) this;
    }
}
