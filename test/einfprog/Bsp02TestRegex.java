package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.RegexTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Bsp02TestRegex
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
            
            String expected = "? Erreichte Punkte [0-120]: ";
            
            RegexTest t = new RegexTest(() -> Bsp02.main(new String[] {}),
                    "\\s*\\?? Erreichte Punkte\\s*\\[0[-–—]120]\\s*:\\s*%s\\s*"
                            .formatted(note
                                    .replace(" ", "\\s*")
                                    .replace("(", "\\(")
                                    .replace(")", "\\)")
                            ),
                    expected,
                    String.valueOf(punkte)
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
                
                int positiv = (int) Arrays.stream(noten).filter(n -> n > 0 && n < 5).count();
                int negativ = (int) Arrays.stream(noten).filter(n -> n == 5).count();
                int ungueltig = noten.length - positiv - negativ;
                
                String expected = ("""
                        ? Erreichte Punkte:
                        %s
                        ? Anzahl der Teilnehmer:
                        %s
                        %d/%d Teilnehmer haben bestanden.
                        %d/%d Teilnehmer haben nicht bestanden.
                        %d/%d Teilnehmer haben eine ungültige Bewertung."""
                ).formatted(
                        note,
                        "\n? Note [1-5]: ".repeat(teilnehmer).substring(1),
                        positiv, teilnehmer,
                        negativ, teilnehmer,
                        ungueltig, teilnehmer
                );
                
                RegexTest t = new RegexTest(() -> Bsp02.main(new String[] {}),
                        (
                                "\\s*\\?? Erreichte Punkte\\s*\\[0[-–—]120]\\s*:\\s*%s\\s*" +
                                        "\\s*\\??\\s*Anzahl\\s*der\\s*Teilnehmer:\\s*" +
                                        "\\s*\\??\\s*Note\\s*\\[1[-–—]5]\\s*:\\s*".repeat(teilnehmer) +
                                        "\\s*%d/%d\\s*Teilnehmer\\s*haben\\s*bestanden.\\s*" +
                                        "\\s*%d/%d\\s*Teilnehmer\\s*haben\\s*nicht\\s*bestanden.\\s*" +
                                        "\\s*%d/%d\\s*Teilnehmer\\s*haben\\s*eine\\s*ungültige\\s*Beurteilung.\\s*"
                        ).formatted(
                                note
                                        .replace(" ", "\\s*")
                                        .replace("(", "\\(")
                                        .replace(")", "\\)"),
                                positiv, teilnehmer,
                                negativ, teilnehmer,
                                ungueltig, teilnehmer
                        ),
                        expected,
                        ("" +
                                punkte + ";" +
                                teilnehmer + ";" +
                                Arrays.stream(noten).mapToObj(String::valueOf).collect(Collectors.joining(";"))
                        ).split(";")
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
    }
}
