package einfprog;

import einfprog.Bsp01S;
import einfprog.test_engine.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Bsp01Test
{
    @Test
    public void test1() throws IOException
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
                        new Atom[] { // input to send to the program
                                Atom.doubleAtom(verbrauchPro100km),
                                Atom.doubleAtom(dieselpreisPro1L)
                        }, // output to check for
                        Compound.start("? Verbrauch 100km[l]: "),
                        Compound.start("? Dieselpreis pro Liter[Euro]: "),
                        Compound.start("Kosten pro 100km[Euro] = ").doubleAtom(kostenPro100km)
                );
                
                Engine.ENGINE.checkTest(t);
            }
        }
    }
    
    @Test
    public void test2() throws IOException
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
                                new Atom[] { // input to send to the program
                                        Atom.doubleAtom(verbrauchPro100km),
                                        Atom.doubleAtom(dieselpreisPro1L),
                                        Atom.doubleAtom(stromverbrauchPro100km),
                                        Atom.doubleAtom(strompreisPro1kWh)
                                }, // output to check for
                                Compound.start("? Verbrauch 100km[l]: "),
                                Compound.start("? Dieselpreis pro Liter[Euro]: "),
                                Compound.start("Kosten pro 100km[Euro] = ").doubleAtom(kostenPro100km),
                                Compound.start("? Verbrauch 100km[kWh]: "),
                                Compound.start("? Strompreis pro kWh[Euro]: "),
                                Compound.start("Kosten pro 100km[Euro] = ").doubleAtom(stromkostenPro100km),
                                Compound.start("Verhältnis S/D = ").doubleAtom(verhaeltnis)
                        );
                        
                        Engine.ENGINE.checkTest(t);
                    }
                }
            }
        }
    }
}