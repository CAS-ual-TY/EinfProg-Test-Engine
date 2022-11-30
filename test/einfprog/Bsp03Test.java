package einfprog;

import einfprog.test_engine.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Bsp03Test
{
    private int rollDieTest()
    {
        return PRNG.randomInt(6) + 1;
    }
    
    private int groessererWert(int w1, int w2)
    {
        return Math.max(w1, w2) * 10 + Math.min(w1, w2);
    }
    
    private void generatePart1Output(int maxPaare, int maxMaexchen, Compound.Builder output)
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
                output.add("(", w1, ",", w2, ")", " ", "Maexchen", " ", "#", ++maexchen);
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
                //if (! (getRankedValue(d1, d2) > getRankedValue(bestD1, bestD2))) throw new RuntimeException("Error in implementation");
                bestD1 = d1;
                bestD2 = d2;
            }
            currentN++;
        }
        output.add("Wurf ", bestN, " hat gewonnen.");
    }
    
    @Test
    public void test1()
    {
        final Random random = new Random(27);
        
        // #################################### Test part1 main(...) ####################################
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int maxPaare = random.nextInt(20) + 1;
            int maxMaexchen = random.nextInt(20) + 1;
            
            Compound.Builder output = Compound.builder();
            
            output.add("? Maximale Anzahl der Paare: ").add("? Maximale Anzahl der Maexchen: ");
            generatePart1Output(maxPaare, maxMaexchen, output);
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            AtomTest t = new AtomTest(() -> Bsp03.main(new String[] {}),
                    Atom.construct(maxPaare, maxMaexchen),
                    output.build()
            );
            
            Engine.ENGINE.checkTest(t);
        }
        
        // #################################### Test rollDie() ####################################
        for(int test = 0; test < 50; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int result = rollDieTest();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            MethodInvokeTest<Integer, Bsp03> t = new MethodInvokeTest<>(
                    Bsp03.class,
                    "rollDie",
                    result
            );
            
            Engine.ENGINE.checkTest(t);
        }
        
        // #################################### Test isMaexchen(...) & isPair(...) ####################################
        for(int w1 = 1; w1 <= 6; w1++)
        {
            for(int w2 = 1; w2 <= 6; w2++)
            {
                MethodInvokeTest<Boolean, Bsp03> t1 = new MethodInvokeTest<>(
                        Bsp03.class,
                        "isMaexchen",
                        groessererWert(w1, w2) == 21,
                        Util.objArr(w1, w2)
                );
                
                MethodInvokeTest<Boolean, Bsp03> t2 = new MethodInvokeTest<>(
                        Bsp03.class,
                        "isPair",
                        w1 == w2,
                        Util.objArr(w1, w2)
                );
                
                Engine.ENGINE.checkTest(t1);
                Engine.ENGINE.checkTest(t2);
            }
        }
    }
    
    @Test
    public void test2()
    {
        final Random random = new Random(2727);
        
        // #################################### Test maexchen(...) ####################################
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int wuerfe = random.nextInt(20) + 1;
            
            Compound.Builder output = Compound.builder();
            generateMaexchenOutput(wuerfe, output);
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            MethodInvokeTest<Void, Bsp03> t1 = new MethodInvokeTest<>(
                    Bsp03.class,
                    "maexchen",
                    null,
                    Util.objArr(wuerfe)
            );
            
            AtomTest t2 = new AtomTest(t1, output.build());
            
            if(!Engine.ENGINE.checkTest(t2))
            {
                return;
            }
        }
        
        // #################################### Test part2 main(...) ####################################
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            int maxPaare = random.nextInt(20) + 1;
            int maxMaexchen = random.nextInt(20) + 1;
            int wuerfe = random.nextInt(20) + 1;
            
            Compound.Builder output = Compound.builder();
            
            output.add("? Maximale Anzahl der Paare: ").add("? Maximale Anzahl der Maexchen: ");
            generatePart1Output(maxPaare, maxMaexchen, output);
            
            output.add("? Anzahl der Wuerfe: ");
            generateMaexchenOutput(wuerfe, output);
            
            // RESET PRNG
            PRNG.randomize(seed);
            
            AtomTest t = new AtomTest(() -> Bsp03.main(new String[] {}),
                    Atom.construct(maxPaare, maxMaexchen, wuerfe),
                    output.build()
            );
            
            if(!Engine.ENGINE.checkTest(t))
            {
                return;
            }
        }
    }
}
