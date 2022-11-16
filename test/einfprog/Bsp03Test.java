package einfprog;

import einfprog.test_engine.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

public class Bsp03Test
{
    private int rollDie()
    {
        return PRNG.randomInt(6) + 1;
    }
    
    private int groessererWert(int w1, int w2)
    {
        return Math.max(w1, w2) * 10 + Math.min(w1, w2);
    }
    
    @Test
    public void test1() throws IOException
    {
        final Random random = new Random(27);
        
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG #####################################################################
            PRNG.randomize(seed);
            
            int maxPaare = random.nextInt(20);
            int maxMaexchen = random.nextInt(20);
            
            Compound.Builder output = Compound.builder();
            output.add("? Maximale Anzahl der Paare: ").add("? Maximale Anzahl der Maexchen: ");
            
            int paare = 0;
            int maexchen = 0;
            while(paare < maxPaare && maexchen < maxMaexchen)
            {
                int w1 = rollDie();
                int w2 = rollDie();
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
            
            // RESET PRNG #####################################################################
            PRNG.randomize(seed);
            
            AtomTest t = new AtomTest(() -> Bsp03.main(new String[] {}),
                    Atom.construct(maxPaare, maxMaexchen),
                    output.build()
            );
            
            if(!Engine.ENGINE.checkTest(t))
            {
                return;
            }
        }
        
        for(int test = 0; test < 50; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG #####################################################################
            PRNG.randomize(seed);
            
            int result = rollDie();
            
            // RESET PRNG #####################################################################
            PRNG.randomize(seed);
            
            MethodTest<Integer, Bsp03> t = new MethodTest<>(
                    Bsp03.class,
                    "rollDie",
                    result
            );
            
            if(!Engine.ENGINE.checkTest(t))
            {
                return;
            }
        }
        
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
    }
    
    private void maexchen(int n, Compound.Builder builder)
    {
        int currentN = 1;
        int bestN = 1;
        int bestD1 = 0;
        int bestD2 = 0;
        
        while(currentN <= n && groessererWert(bestD1, bestD2) != 21)
        {
            int d1 = rollDie();
            int d2 = rollDie();
            
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
                //if (! (getRankedValue(d1, d2) > getRankedValue(bestD1, bestD2))) throw new RuntimeException("Error in implementation");
                bestD1 = d1;
                bestD2 = d2;
            }
            currentN++;
        }
        builder.add("Wurf ", bestN, " hat gewonnen.");
    }
    
    @Test
    public void test2() throws IOException
    {
        final Random random = new Random(27);
        
        for(int test = 0; test < 10; test++)
        {
            long seed = random.nextLong();
            
            // RESET PRNG #####################################################################
            PRNG.randomize(seed);
            
            int wuerfe = random.nextInt(20);
            Compound.Builder builder = Compound.builder();
            maexchen(wuerfe, builder);
            
            // RESET PRNG #####################################################################
            PRNG.randomize(seed);
            
            MethodTest<Void, Bsp03> t1 = new MethodTest<Void, Bsp03>(
                    Bsp03.class,
                    "maexchen",
                    null,
                    wuerfe
            );
            
            AtomTest t2 = new AtomTest(() -> Engine.ENGINE.checkTest(t1),
                    Atom.construct(),
                    builder.build()
            );
            
            if(!Engine.ENGINE.checkTest(t2))
            {
                return;
            }
        }
    }
}
