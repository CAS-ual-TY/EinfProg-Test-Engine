package einfprog;

public class Bsp02
{
    public static void main(String[] args)
    {
        System.out.print("? Erreichte Punkte [0-120]: ");
        int erreichtePunkte = SavitchIn.readLineInt();
        
        if(erreichtePunkte < 60)
        {
            System.out.println("Nicht Genügend (5)");
        }
        else if(erreichtePunkte < 75)
        {
            System.out.println("Genügend (4)");
        }
        else if(erreichtePunkte < 90)
        {
            System.out.println("Befriedigend (3)");
        }
        else if(erreichtePunkte < 105)
        {
            System.out.println("Gut (2)");
        }
        else
        {
            System.out.println("Sehr Gut (1)");
        }
        
        System.out.print("? Anzahl der Teilnehmer: ");
        int teilnehmer = SavitchIn.readLineInt();
        
        int positiv = 0;
        int negativ = 0;
        int ungueltig = 0;
        
        int i = 0;
        while(i < teilnehmer)
        {
            System.out.print("? Note [1-5]: ");
            int note = SavitchIn.readLineInt();
            
            if(note < 1)
            {
                ungueltig = ungueltig + 1;
            }
            else if(note < 5)
            {
                positiv = positiv + 1;
            }
            else if(note == 5)
            {
                negativ = negativ + 1;
            }
            else
            {
                ungueltig = ungueltig + 1;
            }
            
            i++;
        }
        
        System.out.println(positiv + "/" + teilnehmer + " Teilnehmer haben bestanden.");
        System.out.println(negativ + "/" + teilnehmer + " Teilnehmer haben nicht bestanden.");
        System.out.println(ungueltig + "/" + teilnehmer + " Teilnehmer haben eine ungültige Beurteilung.");
    }
}
