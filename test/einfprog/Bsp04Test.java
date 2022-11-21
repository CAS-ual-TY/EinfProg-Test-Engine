package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.MethodInvokeTest;
import einfprog.test_engine.MethodTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class Bsp04Test
{
    private int charScore(char c)
    {
        if(c >= 'a')
        {
            c = (char) (c - 'a' + 'A');
        }
        
        return switch(c)
                {
                    case 'F', 'H', 'V', 'W', 'Y' -> 4;
                    case 'J', 'X' -> 8;
                    case 'Q', 'Z' -> 10;
                    default -> 1;
                };
    }
    
    @Test
    public void test1()
    {
        // #################################### Test isPalindrome(...) ####################################
        
        String[][] strings = {{"ana", "anna", "tacocat"}, {"ananas", "banana", "alphabet"}, {"Xylophone", "Alphabet"}, {"", null}};
        
        for(int i = 0; i < 2; i++)
        {
            String[] ss = strings[i];
            
            for(String s : ss)
            {
                MethodInvokeTest<Boolean, Bsp04> t = new MethodInvokeTest<>(
                        Bsp04.class,
                        "isPalindrome",
                        i == 0,
                        s
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
        
        // #################################### Test wordScore(...) ####################################
        
        for(String[] ss : strings)
        {
            for(String s : ss)
            {
                int sum = 0;
                
                if(s == null)
                {
                    sum = -1;
                }
                else
                {
                    for(char c : s.toCharArray())
                    {
                        sum += charScore(c);
                    }
                }
                
                MethodTest<Integer, Bsp04> t0 = new MethodTest<>(
                        Bsp04.class,
                        "wordScore",
                        int.class,
                        String.class
                );
                
                MethodInvokeTest<Integer, Bsp04> t = new MethodInvokeTest<>(
                        t0,
                        sum,
                        s
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
    }
    
    @Test
    public void test2()
    {
        try
        {
            test1();
        }
        catch(AssertionFailedError e)
        {
            Assertions.fail("Part 1 failed.");
        }
        
        
    }
}
