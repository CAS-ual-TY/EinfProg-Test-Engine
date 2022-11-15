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
    
    private Engine()
    {
        initialized = false;
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
        initialize();
        userOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(userOut));
    }
    
    private void afterEach()
    {
        System.setOut(SYS_OUT);
        System.setIn(SYS_IN);
    }
    
    private void sendInput(String[] input)
    {
        System.setIn(new ByteArrayInputStream((String.join("\n", input) + "\n" + "0\n".repeat(100)).getBytes(StandardCharsets.UTF_8)));
    }
    
    private String getRawOutput()
    {
        return userOut.toString();
    }
    
    public void checkTest(OutputTest test) throws IOException
    {
        beforeEach();
        
        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw))
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
            
            String rawOut = getRawOutput();
            
            if(!successfulRun || !test.passes(pw, rawOut))
            {
                Assertions.fail(sw.toString());
            }
        }
        finally
        {
            afterEach();
        }
    }
    
    public <C, T> void checkTest(MethodTest<C, T> test) throws IOException
    {
        initialize();
        
        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw))
        {
            if(!test.hasMethod(pw) || !test.testValue(pw))
            {
                Assertions.fail(sw.toString());
            }
        }
    }
}