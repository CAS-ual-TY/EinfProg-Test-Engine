package einfprog.test_engine.base;

public interface IFieldInvoker<C, T> extends IFeedback
{
    T invoke(C instance) throws IllegalAccessException;
}
