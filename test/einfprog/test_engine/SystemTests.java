package einfprog.test_engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Modifier;
import java.util.Arrays;

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
    
    public int testArray(int[] array)
    {
        return Arrays.stream(array).findFirst().orElse(0);
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
                Util.objArr(8))
        );
    
        Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                SystemTests.class,
                this,
                "createArray2",
                createArray2(8),
                Util.objArr(8))
        );
    
        Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                SystemTests.class,
                this,
                "testArray",
                Modifier.PUBLIC,
                testArray(createArray1(8)),
                Util.objArr(createArray1(8))
        ));
        
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        createArray2(8),
                        Util.objArr(8))
                ));
        
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        createArray3(8),
                        Util.objArr(8))
                )
        );
        
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        createArray4(8),
                        Util.objArr(8))
                )
        );
        
        shouldFail(() ->
                Engine.ENGINE.checkTest(new MethodInvokeTest<>(
                        SystemTests.class,
                        this,
                        "createArray1",
                        null,
                        Util.objArr(8))
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
            e.printStackTrace();
            return;
        }
        
        Assertions.fail("This should be unreachable...");
    }
}
