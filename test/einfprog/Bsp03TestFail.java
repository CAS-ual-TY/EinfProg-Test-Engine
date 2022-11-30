package einfprog;

import einfprog.test_engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Modifier;
import java.util.Random;

public class Bsp03TestFail
{
    // 1: Wrong output of method which might not exist
    // 2: Wrong method signature
    // 3: Wrong method signature
    // 4: Wrong method signature
    // 5: Wrong method return
    public static final int ERRORS = 5;
    
    public void testRun(int error)
    {
        System.out.print("? Maximale Anzahl der Paare: ");
        int nofPairs = SavitchIn.readLineInt();
        System.out.print("? Maximale Anzahl der Maexchen: ");
        int nofMaexchen = SavitchIn.readLineInt();
        
        int countPairs = 0;
        int countMax = 0;
        while(countMax < nofMaexchen && countPairs < nofPairs)
        {
            int d1 = rollDie();
            int d2 = rollDie();
            System.out.print("(" + d1 + "," + d2 + ") ");
            if(isMaexchen(d1, d2))
            {
                countMax++;
                System.out.println("Maexchen #" + countMax);
            }
            else if(isPair(d1, d2))
            {
                countPairs++;
                System.out.println("Paar #" + countPairs);
            }
            else
                System.out.println(getValue(d1, d2));
        }
        
        System.out.println();
        
        //Part 2
        System.out.print("? Anzahl der Wuerfe: ");
        int n = SavitchIn.readLineInt();
        maexchen(n);
    }
    
    public static int rollDie()
    {
        return PRNG.randomInt(6) + 1;
    }
    
    private static int getValue(int d1, int d2)
    {
        if(d1 > d2)
            return d1 * 10 + d2;
        else
            return d2 * 10 + d1;
    }
    
    public static boolean isMaexchen(int d1, int d2)
    {
        return getValue(d1, d2) == 21;
    }
    
    public static boolean isPair(int d1, int d2)
    {
        return d1 == d2;
    }
    
    public static void maexchen(int n)
    {
        int currentN = 1;
        int bestN = 1;
        int bestD1 = 0;
        int bestD2 = 0;
        
        while(currentN <= n && !isMaexchen(bestD1, bestD2))
        {
            int d1 = rollDie();
            int d2 = rollDie();
            
            System.out.print(currentN + ". Wurf: ");
            if(isMaexchen(d1, d2))
            {
                System.out.println("Maexchen");
                bestN = currentN;
            }
            else if(isPair(d1, d2))
            {
                System.out.println(d1 + "er Paar");
                if(!isPair(bestD1, bestD2) || d1 > bestD1)
                    bestN = currentN;
            }
            else
            {
                System.out.println(getValue(d1, d2));
                if(!isPair(bestD1, bestD2) && getValue(d1, d2) > getValue(bestD1, bestD2))
                    bestN = currentN;
            }
            if(bestN == currentN)
            {
                //if (! (getRankedValue(d1, d2) > getRankedValue(bestD1, bestD2))) throw new RuntimeException("Error in implementation");
                bestD1 = d1;
                bestD2 = d2;
            }
            currentN++;
        }
        System.out.println("Wurf " + bestN + " hat gewonnen.");
    }
    
    private int rollDieTest()
    {
        return PRNG.randomInt(6) + 1;
    }
    
    private int groessererWert(int w1, int w2)
    {
        return Math.max(w1, w2) * 10 + Math.min(w1, w2);
    }
    
    private void generatePart1Output(int maxPaare, int maxMaexchen, Compound.Builder output, int error)
    {
        int paare = 0;
        int maexchen = 0;
        while(paare < maxPaare && maexchen < maxMaexchen)
        {
            int w1 = rollDieTest();
            int w2 = rollDieTest();
            int wert = groessererWert(w1, w2);
            
            if(wert == 21)
            {
                output.add("(", w1, ",", w2 + (error == 1 ? 1 : 0), ")", " ", "Maexchen", " ", "#", ++maexchen);
            }
            else if(w1 == w2)
            {
                output.add("(", w1, ",", w2, ")", " ", "Paar", " ", "#", ++paare);
            }
            else
            {
                output.add("(", w1, ",", w2, ")", " ", wert);
            }
        }
    }
    
    private void generateMaexchenOutput(int n, Compound.Builder output)
    {
        int currentN = 1;
        int bestN = 1;
        int bestD1 = 0;
        int bestD2 = 0;
        
        while(currentN <= n && groessererWert(bestD1, bestD2) != 21)
        {
            int d1 = rollDieTest();
            int d2 = rollDieTest();
            
            if(groessererWert(d1, d2) == 21)
            {
                output.add(currentN, ". Wurf: ", "Maexchen");
                bestN = currentN;
            }
            else if(d1 == d2)
            {
                output.add(currentN, ". Wurf: ", d1, "er Paar");
                if(bestD1 != bestD2 || d1 > bestD1)
                    bestN = currentN;
            }
            else
            {
                output.add(currentN, ". Wurf: ", groessererWert(d1, d2));
                if(bestD1 != bestD2 && groessererWert(d1, d2) > groessererWert(bestD1, bestD2))
                    bestN = currentN;
            }
            if(bestN == currentN)
            {
                bestD1 = d1;
                bestD2 = d2;
            }
            currentN++;
        }
        output.add("Wurf ", bestN, " hat gewonnen.");
    }
    
    @Test
    public void testWithError()
    {
        final Random random = new Random(27);
        
        // #################################### Test main(...) ####################################
        for(int error = 0; error <= ERRORS; error++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int maxPaare = random.nextInt(20) + 1;
            int maxMaexchen = random.nextInt(20) + 1;
            int wuerfe = random.nextInt(20) + 1;
            
            Compound.Builder output = Compound.builder();
            
            output.add("? Maximale Anzahl der Paare: ").add("? Maximale Anzahl der Maexchen: ");
            generatePart1Output(maxPaare, maxMaexchen, output, error);
            
            output.add("? Anzahl der Wuerfe: ");
            generateMaexchenOutput(wuerfe, output);
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            final int finalError = error;
            AtomTest t1 = new AtomTest(() -> testRun(finalError),
                    Atom.construct(maxPaare, maxMaexchen, wuerfe),
                    output.build()
            );
            
            MethodTest<Integer, Bsp03TestFail> t2 = new MethodTest<>(
                    Bsp03TestFail.class, error == 3 || error == 4 ? null : this, "rollDieTest", (error == 3 || error == 4 ? Modifier.STATIC : 0) + (error == 2 || error == 4 ? Modifier.PUBLIC : Modifier.PRIVATE), int.class
            );
            
            int w1 = 1;
            int w2 = 2;
            
            MethodInvokeTest<Boolean, Bsp03> t3 = new MethodInvokeTest<>(
                    Bsp03.class,
                    "isMaexchen",
                    error != 5,
                    Util.objArr(w1, w2)
            );
            
            try
            {
                Engine.ENGINE.checkTest(t1);
                Engine.ENGINE.checkTest(t2);
                Engine.ENGINE.checkTest(t3);
                
                if(error > 0)
                {
                    Assertions.fail("This should be unreachable...");
                }
            }
            catch(AssertionFailedError e)
            {
                //e.printStackTrace();
                System.err.print(e.getMessage());
                Util.testSpacer();
            }
        }
    }
}
