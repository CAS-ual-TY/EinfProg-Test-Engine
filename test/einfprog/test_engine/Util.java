package einfprog.test_engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util
{
    public static final String DOUBLE_REGEX;
    public static final String INT_REGEX = "[+-]?[1-9][0-9]*";
    
    public static final Object[] EMPTY_OBJ_ARR = new Object[0];
    
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
        if(o instanceof Object[])
        {
            return (Object[]) o;
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
    
    public static String objectToString(Object t)
    {
        if(t == null)
        {
            return "null";
        }
        else if(t.getClass().isArray())
        {
            return arraysToString(t);
        }
        else
        {
            return t.toString();
        }
    }
    
    public static String arraysToString(Object t)
    {
        return "[" + Arrays.stream(castToObjArray(t)).map(Objects::toString).collect(Collectors.joining(", ")) + "]";
    }
    
    public static boolean objectsEquals(Object t, Object x)
    {
        if(t == x)
        {
            return true;
        }
        else if(t == null || x == null)
        {
            return false;
        }
        else if(t.getClass() != x.getClass())
        {
            return false;
        }
        else if(t.getClass().isArray())
        {
            return arraysEquals(t, x);
        }
        else if(t.getClass() == Double.class)
        {
            return doubleEquals((double) t, (double) x);
        }
        else if(t.getClass() == Float.class)
        {
            return doubleEquals((float) t, (float) x);
        }
        else
        {
            return t.equals(x);
        }
    }
    
    public static boolean arraysEquals(Object t, Object x)
    {
        Object[] ts = castToObjArray(t);
        Object[] xs = castToObjArray(x);
        
        if(ts.length != xs.length)
        {
            return false;
        }
        else if(ts.length == 0)
        {
            return true;
        }
        
        BiPredicate<Object, Object> comparator = Object::equals;
        
        if(t.getClass() == double[].class || t.getClass() == Double[].class)
        {
            comparator = (d1, d2) -> doubleEquals((double) d1, (double) d2);
        }
        else if(t.getClass() == float[].class || t.getClass() == Float[].class)
        {
            comparator = (f1, f2) -> doubleEquals((float) f1, (float) f2);
        }
        
        for(int i = 0; i < ts.length; i++)
        {
            if((ts[i] == null || xs[i] == null) && ts[i] != xs[i])
            {
                return false;
            }
            else if(!comparator.test(xs[i], ts[i]))
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
    
    public static <T> Object[] objArr(T o)
    {
        return new Object[] {o};
    }
    
    public static Object[] objArr(Object... arr)
    {
        return arr;
    }
    
    public static Runnable suppressClassloading(Supplier<Runnable> supplier)
    {
        return supplier.get();
    }
    
    public static void error(PrintWriter errorCallback, Throwable e)
    {
        errorCallback.println("Something went wrong... Please report this error.");
        weakSpacer(errorCallback);
        e.printStackTrace(errorCallback);
    }
    
    public static void writeExceptionToPW(PrintWriter errorCallback, Throwable e)
    {
        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw))
        {
            pw.println(e.getClass().getName() + (e.getMessage() != null ? (": " + e.getMessage()) : ""));
            
            if(Settings.PRINT_STACKTRACE_ON_ERROR)
            {
                String s0 = "";
                for(StackTraceElement element : e.getStackTrace())
                {
                    String s = element.toString();
                    
                    if(s.contains("einfprog.test_engine"))
                    {
                        break;
                    }
                    else if(s.equals(s0))
                    {
                        // eg. recursive stack overflow
                        pw.println("    " + s);
                        pw.println("    " + s);
                        break;
                    }
                    
                    pw.println("    " + s);
                    s0 = s;
                }
                
                pw.println("    ...");
                
                s0 = sw.toString();
                errorCallback.println(s0.substring(0, s0.length() - System.lineSeparator().length()));
            }
        }
        catch(IOException ioException)
        {
        }
    }
}
