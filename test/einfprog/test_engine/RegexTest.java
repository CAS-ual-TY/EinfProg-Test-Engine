package einfprog.test_engine;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest extends AbstractTest
{
    private String regex;
    private Supplier<String> expected;
    private BiPredicate<Pattern, Matcher> valueTest;
    private String[] input;
    
    public RegexTest(Runnable test, String regex, Supplier<String> expected, BiPredicate<Pattern, Matcher> valueTest, String... input)
    {
        super(test);
        this.regex = regex;
        this.expected = expected;
        this.valueTest = valueTest;
        this.input = input;
    }
    
    public RegexTest(Runnable test, String regex, Supplier<String> expected, String... input)
    {
        this(test, regex, expected, (pattern, matcher) -> true, input);
    }
    
    @Override
    public String[] getInput()
    {
        return input;
    }
    
    @Override
    public boolean passes(PrintWriter errorCallback, String rawOutput)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawOutput);
        
        if(!matcher.find() || !valueTest.test(pattern, matcher))
        {
            printError(errorCallback, rawOutput);
            return false;
        }
        
        return true;
    }
    
    protected void printError(PrintWriter errorCallback, String rawOutput)
    {
        String expected = this.expected.get();
        
        errorCallback.println("Wrong output detected!");
        errorCallback.println("=".repeat(AtomTest.LINE_LENGTH));
        
        errorCallback.println("Expected:");
        errorCallback.println("  (This is to be done:)");
        errorCallback.println("-".repeat(AtomTest.LINE_LENGTH));
        errorCallback.print(expected);
        if(!expected.endsWith(System.lineSeparator()))
        {
            errorCallback.println();
        }
        
        errorCallback.println("=".repeat(AtomTest.LINE_LENGTH));
        
        errorCallback.println("Found:");
        errorCallback.println("  (This is your output:)");
        errorCallback.println("-".repeat(AtomTest.LINE_LENGTH));
        errorCallback.print(rawOutput);
        if(!rawOutput.endsWith(System.lineSeparator()))
        {
            errorCallback.println();
        }
        
        errorCallback.println("=".repeat(AtomTest.LINE_LENGTH));
        
        errorCallback.println("With the following input:");
        errorCallback.println("-".repeat(AtomTest.LINE_LENGTH));
        Arrays.stream(input).forEach(errorCallback::println);
        
        errorCallback.println("=".repeat(AtomTest.LINE_LENGTH));
    }
}
