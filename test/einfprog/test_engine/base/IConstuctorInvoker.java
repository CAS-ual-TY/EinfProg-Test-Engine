package einfprog.test_engine.base;

import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;

import java.lang.reflect.InvocationTargetException;

public interface IConstuctorInvoker<C, P extends IParamTypeSet> extends IFeedback
{
    <PP extends IParamSet<? extends P>> C invoke(PP params) throws InvocationTargetException, InstantiationException, IllegalAccessException;
}
