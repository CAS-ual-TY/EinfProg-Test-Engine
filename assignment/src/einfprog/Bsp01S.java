package einfprog;

public class Bsp01S
{
    public static final boolean DO_2ND_ASSIGNMENT = true;
    public static final boolean NEWLINES = true;
    
    public static void main(String[] args)
    {
        System.out.print("? Verbrauch 100km[l]: ");
        double verbrauchPro100km = SavitchIn.readLineDouble();
        if(NEWLINES) System.out.println();
        
        System.out.print("? Dieselpreis pro Liter[Euro]: ");
        double dieselpreisPro1L = SavitchIn.readLineDouble();
        if(NEWLINES) System.out.println();
        
        double kostenPro100km = verbrauchPro100km * dieselpreisPro1L;
        System.out.println("Kosten pro 100km[Euro] = " + kostenPro100km);
        
        if(!DO_2ND_ASSIGNMENT)
        {
            return;
        }
        
        if(NEWLINES)
        {
            // carriage return and tabs filtered out
            System.out.println("\r\n".repeat(5));
        }
        
        if(NEWLINES)
        {
            // empty lines in between, even with spaces
            System.out.println("\t \t ".repeat(5));
            System.out.println();
            System.out.println();
            System.out.println("  ");
        }
        
        System.out.print("? Verbrauch 100km[kWh]: ");
        double stromverbrauchPro100km = SavitchIn.readLineDouble();
        if(NEWLINES) System.out.println();
        
        System.out.print("? Strompreis pro kWh[Euro]: ");
        double strompreisPro1kWh = SavitchIn.readLineDouble();
        if(NEWLINES) System.out.println();
        
        double stromkostenPro100km = stromverbrauchPro100km * strompreisPro1kWh;
        System.out.println("Kosten pro 100km[Euro] = " + stromkostenPro100km);
        
        double verhaeltnis = stromkostenPro100km / kostenPro100km;
        System.out.println("Verhältnis S/D = " + verhaeltnis);
    }
}
