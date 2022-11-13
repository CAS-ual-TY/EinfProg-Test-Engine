package einfprog.test_engine;

import java.util.regex.Pattern;

public class Util
{
    public static final String DOUBLE_REGEX;
    
    static
    {
        // according to Double#parseDouble docs
        final String Digits = "(\\p{Digit}+)";
        final String HexDigits = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp = "[eE][+-]?" + Digits;
        DOUBLE_REGEX =
                ("[\\x00-\\x20]*" +  // Optional leading "whitespace"
                        "[+-]?(" + // Optional sign character
                        "NaN|" +           // "NaN" string
                        "Infinity|" +      // "Infinity" string
                        
                        // A decimal floating-point string representing a finite positive
                        // number without a leading sign has at most five basic pieces:
                        // Digits . Digits ExponentPart FloatTypeSuffix
                        //
                        // Since this method allows integer-only strings as input
                        // in addition to strings of floating-point literals, the
                        // two sub-patterns below are simplifications of the grammar
                        // productions from section 3.10.2 of
                        // The Java Language Specification.
                        
                        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                        "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
                        
                        // . Digits ExponentPart_opt FloatTypeSuffix_opt
                        "(\\.(" + Digits + ")(" + Exp + ")?)|" +
                        
                        // Hexadecimal strings
                        "((" +
                        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "(\\.)?)|" +
                        
                        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
                        
                        ")[pP][+-]?" + Digits + "))" +
                        "[fFdD]?))" +
                        "[\\x00-\\x20]*");// Optional trailing "whitespace"
    }
    
    public static String removeFakeSpace(String s)
    {
        return s.replaceAll("[^\\S\\r\\n]", " ");
    }
    
    public static String removeMultiSpace(String s)
    {
        return s.replaceAll("( )( )+", " ");
    }
    
    public static String removeCR(String s)
    {
        return s.replaceAll("\r", "");
    }
    
    public static String removeMultiNewlines(String s)
    {
        return s.replaceAll("\n[\n ]*\n", "\n");
    }
    
    public static String prepareString(String s)
    {
        return removeMultiNewlines(removeMultiSpace(removeFakeSpace(removeCR(s))));
    }
    
    public static String[] prepareOutput(String rawOutput)
    {
        return prepareString(rawOutput).split("\n");
    }
    
    public static boolean isInt(String s)
    {
        return s.matches("[+-]?[1-9][0-9]*");
    }
    
    public static boolean isDouble(String s)
    {
        return Pattern.matches(DOUBLE_REGEX, s);
    }
}
