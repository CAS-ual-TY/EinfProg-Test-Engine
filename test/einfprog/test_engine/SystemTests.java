package einfprog.test_engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SystemTests
{
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
}
