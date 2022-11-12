package einfprog;

import einfprog.test_engine.Atom;
import einfprog.test_engine.AtomTest;
import einfprog.test_engine.Compound;
import einfprog.test_engine.Engine;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Bsp02Test
{
    @Test
    public void test1() throws IOException
    {
        for(int punkte = 0; punkte <= 120; punkte++)
        {
            String note;
            
            if(punkte < 60)
            {
                note = "Nicht Genügend (5)";
            }
            else if(punkte < 75)
            {
                note = "Genügend (4)";
            }
            else if(punkte < 90)
            {
                note = "Befriedigend (3)";
            }
            else if(punkte < 105)
            {
                note = "Gut (2)";
            }
            else
            {
                note = "Sehr Gut (1)";
            }
            
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
                String note;
                
                if(punkte < 60)
                {
                    note = "Nicht Genügend (5)";
                }
                else if(punkte < 75)
                {
                    note = "Genügend (4)";
                }
                else if(punkte < 90)
                {
                    note = "Befriedigend (3)";
                }
                else if(punkte < 105)
                {
                    note = "Gut (2)";
                }
                else
                {
                    note = "Sehr Gut (1)";
                }
                
                int teilnehmer = noten.length;
                
                int positiv = 0;
                int negativ = 0;
                int ungueltig = 0;
                
                for(int note2 : noten)
                {
                    if(note2 < 1)
                    {
                        ungueltig = ungueltig + 1;
                    }
                    else if(note2 < 5)
                    {
                        positiv = positiv + 1;
                    }
                    else if(note2 == 5)
                    {
                        negativ = negativ + 1;
                    }
                    else
                    {
                        ungueltig = ungueltig + 1;
                    }
                }
                
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
