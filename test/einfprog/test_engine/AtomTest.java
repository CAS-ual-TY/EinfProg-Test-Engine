package einfprog.test_engine;

import org.opentest4j.AssertionFailedError;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class AtomTest extends OutputTest
{
    private String error;
    private Atom[] input;
    private Compound[] lines;
    
    private Runnable test;
    
    private MethodInvokeTest<?, ?> methodInvokeTest;
    
    public AtomTest(String error, Runnable test, Atom[] input, Compound... output)
    {
        super(test);
        this.error = error;
        this.input = input;
        lines = output;
        
        methodInvokeTest = null;
    }
    
    public AtomTest(Runnable test, Atom[] input, Compound... output)
    {
        this(null, test, input, output);
    }
    
    public AtomTest(MethodInvokeTest<?, ?> methodInvokeTest, Atom[] input, Compound... output)
    {
        this("When calling method \"" + methodInvokeTest.getMethodName() + "\" in class \"" + methodInvokeTest.getMethodClass().getSimpleName() + "\":",
                () -> Engine.ENGINE.checkTest(methodInvokeTest), input, output);
        this.methodInvokeTest = methodInvokeTest;
    }
    
    public AtomTest(MethodInvokeTest<?, ?> methodInvokeTest, Compound... output)
    {
        this(methodInvokeTest, Atom.construct(), output);
    }
    
    @Override
    public String[] getInput()
    {
        return Arrays.stream(input).map(Atom::toString).toArray(String[]::new);
    }
    
    @Override
    public boolean passes(PrintWriter errorCallback, String rawOutput)
    {
        String[] outputLines = Util.prepareOutput(rawOutput);
        
        LinkedList<Compound> compounds = new LinkedList<>(Arrays.asList(lines));
        LinkedList<String> lines = new LinkedList<>(Arrays.asList(outputLines));
        
        // make sure there is no IndexOutOfBoundsException
        for(int i = 0; i < 100; i++)
        {
            lines.add("");
        }
        
        LinkedList<String> doneOutputs = new LinkedList<>();
        
        Compound compound;
        String line = lines.removeFirst();
        
        while(!compounds.isEmpty())
        {
            compound = compounds.removeFirst();
            
            int trim = 0;
            LinkedList<Atom> atoms = new LinkedList<>(compound.getAtoms());
            LinkedList<Atom> removedAtoms = new LinkedList<>();
            
            while(!atoms.isEmpty())
            {
                Atom atom = atoms.removeFirst();
                
                String substring = line.substring(trim);
                
                if(!atom.test(substring)) // +size to account for spaces between atoms
                {
                    errorCallback.println("Wrong console output:");
                    
                    if(error != null)
                    {
                        errorCallback.println(error);
                    }
                    
                    Util.strongSpacer(errorCallback);
                    
                    errorCallback.println("Expected: \"" + atom.toString() + "\"");
                    errorCallback.println("  (This is to be done:)");
                    Util.weakSpacer(errorCallback);
                    doneOutputs.forEach(errorCallback::println);
                    errorCallback.println(removedAtoms.stream().map(Atom::toString).collect(Collectors.joining()) + atom.toString() + atoms.stream().map(Atom::toString).collect(Collectors.joining()));
                    errorCallback.println(" ".repeat(trim + atom.getErrorOffset(substring)) + "^");
                    
                    Util.strongSpacer(errorCallback);
                    
                    errorCallback.println("Found: \"" + substring + "\"");
                    errorCallback.println("  (This is your output:)");
                    Util.weakSpacer(errorCallback);
                    doneOutputs.forEach(errorCallback::println);
                    errorCallback.println(line);
                    errorCallback.println(" ".repeat(trim + atom.getErrorOffset(substring)) + "^");
                    
                    Util.strongSpacer(errorCallback);
                    
                    appendParams(errorCallback);
                    appendInput(errorCallback);
                    
                    return false;
                }
                else
                {
                    trim += atom.trimOffString(substring);
                }
                
                removedAtoms.add(atom);
            }
            
            if(trim >= line.length())
            {
                doneOutputs.add(line);
                line = lines.removeFirst();
            }
            else
            {
                doneOutputs.add(line.substring(0, trim));
                line = line.substring(trim);
            }
        }
        
        return true;
    }
    
    public void appendParams(PrintWriter errorCallback)
    {
        if(methodInvokeTest != null)
        {
            methodInvokeTest.appendParams(errorCallback);
        }
    }
    
    public void appendInput(PrintWriter errorCallback)
    {
        if(input.length > 0)
        {
            errorCallback.println("With the following console input:");
            Util.weakSpacer(errorCallback);
            Arrays.stream(input).forEach(a -> errorCallback.println(a.toString()));
            
            Util.strongSpacer(errorCallback);
        }
    }
    
    @Override
    public void onError(Throwable e, PrintWriter errorCallback)
    {
        if(!(e instanceof AssertionFailedError))
        {
            errorCallback.println("An error occurred:");
            
            if(error != null)
            {
                errorCallback.println(error);
            }
            
            Util.strongSpacer(errorCallback);
            
            errorCallback.println("Exception:");
            Util.weakSpacer(errorCallback);
            if(Settings.PRINT_STACKTRACE_ON_ERROR)
            {
                e.printStackTrace(errorCallback);
            }
            else
            {
                errorCallback.println(e.getMessage());
            }
            
            Util.strongSpacer(errorCallback);
        }
        
        appendInput(errorCallback);
    }
}
