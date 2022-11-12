package einfprog;

public class Bsp01
{
    public static void main(String[] args)
    {
        System.out.print("? Verbrauch 100km[l]: ");
        double verbrauchPro100km = SavitchIn.readLineDouble();
        System.out.println();
        
        System.out.print("? Dieselpreis pro Liter[Euro]: ");
        double dieselpreisPro1L = SavitchIn.readLineDouble();
        System.out.println();
        
        double kostenPro100km = verbrauchPro100km * dieselpreisPro1L;
        System.out.println("Kosten pro 100km[Euro] = " + kostenPro100km);
        
        System.out.print("? Verbrauch 100km[kWh]: ");
        double stromverbrauchPro100km = SavitchIn.readLineDouble();
        System.out.println();
        
        System.out.print("? Strompreis pro kWh[Euro]: ");
        double strompreisPro1kWh = SavitchIn.readLineDouble();
        System.out.println();
        
        double stromkostenPro100km = stromverbrauchPro100km * strompreisPro1kWh;
        System.out.println("Kosten pro 100km[Euro] = " + stromkostenPro100km);
        
        double verhaeltnis = stromkostenPro100km / kostenPro100km;
        System.out.println("Verhältnis S/D = " + verhaeltnis);
    }
}
