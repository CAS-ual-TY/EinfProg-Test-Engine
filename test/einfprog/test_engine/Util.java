package einfprog.test_engine;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util
{
    public static final String DOUBLE_REGEX;
    public static final String INT_REGEX = "[+-]?[1-9][0-9]*";
    
    static
    {
        // according to Double#parseDouble docs
        final String Digits = "(\\p{Digit}+)";
        final String HexDigits = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp = "[eE][+-]?" + Digits;
        DOUBLE_REGEX = "(" +
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
                        "[\\x00-\\x20]*")// Optional trailing "whitespace"
                + ")";
    }
    
    public static void strongSpacer(PrintWriter pw)
    {
        pw.println("=".repeat(Settings.SPACING_LINE_LENGTH));
    }
    
    public static void weakSpacer(PrintWriter pw)
    {
        pw.println("-".repeat(Settings.SPACING_LINE_LENGTH));
    }
    
    public static void testSpacer()
    {
        System.err.println();
        System.err.println("#".repeat(Settings.SPACING_LINE_LENGTH * 2));
        System.err.println("#".repeat(Settings.SPACING_LINE_LENGTH * 2));
        System.err.println();
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
        return s.matches(INT_REGEX);
    }
    
    public static boolean isDouble(String s)
    {
        return Pattern.matches(DOUBLE_REGEX, s);
    }
    
    public static boolean doubleEquals(double x, double y, double error)
    {
        return Math.abs(x - y) < error;
    }
    
    public static boolean doubleEquals(double x, double y)
    {
        return doubleEquals(x, y, Settings.DEFAULT_DOUBLE_ERROR);
    }
    
    public static boolean intEquals(Matcher matcher, String groupName, int value)
    {
        String s = matcher.group(groupName).trim();
        return isInt(s) && value == Integer.parseInt(s);
    }
    
    public static boolean doubleEquals(Matcher matcher, String groupName, double value, double error)
    {
        String s = matcher.group(groupName).trim();
        return isDouble(s) && doubleEquals(value, Double.parseDouble(s), error);
    }
    
    public static boolean doubleEquals(Matcher matcher, String groupName, double value)
    {
        return doubleEquals(matcher, groupName, value, Settings.DEFAULT_DOUBLE_ERROR);
    }
    
    public static Object[] castToObjArray(Object o)
    {
        if (o instanceof Object[])
        {
            return (Object[])o;
        }
        int length = Array.getLength(o);
        Object[] outputArray = new Object[length];
        for(int i = 0; i < length; ++i)
        {
            outputArray[i] = Array.get(o, i);
        }
        return outputArray;
    }
    
    public static <T> Predicate<T> arraysEqualsPredicate(T t)
    {
        return x -> arraysEquals(t, x);
    }
    
    public static boolean arraysEquals(Object t, Object x)
    {
        if(t.getClass().isArray() != x.getClass().isArray())
        {
            return false;
        }
        
        Object[] ts = castToObjArray(t);
        Object[] xs = castToObjArray(x);
        
        if(ts.length != xs.length)
        {
            return false;
        }
        
        for(int i = 0; i < ts.length; i++)
        {
            if((ts[i] == null || xs[i] == null) && ts[i] != xs[i])
            {
                return false;
            }
            if(!ts[i].equals(xs[i]))
            {
                return false;
            }
        }
        
        return true;
    }
    
    public static Class<?> unboxClass(Class<?> o)
    {
        if(o == Byte.class)
        {
            return byte.class;
        }
        if(o == Short.class)
        {
            return short.class;
        }
        if(o == Integer.class)
        {
            return int.class;
        }
        if(o == Long.class)
        {
            return long.class;
        }
        if(o == Float.class)
        {
            return float.class;
        }
        if(o == Double.class)
        {
            return double.class;
        }
        if(o == Boolean.class)
        {
            return boolean.class;
        }
        if(o == Character.class)
        {
            return char.class;
        }
        
        return o;
    }
    
    public static Runnable suppressClassloading(Supplier<Runnable> supplier)
    {
        return supplier.get();
    }
}
