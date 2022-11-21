package einfprog;

public class Bsp04
{
    public static void main(String[] args)
    {
        System.out.println("test1: " + isPalindrome("test1"));
        System.out.println("madam: " + isPalindrome("madam"));
        
        System.out.println(wordScore("Jinx"));
        System.out.println(wordScore("Why"));
        System.out.println(wordScore("Zenia"));
        System.out.println(wordScore("Zen a"));
        
        int i = chainWords(100);
        if(i > 0)
        {
            System.out.println("P1 wins with score " + i);
        }
        else
        {
            System.out.println("P2 wins with score " + (0 - i));
        }
    }
    
    // Teil 1
    
    /**
     * Checks if string is a palindrome
     *
     * @param s string to check
     * @return true if s is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String s)
    {
        int n = s.length() - 1;
        int i = 0;
        while(i <= n / 2)
        {
            if(s.charAt(i) != s.charAt(n - i))
                return false;
            i++;
        }
        return true;
    }
    
    /**
     * Calculates partial Scrabble score of word w
     * Letters have the following points:
     * - F, H, V, W and Y: 4 Points.
     * - K : 5 Points.
     * - J and X: 8 Points.
     * - Q and Z: 10 Points.
     * - any other letter: 1 Point
     *
     * @param word : one word with letters from Latin alphabet (a-z,A-Z)
     * @return : the value obtained by adding up the points of every letter of word
     * : 0 if word is empty
     * : or -1 if word is null or if it contains a character that's not a letter
     */
    public static int wordScore(String word)
    {
        if(word == null)
        {
            return -1;
        }
        
        int sum = 0;
        
        int i = 0;
        String w = word.toUpperCase();
        while(i < w.length())
        {
            if((w.charAt(i) >= 'A') && (w.charAt(i) <= 'Z'))
            {
                switch(w.charAt(i))
                {
                    case 'F':
                    case 'H':
                    case 'V':
                    case 'W':
                    case 'Y':
                        sum += 4;
                        break;
                    case 'J':
                    case 'X':
                        sum += 8;
                        break;
                    case 'Q':
                    case 'Z':
                        sum += 10;
                        break;
                    default:
                        sum++;
                }
            }
            else
            {
                return -1;
            }
            i++;
        }
        return sum;
    }
    
    // Teil 2
    
    /**
     * Plays a two-player game where each player must enter a word that starts with
     * the last two letters of the word previously entered by the other player.
     * The first player may enter any word at the start of the game.
     * A player's score is the sum of the points of all words entered by that player.
     * Each palindrome word counts twice,i.e., the points of each palindrome are doubled
     * in the player score.
     * The game ends when a player's score reaches or exceeds the maximum value,
     * or when a player can't find a word to chain. In this case, the player may enter any word.
     *
     * @param stopPoints : a positive integer used to stop the game (see above).
     * @return : the highest player score plus information about who won (P1 or P2).
     */
    public static int chainWords(int stopPoints)
    {
        int sum1 = 0;
        int sum2 = 0;
        
        System.out.print("P1 input: ");
        String w1 = SavitchIn.readLine();
        int l1 = w1.length();
        if(l1 < 2)
            return 0;
        
        sum1 += isPalindrome(w1) ? 2 * wordScore(w1) : wordScore(w1);
        System.out.println("P1 score is " + sum1);
        while(sum1 < stopPoints)
        {
            
            System.out.print("P2 input: ");
            String w2 = SavitchIn.readLine();
            if(isChain(w1, w2))
            {
                sum2 += isPalindrome(w2) ? 2 * wordScore(w2) : wordScore(w2);
            }
            else
            {
                break;
            }
            System.out.println("P2 score is " + sum2);
            
            if(sum2 >= stopPoints)
            {
                break;
            }
            
            System.out.print("P1 input: ");
            w1 = SavitchIn.readLine();
            if(isChain(w2, w1))
            {
                sum1 += isPalindrome(w1) ? 2 * wordScore(w1) : wordScore(w1);
            }
            else
            {
                break;
            }
            System.out.println("P1 score is " + sum1);
        }
        return sum1 > sum2 ? sum1 : (0 - sum2);
    }
    
    /**
     * Helper method, not included in md
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isChain(String s1, String s2)
    {
        int l1 = s1.length();
        int l2 = s2.length();
        return (l1 > 1) && (l2 > 1) && (s1.charAt(l1 - 2) == s2.charAt(0)) && (s1.charAt(l1 - 1) == s2.charAt(1));
    }
}
