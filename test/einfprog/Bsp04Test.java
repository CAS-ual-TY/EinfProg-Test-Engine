package einfprog;

import einfprog.test_engine.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class Bsp04Test
{
    private boolean isPalindrome(String s)
    {
        s = s.toUpperCase();
        int n = s.length() - 1;
        int i = 0;
        while(i <= n / 2)
        {
            if(s.charAt(i) != s.charAt(n - i))
                return false;
            i++;
        }
        return true;
    }
    
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
    
    private int wordScore(String word)
    {
        if(word == null)
        {
            return -1;
        }
        
        int sum = 0;
        
        int i = 0;
        String w = word.toUpperCase();
        while(i < w.length())
        {
            if((w.charAt(i) >= 'A') && (w.charAt(i) <= 'Z'))
            {
                sum += charScore(w.charAt(i));
            }
            else
            {
                return -1;
            }
            i++;
        }
        return sum;
    }
    
    private int chainWords(int stopPoints, Compound.Builder output, LinkedList<String> input)
    {
        int score = chainWords2(stopPoints, output, input);
        
        if(score > 0)
        {
            output.add("P1 wins with score ", score);
        }
        else
        {
            output.add("P2 wins with score ", (-score));
        }
        
        return score;
    }
    
    private int chainWords2(int stopPoints, Compound.Builder output, LinkedList<String> input)
    {
        int sum1 = 0;
        int sum2 = 0;
        
        output.add("P1 input: ");
        String w1 = input.removeFirst();
        int l1 = w1.length();
        if(l1 < 2)
            return 0;
        
        sum1 += isPalindrome(w1) ? 2 * wordScore(w1) : wordScore(w1);
        output.add("P1 score is ", sum1);
        while(sum1 < stopPoints)
        {
            
            output.add("P2 input: ");
            String w2 = input.removeFirst();
            if(isChain(w1, w2))
            {
                sum2 += isPalindrome(w2) ? 2 * wordScore(w2) : wordScore(w2);
            }
            else
            {
                break;
            }
            output.add("P2 score is ", sum2);
            
            if(sum2 >= stopPoints)
            {
                break;
            }
            
            output.add("P1 input: ");
            w1 = input.removeFirst();
            if(isChain(w2, w1))
            {
                sum1 += isPalindrome(w1) ? 2 * wordScore(w1) : wordScore(w1);
            }
            else
            {
                break;
            }
            output.add("P1 score is ", sum1);
        }
        return sum1 > sum2 ? sum1 : (-sum2);
    }
    
    private boolean isChain(String s1, String s2)
    {
        int l1 = s1.length();
        int l2 = s2.length();
        return (l1 > 1) && (l2 > 1) && (s1.charAt(l1 - 2) == s2.charAt(0)) && (s1.charAt(l1 - 1) == s2.charAt(1));
    }
    
    @Test
    public void test1()
    {
        // #################################### Test isPalindrome(...) ####################################
        
        String[][] strings = {{"ana", "anna", "tacocat"}, {"ananas", "banana", "Alphabet"}, {"xylophone", "number1"}, {""}};
        
        for(int i = 0; i < 2; i++)
        {
            String[] ss = strings[i];
            
            for(String s : ss)
            {
                MethodInvokeTest<Boolean, Bsp04> t = new MethodInvokeTest<>(
                        Bsp04.class,
                        "isPalindrome",
                        isPalindrome(s),
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
                MethodInvokeTest<Integer, Bsp04> t = new MethodInvokeTest<>(
                        Bsp04.class,
                        "wordScore",
                        wordScore(s),
                        s
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
    }
    
    private void testPart2(int score, String... input)
    {
        LinkedList<String> inputList = new LinkedList<>(Arrays.asList(input));
        
        Compound.Builder output = Compound.builder();
        int ret = chainWords(score, output, inputList);
        
        MethodInvokeTest<Integer, Bsp04> t = new MethodInvokeTest<Integer, Bsp04>(
                Bsp04.class,
                "chainWords",
                ret,
                score
        );
        
        AtomTest t1 = new AtomTest(
                t, Atom.construct((Object[]) input), output.build()
        );
        
        Engine.ENGINE.checkTest(t1);
    }
    
    @Test
    public void test2()
    {
        Engine.ENGINE.requiresDoesNotFail(this::test1, "Part 1 failed.");
        
        testPart2(1000, "alex", "exemption", "onward", "no idea");
        testPart2(1000, "hello", "low", "i don't know");
        testPart2(100, "super", "erklaeren", "engel", "elend", "oidaaaaaa waaaas");
        testPart2(33, "anna", "nase", "sehen", "endlich", "chancen", "entsetzen", "entfernen");
        testPart2(30, "jax", "axiom", "omnipotent", "don't know");
        testPart2(30, "rotator", "orangutan", "anna", "naturally", "lychee");
        testPart2(30, "erroneous", "usage", "genuine", "nexus", "usa", "sagas", "asunder", "eragon");
    }
}
