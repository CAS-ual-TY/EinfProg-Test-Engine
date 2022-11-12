package einfprog.test_engine;

import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class Engine
{
    public static final String NEWLINE = "\n";
    public static final Random RANDOM = new Random(0);
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
    
    public void beforeAll()
    {
        // formatting %f could result in commas as decimal points (instead of dots)
        Locale.setDefault(Locale.US);
    }
    
    private void beforeEach()
    {
        if(!initialized)
        {
            beforeAll();
            initialized = true;
        }
        
        userOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(userOut));
        
        RANDOM.setSeed(42);
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
    
    public String getRawOutput()
    {
        return userOut.toString();
    }
    
    public void checkTest(AbstractTest test) throws IOException
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
                pw.println("An error occurred while testing the program:");
                e.printStackTrace(pw);
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
}