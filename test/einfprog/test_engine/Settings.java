package einfprog.test_engine;

public class Settings
{
    // length of "==...=" and "--...-" lines
    public static int SPACING_LINE_LENGTH = 100;
    
    // default permitted error:
    // x = required value,  o = output value
    // o passes  iff.  abs(x - o) <= error
    public static double DEFAULT_DOUBLE_ERROR = 0.1D;
    
    public static boolean PRINT_STACKTRACE_ON_ERROR = true;
}
