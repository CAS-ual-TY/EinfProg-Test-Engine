package einfprog.test_engine.base;

import java.io.PrintWriter;

public interface IOutputProcessor
{
    boolean testOutput(PrintWriter errorCallback, String rawOut);
    
    String[] getInput();
}
