package einfprog.test_engine.base;

public interface IClassGetter<C> extends IFeedback
{
    Class<? extends C> findClass();
    
    String getClassName();
}
