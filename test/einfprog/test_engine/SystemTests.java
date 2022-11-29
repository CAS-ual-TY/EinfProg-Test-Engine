package einfprog.test_engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class SystemTests
{
    public int[] createArray1(int length)
    {
        int[] a = new int[length];
        for(int i = 0; i < length; i++)
        {
            a[i] = i;
        }
        return a;
    }
    
    public int[] createArray2(int length)
    {
        int[] a = new int[length];
        for(int i = 0; i < length; i++)
        {
            a[i] = length - i - 1;
        }
        return a;
    }
    
    public short[] createArray3(int length)
    {
        short[] a = new short[length];
        for(short i = 0; i < length; i++)
        {
            a[i] = i;
        }
        return a;
    }
    
    public String[] createArray4(int length)
    {
        String[] a = new String[length];
        for(int i = 0; i < length; i++)
        {
            a[i] = "String " + i;
        }
        return a;
    }
    
    @Test
    public void testStringUtils()
    {
        Assertions.assertEquals("test", Util.removeCR("test"));
        Assertions.assertEquals("test", Util.removeCR("test\r"));
        Assertions.assertEquals("test test", Util.removeMultiSpace("test test"));
        Assertions.assertEquals("test test", Util.removeMultiSpace("test   test"));
        Assertions.assertEquals("test\n", Util.removeMultiNewlines("test\n"));
        Assertions.assertEquals("test\n", Util.removeMultiNewlines("test\n\n\n"));
        Assertions.assertEquals("test test", Util.removeFakeSpace("test\ttest"));
        Assertions.assertEquals("test test", Util.removeFakeSpace("test test"));
        Assertions.assertEquals("test\ntest", Util.removeFakeSpace("test\ntest"));
    }
    
    @Test
    public void testArrays()
    {
        Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                SystemTests.class,
                this,
                "createArray1",
                createArray1(8),
                8)
        );
    
        Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                SystemTests.class,
                this,
                "createArray2",
                createArray2(8),
                8)
        );
    
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        createArray3(8),
                        8)
                )
        );
    
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        createArray4(8),
                        8)
                )
        );
    
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        null,
                        8)
                )
        );
    }
    
    private void shouldFail(Runnable r)
    {
        try
        {
            r.run();
        }
        catch(AssertionFailedError e)
        {
            return;
        }
        
        Assertions.fail("This should be unreachable...");
    }
}
