package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.MethodTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Bsp03Test
{
    // TODO do proper test of Bsp03
    
    @Test
    public void test1() throws IOException
    {
        MethodTest<Integer, Bsp03> t = new MethodTest<>(
                Bsp03.class,
                "test",
                3,
                7, 7.0
        );
        
        Engine.ENGINE.checkTest(t);
    }
}
