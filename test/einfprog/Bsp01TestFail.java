package einfprog;

import einfprog.test_engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class Bsp01TestFail
{
    // 1: mismatched string
    // 2: value slightly off
    // 3: missing entire output
    // 4: throw exception
    public static final int ERROR = 1;
    
    private void testRun()
    {
        System.out.print("? Verbrauch 100km[l]: ");
        double verbrauchPro100km = SavitchIn.readLineDouble();
        System.out.println();
        
        if(ERROR == 2)
        {
            verbrauchPro100km += Settings.DEFAULT_DOUBLE_ERROR * 1.0001;
        }
        
        System.out.print("? Dieselpreis pro " + (ERROR == 1 ? "t" : "") + "Liter[Euro]: ");
        double dieselpreisPro1L = SavitchIn.readLineDouble();
        System.out.println();
        
        double kostenPro100km = verbrauchPro100km * dieselpreisPro1L;
        System.out.println("Kosten pro 100km[Euro] = " + kostenPro100km);
        
        System.out.print("? Verbrauch 100km[kWh]: ");
        double stromverbrauchPro100km = SavitchIn.readLineDouble();
        System.out.println();
        
        if(ERROR == 4)
        {
            throw new RuntimeException("ERROR == " + ERROR);
        }
        
        System.out.print("? Strompreis pro kWh[Euro]: ");
        double strompreisPro1kWh = SavitchIn.readLineDouble();
        System.out.println();
        
        double stromkostenPro100km = stromverbrauchPro100km * strompreisPro1kWh;
        System.out.println("Kosten pro 100km[Euro] = " + stromkostenPro100km);
        
        if(ERROR != 3)
        {
            double verhaeltnis = stromkostenPro100km / kostenPro100km;
            System.out.println("Verhältnis S/D = " + verhaeltnis);
        }
    }
    
    @Test
    public void testWithError()
    {
        double verbrauchPro100km = 2D;
        double dieselpreisPro1L = 3D;
        double kostenPro100km = verbrauchPro100km * dieselpreisPro1L;
        
        double stromverbrauchPro100km = 4D;
        double strompreisPro1kWh = 5D;
        double stromkostenPro100km = stromverbrauchPro100km * strompreisPro1kWh;
        
        double verhaeltnis = stromkostenPro100km / kostenPro100km;
        
        AtomTest t = new AtomTest(this::testRun,
                new Atom[] {
                        Atom.doubleAtom(verbrauchPro100km),
                        Atom.doubleAtom(dieselpreisPro1L),
                        Atom.doubleAtom(stromverbrauchPro100km),
                        Atom.doubleAtom(strompreisPro1kWh)
                },
                Compound.start("? Verbrauch 100km[l]: "),
                Compound.start("? Dieselpreis pro Liter[Euro]: "),
                Compound.start("Kosten pro 100km[Euro] = ").doubleAtom(kostenPro100km),
                Compound.start("? Verbrauch 100km[kWh]: "),
                Compound.start("? Strompreis pro kWh[Euro]: "),
                Compound.start("Kosten pro 100km[Euro] = ").doubleAtom(stromkostenPro100km),
                Compound.start("Verhältnis S/D = ").doubleAtom(verhaeltnis)
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
        }
    }
}
