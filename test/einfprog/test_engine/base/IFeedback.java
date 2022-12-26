package einfprog.test_engine.base;

import java.io.PrintWriter;

public interface IFeedback extends ITest
{
    void appendFeedback(PrintWriter errorCallback);
}
