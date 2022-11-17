package einfprog.test_engine;

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
    
    public AtomTest(String error, Runnable test, Atom[] input, Compound... output)
    {
        super(test);
        this.error = error;
        this.input = input;
        lines = output;
    }
    
    public AtomTest(Runnable test, Atom[] input, Compound... output)
    {
        this("Wrong output detected!", test, input, output);
    }
    
    public AtomTest(MethodInvokeTest<?, ?> methodTest, Atom[] input, Compound... output)
    {
        this("Wrong output detected when calling " + methodTest.getMethodName() + "!", () -> Engine.ENGINE.checkTest(methodTest), input, output);
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
                    errorCallback.println(error);
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
                    
                    errorCallback.println("With the following input:");
                    Util.weakSpacer(errorCallback);
                    Arrays.stream(input).forEach(a -> errorCallback.println(a.toString()));
                    
                    Util.strongSpacer(errorCallback);
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
}
