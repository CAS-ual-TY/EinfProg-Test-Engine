package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.RegexTest;
import einfprog.test_engine.Util;
import org.junit.jupiter.api.Test;

public class Bsp01TestRegex
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
                
                RegexTest t = new RegexTest(() -> Bsp01.main(new String[] {}),
                        // regex
                        ("" +
                                "\\s*\\??\\s*Verbrauch\\s*100km\\[l]:\\s*" +
                                "\\s*\\??\\s*Dieselpreis\\s*pro\\s*Liter\\[Euro]:\\s*" +
                                "\\s*Kosten\\s*pro\\s*100km\\[Euro]\\s*=\\s*(?<kostenPro100km>%s)\\s*"
                        ).formatted(Util.DOUBLE_REGEX),
                        
                        // expected
                        () -> """
                                ? Verbrauch 100km[l]:
                                ? Dieselpreis pro Liter[Euro]:
                                Kosten pro 100km[Euro] = %f""".formatted(kostenPro100km),
                        
                        // value tests
                        (pattern, matcher) -> Util.doubleEquals(matcher, "kostenPro100km", kostenPro100km),
                        
                        // input
                        String.valueOf(verbrauchPro100km), String.valueOf(dieselpreisPro1L)
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
                        
                        RegexTest t = new RegexTest(() -> Bsp01.main(new String[] {}),
                                // regex
                                ("" +
                                        "\\s*\\??\\s+Verbrauch\\s+100km\\[l]:\\s*" +
                                        "\\s*\\??\\s+Dieselpreis\\s+pro\\s+Liter\\[Euro]:\\s*" +
                                        "\\s*Kosten\\s+pro\\s+100km\\[Euro]\\s+=\\s+(?<kostenPro100km>%s)\\s*" +
                                        "\\s*\\??\\s+Verbrauch\\s+100km\\[kWh]:\\s*" +
                                        "\\s*\\??\\s+Strompreis\\s+pro\\s+kWh\\[Euro]:\\s*" +
                                        "\\s*Kosten\\s+pro\\s+100km\\[Euro]\\s+=\\s+(?<stromkostenPro100km>%s)\\s*" +
                                        "\\s*Verhältnis\\s+S/D\\s+=\\s+(?<verhaeltnis>%s)\\s*"
                                ).formatted(Util.DOUBLE_REGEX, Util.DOUBLE_REGEX, Util.DOUBLE_REGEX),
                                
                                // expected
                                () -> """
                                        ? Verbrauch 100km[l]:
                                        ? Dieselpreis pro Liter[Euro]:
                                        Kosten pro 100km[Euro] = %f
                                        ? Verbrauch 100km[kWh]:
                                        ? Strompreis pro kWh[Euro]:
                                        Kosten pro 100km[Euro] = %f
                                        Verhältnis S/D = %f""".formatted(kostenPro100km, stromkostenPro100km, verhaeltnis),
                                
                                // value tests
                                (pattern, matcher) ->
                                        (
                                                Util.doubleEquals(matcher, "kostenPro100km", kostenPro100km) &&
                                                        Util.doubleEquals(matcher, "stromkostenPro100km", stromkostenPro100km) &&
                                                        Util.doubleEquals(matcher, "verhaeltnis", verhaeltnis)
                                        ),
                                
                                // input
                                String.valueOf(verbrauchPro100km), String.valueOf(dieselpreisPro1L),
                                String.valueOf(stromverbrauchPro100km), String.valueOf(strompreisPro1kWh)
                        );
                        
                        Engine.ENGINE.checkTest(t);
                    }
                }
            }
        }
    }
}
