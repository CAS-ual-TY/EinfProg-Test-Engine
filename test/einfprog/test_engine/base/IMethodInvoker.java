package einfprog.test_engine.base;

import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.lang.reflect.InvocationTargetException;

public interface IMethodInvoker<T, C, P extends IParamTypeSet> extends IFeedback
{
    <PP extends IParamSet<? extends P>> T invoke(C instance, PP params) throws InvocationTargetException, IllegalAccessException;
}
