package einfprog;

import einfprog.test_engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.Random;

public class Bsp03TestFail
{
    public static final int ERROR = 1;
    
    public void testRun()
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
    
    private void part1Test(int maxPaare, int maxMaexchen, Compound.Builder output)
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
                output.add("(", w1, ",", w2 + (ERROR == 1 ? 1 : 0), ")", " ", "Maexchen", " ", "#", ++maexchen);
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
    
    private void maexchenTest(int n, Compound.Builder builder)
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
                builder.add(currentN, ". Wurf: ", "Maexchen");
                bestN = currentN;
            }
            else if(d1 == d2)
            {
                builder.add(currentN, ". Wurf: ", d1, "er Paar");
                if(bestD1 != bestD2 || d1 > bestD1)
                    bestN = currentN;
            }
            else
            {
                builder.add(currentN, ". Wurf: ", groessererWert(d1, d2));
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
        builder.add("Wurf ", bestN, " hat gewonnen.");
    }
    
    @Test
    public void testWithError()
    {
        final Random random = new Random(27);
        
        // #################################### Test rollDie() ####################################
        for(int test = 0; test < 50; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int result = rollDieTest();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            MethodTest<Integer, Bsp03TestFail> t = new MethodTest<>(
                    Bsp03TestFail.class,
                    "rollDie",
                    result
            );
            
            if(!Engine.ENGINE.checkTest(t))
            {
                return;
            }
        }
        
        // #################################### Test isMaexchen(...) ####################################
        for(int w1 = 1; w1 <= 6; w1++)
        {
            for(int w2 = 1; w2 <= 6; w2++)
            {
                MethodTest<Boolean, Bsp03> t1 = new MethodTest<>(
                        Bsp03.class,
                        "isMaexchen",
                        groessererWert(w1, w2) == 21,
                        w1, w2
                );
                
                MethodTest<Boolean, Bsp03> t2 = new MethodTest<>(
                        Bsp03.class,
                        "isPair",
                        w1 == w2,
                        w1, w2
                );
                
                if(!Engine.ENGINE.checkTest(t1) || !Engine.ENGINE.checkTest(t2))
                {
                    return;
                }
            }
        }
        
        // #################################### Test maexchen(...) ####################################
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int wuerfe = random.nextInt(20);
            
            Compound.Builder output = Compound.builder();
            maexchenTest(wuerfe, output);
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            MethodTest<Void, Bsp03> t1 = new MethodTest<Void, Bsp03>(
                    Bsp03.class,
                    "maexchen",
                    null,
                    wuerfe
            );
            
            AtomTest t2 = new AtomTest(t1,
                    Atom.construct(),
                    output.build()
            );
            
            if(!Engine.ENGINE.checkTest(t2))
            {
                return;
            }
        }
        
        // #################################### Test main(...) ####################################
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int maxPaare = random.nextInt(20);
            int maxMaexchen = random.nextInt(20);
            int wuerfe = random.nextInt(20);
            
            Compound.Builder output = Compound.builder();
            
            output.add("? Maximale Anzahl der Paare: ").add("? Maximale Anzahl der Maexchen: ");
            part1Test(maxPaare, maxMaexchen, output);
            
            output.add("? Anzahl der Wuerfe: ");
            maexchenTest(wuerfe, output);
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            AtomTest t = new AtomTest(() -> Bsp03.main(new String[] {}),
                    Atom.construct(maxPaare, maxMaexchen, wuerfe),
                    output.build()
            );
            
            try
            {
                Engine.ENGINE.checkTest(t);
                
                if(ERROR != 0)
                {
                    Assertions.fail("This should be unreachable...");
                }
            }
            catch(AssertionFailedError e)
            {
                e.printStackTrace();
                return;
            }
        }
    }
}
