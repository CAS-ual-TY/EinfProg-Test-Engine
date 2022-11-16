package einfprog;

import einfprog.test_engine.Atom;
import einfprog.test_engine.AtomTest;
import einfprog.test_engine.Compound;
import einfprog.test_engine.Engine;
import org.junit.jupiter.api.Test;

public class Bsp01TestQuick
{
    @Test
    public void test1()
    {
        double[][] input = new double[][] {
                {3D, 7D, 12.5D, 22D, 30.4D},
                {5D, 3.1D, 2.7D, 50D}
        };
        
        for(double verbrauchPro100km : input[0])
        {
            for(double dieselpreisPro1L : input[1])
            {
                double kostenPro100km = verbrauchPro100km * dieselpreisPro1L;
                
                AtomTest t = new AtomTest(() -> Bsp01S.main(new String[] {}),
                        Atom.construct(verbrauchPro100km, dieselpreisPro1L), // input
                        Compound.construct("? Verbrauch 100km[l]: "), // output
                        Compound.construct("? Dieselpreis pro Liter[Euro]: "),
                        Compound.construct("Kosten pro 100km[Euro] = ", kostenPro100km)
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
    }
    
    @Test
    public void test2()
    {
        double[][] input = new double[][] {
                {3D, 7D, 12.5D, 22D, 30.4D},
                {5D, 3.1D, 2.7D, 50D},
                {2D, 4.5D, 11D, 19D, 22.8D},
                {6D, 3.6D, 3.75D, 40D},
        };
        
        for(double verbrauchPro100km : input[0])
        {
            for(double dieselpreisPro1L : input[1])
            {
                for(double stromverbrauchPro100km : input[2])
                {
                    for(double strompreisPro1kWh : input[3])
                    {
                        double kostenPro100km = verbrauchPro100km * dieselpreisPro1L;
                        double stromkostenPro100km = stromverbrauchPro100km * strompreisPro1kWh;
                        double verhaeltnis = stromkostenPro100km / kostenPro100km;
                        
                        AtomTest t = new AtomTest(() -> Bsp01S.main(new String[] {}),
                                Atom.construct(verbrauchPro100km, dieselpreisPro1L, stromverbrauchPro100km, strompreisPro1kWh), // input
                                Compound.construct("? Verbrauch 100km[l]: "), // output
                                Compound.construct("? Dieselpreis pro Liter[Euro]: "),
                                Compound.construct("Kosten pro 100km[Euro] = ", kostenPro100km),
                                Compound.construct("? Verbrauch 100km[kWh]: "),
                                Compound.construct("? Strompreis pro kWh[Euro]: "),
                                Compound.construct("Kosten pro 100km[Euro] = ", stromkostenPro100km),
                                Compound.construct("Verhältnis S/D = ", verhaeltnis)
                        );
                        
                        Engine.ENGINE.checkTest(t);
                    }
                }
            }
        }
    }
}
