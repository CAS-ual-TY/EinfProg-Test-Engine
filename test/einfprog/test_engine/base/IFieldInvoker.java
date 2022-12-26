package einfprog.test_engine.base;

import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.lang.reflect.InvocationTargetException;

public interface IFieldInvoker<C, T> extends IFeedback
{
    T invoke(C instance) throws IllegalAccessException;
}
