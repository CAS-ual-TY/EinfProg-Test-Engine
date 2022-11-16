package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.RegexTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Bsp02TestRegex
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
    public void test1()
    {
        for(int punkte = 0; punkte <= 120; punkte++)
        {
            String note = note(punkte);
            String noteRegex = note
                    .replace(" ", "\\s*")
                    .replace("(", "\\(")
                    .replace(")", "\\)");
            
            RegexTest t = new RegexTest(() -> Bsp02.main(new String[] {}),
                    "\\s*\\?? Erreichte Punkte\\s*\\[0[-–—]120]\\s*:\\s*" + noteRegex + "\\s*",
                    () -> "? Erreichte Punkte [0-120]: ",
                    String.valueOf(punkte)
            );
            
            Engine.ENGINE.checkTest(t);
        }
    }
    
    @Test
    public void test2()
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
                String noteRegex = note
                        .replace(" ", "\\s*")
                        .replace("(", "\\(")
                        .replace(")", "\\)");
                
                int teilnehmer = noten.length;
                
                int positiv = (int) Arrays.stream(noten).filter(n -> n > 0 && n < 5).count();
                int negativ = (int) Arrays.stream(noten).filter(n -> n == 5).count();
                int ungueltig = noten.length - positiv - negativ;
                
                RegexTest t = new RegexTest(() -> Bsp02.main(new String[] {}),
                        
                        (
                                "\\s*\\?? Erreichte Punkte\\s*\\[0[-–—]120]\\s*:\\s*%s\\s*" +
                                        "\\s*\\??\\s*Anzahl\\s*der\\s*Teilnehmer:\\s*" +
                                        "\\s*\\??\\s*Note\\s*\\[1[-–—]5]\\s*:\\s*".repeat(teilnehmer) +
                                        "\\s*%d/%d\\s*Teilnehmer\\s*haben\\s*bestanden.\\s*" +
                                        "\\s*%d/%d\\s*Teilnehmer\\s*haben\\s*nicht\\s*bestanden.\\s*" +
                                        "\\s*%d/%d\\s*Teilnehmer\\s*haben\\s*eine\\s*ungültige\\s*Beurteilung.\\s*"
                        ).formatted(
                                noteRegex,
                                positiv, teilnehmer,
                                negativ, teilnehmer,
                                ungueltig, teilnehmer
                        ),
                        
                        () -> (
                                """
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
                        ),
                        
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
