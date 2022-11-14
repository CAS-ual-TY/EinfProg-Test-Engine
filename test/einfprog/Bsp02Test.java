package einfprog;

import einfprog.test_engine.Atom;
import einfprog.test_engine.AtomTest;
import einfprog.test_engine.Compound;
import einfprog.test_engine.Engine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class Bsp02Test
{
    private String note(int punkte)
    {
        if(punkte < 60)
        {
            return "Nicht Genügend (5)";
        }
        if(punkte < 75)
        {
            return "Genügend (4)";
        }
        if(punkte < 90)
        {
            return "Befriedigend (3)";
        }
        if(punkte < 105)
        {
            return "Gut (2)";
        }
        
        return "Sehr Gut (1)";
    }
    
    @Test
    public void test1() throws IOException
    {
        for(int punkte = 0; punkte <= 120; punkte++)
        {
            String note = note(punkte);
            
            AtomTest t = new AtomTest(() -> Bsp02.main(new String[] {}),
                    Atom.construct(punkte),
                    Compound.construct("? Erreichte Punkte [0-120]: "),
                    Compound.construct(note)
            );
            
            Engine.ENGINE.checkTest(t);
        }
    }
    
    @Test
    public void test2() throws IOException
    {
        int[][] notenInput = new int[][] {
                {1, 2, 3, 4, 5},
                {0, 1, 2, 3, 4, 5, 6},
                {1, 2, 3, 4},
                {10},
                {1, 2, 3, 2, 1},
                {1, 1, 1, 10, 100, 1000}
        };
        
        for(int[] noten : notenInput)
        {
            for(int punkte = 0; punkte <= 120; punkte += 5)
            {
                String note = note(punkte);
                
                int teilnehmer = noten.length;
                
                int positiv = (int) Arrays.stream(noten).filter(n -> n > 0 && n < 5).count();
                int negativ = (int) Arrays.stream(noten).filter(n -> n == 5).count();
                int ungueltig = noten.length - positiv - negativ;
                
                AtomTest t = new AtomTest(() -> Bsp02.main(new String[] {}),
                        Atom.builder()
                                .add(punkte)
                                .add(teilnehmer)
                                .add(noten.length, i -> noten[i])
                                .build(),
                        Compound.builder()
                                .add("? Erreichte Punkte [0-120]: ")
                                .add(note)
                                .add("? Anzahl der Teilnehmer: ")
                                .add("? Note [1-5]: ").repeat(noten.length)
                                .add(positiv + "/" + teilnehmer, " Teilnehmer haben bestanden.")
                                .add(negativ + "/" + teilnehmer, " Teilnehmer haben nicht bestanden.")
                                .add(ungueltig + "/" + teilnehmer, " Teilnehmer haben eine ungültige Beurteilung.")
                                .build()
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
    }
}
