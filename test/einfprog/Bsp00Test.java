package einfprog;

import einfprog.test_engine.TestMaker;
import einfprog.test_engine.output.Compound;
import org.junit.jupiter.api.Test;

public class Bsp00Test
{
    @Test
    public void test1() {
        TestMaker.callMain("einfprog.Bsp00")
                .expectConsoleOutput(Compound.construct("Hello World :-)"))
                .runTest();
    }
}
