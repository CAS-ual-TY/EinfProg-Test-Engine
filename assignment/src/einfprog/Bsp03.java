package einfprog;

public class Bsp03
{
    public static void main(String[] args)
    {
        System.out.print("? Maximale Anzahl der Paare: ");
        int nofPairs = SavitchIn.readLineInt();
        System.out.print("? Maximale Anzahl der Maexchen: ");
        int nofMaexchen = SavitchIn.readLineInt();
        
        int countPairs = 0;
        int countMax = 0;
        while(countMax < nofMaexchen && countPairs < nofPairs)
        {
            int d1 = rollDie();
            int d2 = rollDie();
            System.out.print("(" + d1 + "," + d2 + ") ");
            if(isMaexchen(d1, d2))
            {
                countMax++;
                System.out.println("Maexchen #" + countMax);
            }
            else if(isPair(d1, d2))
            {
                countPairs++;
                System.out.println("Paar #" + countPairs);
            }
            else
                System.out.println(getValue(d1, d2));
        }
        
        System.out.println();
        
        //Part 2
        System.out.print("? Anzahl der Wuerfe: ");
        int n = SavitchIn.readLineInt();
        maexchen(n);
    }
    
    public static int rollDie()
    {
        return PRNG.randomInt(6) + 1;
    }
    
    private static int getValue(int d1, int d2)
    {
        if(d1 > d2)
            return d1 * 10 + d2;
        else
            return d2 * 10 + d1;
    }
    
    public static boolean isMaexchen(int d1, int d2)
    {
        return getValue(d1, d2) == 21;
    }
    
    public static boolean isPair(int d1, int d2)
    {
        return d1 == d2;
    }
    
    ////////////// Part 2
    
    public static void maexchen(int n)
    {
        int currentN = 1;
        int bestN = 1;
        int bestD1 = 0;
        int bestD2 = 0;
        
        while(currentN <= n && !isMaexchen(bestD1, bestD2))
        {
            int d1 = rollDie();
            int d2 = rollDie();
            
            System.out.print(currentN + ". Wurf: ");
            if(isMaexchen(d1, d2))
            {
                System.out.println("Maexchen");
                bestN = currentN;
            }
            else if(isPair(d1, d2))
            {
                System.out.println(d1 + "er Paar");
                if(!isPair(bestD1, bestD2) || d1 > bestD1)
                    bestN = currentN;
            }
            else
            {
                System.out.println(getValue(d1, d2));
                if(!isPair(bestD1, bestD2) && getValue(d1, d2) > getValue(bestD1, bestD2))
                    bestN = currentN;
            }
            if(bestN == currentN)
            {
                //if (! (getRankedValue(d1, d2) > getRankedValue(bestD1, bestD2))) throw new RuntimeException("Error in implementation");
                bestD1 = d1;
                bestD2 = d2;
            }
            currentN++;
        }
        System.out.println("Wurf " + bestN + " hat gewonnen.");
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////
    
    //Alternative implementation for determine best throw. Used for testing
    private static int getRankedValue(int d1, int d2)
    {
        return ((isMaexchen(d1, d2) ? 1 : 0) * 1000) + ((isPair(d1, d2) ? 1 : 0) * d1 * 100) + getValue(d1, d2);
    }
}
