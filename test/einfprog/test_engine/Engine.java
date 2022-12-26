package einfprog.test_engine;

import einfprog.test_engine.base.IFeedback;
import einfprog.test_engine.base.IOutputProcessor;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Consumer;

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
    
    public boolean requiresDoesNotFail(Runnable test, String messageIfFail)
    {
        try
        {
            test.run();
            return true;
        }
        catch(AssertionFailedError e)
        {
            Assertions.fail(messageIfFail);
            return false;
        }
    }
    
    public boolean checkTest(IFeedback test, IOutputProcessor outputProcessor, Consumer<String> unusedFeedback, Consumer<PrintWriter> errorCallback)
    {
        beforeEach();
        
        try
        {
            sendInput(outputProcessor.getInput());
            
            boolean successfulRun = false;
            
            try
            {
                successfulRun = test.test(pw);
            }
            catch(Throwable e)
            {
                Util.error(pw, e);
            }
            
            String rawOut = getRawOutput();
            
            if(!successfulRun)
            {
                errorCallback.accept(pw);
                Assertions.fail(sw.toString());
                return false;
            }
            else if(!outputProcessor.testOutput(pw, rawOut))
            {
                test.appendFeedback(pw);
                errorCallback.accept(pw);
                Assertions.fail(sw.toString());
                return false;
            }
            
            test.appendFeedback(pw);
            unusedFeedback.accept(sw.toString());
            return true;
        }
        finally
        {
            afterEach();
        }
    }
}