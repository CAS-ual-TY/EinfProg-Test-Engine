package einfprog.test_engine;

import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Engine
{
    public static final Engine ENGINE = new Engine();
    
    public static final PrintStream SYS_OUT = System.out;
    public static final InputStream SYS_IN = System.in;
    
    private ByteArrayOutputStream userOut;
    private PrintStream userIn;
    
    private boolean initialized;
    private int testDepth;
    private boolean testFailed;
    
    private StringWriter sw;
    private PrintWriter pw;
    
    private Engine()
    {
        initialized = false;
        testDepth = 0;
    }
    
    private void initialize()
    {
        if(!initialized)
        {
            // formatting %f could result in commas as decimal points (instead of dots)
            Locale.setDefault(Locale.US);
            initialized = true;
        }
    }
    
    private void beforeEach()
    {
        if(testDepth == 0)
        {
            initialize();
            testFailed = false;
            
            userOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(userOut));
            
            sw = new StringWriter();
            pw = new PrintWriter(sw);
        }
        
        testDepth++;
    }
    
    private void afterEach()
    {
        if(testDepth == 1)
        {
            System.setOut(SYS_OUT);
            System.setIn(SYS_IN);
            
            pw.close();
        }
        
        testDepth--;
    }
    
    private void sendInput(String[] input)
    {
        System.setIn(new ByteArrayInputStream((String.join("\n", input) + "\n" + "0\n".repeat(100)).getBytes(StandardCharsets.UTF_8)));
    }
    
    private String getRawOutput()
    {
        return userOut.toString();
    }
    
    public boolean checkTest(OutputTest test)
    {
        beforeEach();
        
        try
        {
            sendInput(test.getInput());
            
            boolean successfulRun = false;
            
            try
            {
                test.run();
                successfulRun = true;
            }
            catch(Exception e)
            {
                Util.strongSpacer(pw);
                pw.println("An error occurred while testing the program:");
                Util.weakSpacer(pw);
                e.printStackTrace(pw);
                Util.strongSpacer(pw);
            }
            
            if(testFailed)
            {
                return false;
            }
            
            String rawOut = getRawOutput();
            
            if(!successfulRun || !test.passes(pw, rawOut))
            {
                Assertions.fail(sw.toString());
                testFailed = true;
                return false;
            }
            
            return true;
        }
        finally
        {
            afterEach();
        }
    }
    
    public <C, T> boolean checkTest(MethodTest<C, T> test)
    {
        beforeEach();
        
        try
        {
            if(!test.hasMethod(pw))
            {
                Assertions.fail(sw.toString());
                testFailed = true;
                return false;
            }
            
            return true;
        }
        finally
        {
            afterEach();
        }
    }
    
    public <C, T> boolean checkTest(MethodInvokeTest<C, T> test)
    {
        beforeEach();
        
        try
        {
            if(!test.testValue(pw))
            {
                Assertions.fail(sw.toString());
                testFailed = true;
                return false;
            }
            
            return true;
        }
        finally
        {
            afterEach();
        }
    }
}