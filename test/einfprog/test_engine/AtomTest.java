package einfprog.test_engine;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class AtomTest extends AbstractTest
{
    public static int LINE_LENGTH = 50;
    
    private Atom[] input;
    private Compound[] lines;
    private Runnable test;
    
    public AtomTest(Runnable test, Atom[] input, Compound... output)
    {
        super(test);
        this.input = input;
        lines = output;
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
                    errorCallback.println("Wrong output detected!");
                    errorCallback.println("=".repeat(LINE_LENGTH));
                    
                    errorCallback.println("Expected: \"" + atom.toString() + "\"");
                    errorCallback.println("  (This is to be done:)");
                    errorCallback.println("-".repeat(LINE_LENGTH));
                    doneOutputs.forEach(errorCallback::println);
                    errorCallback.println(removedAtoms.stream().map(Atom::toString).collect(Collectors.joining()) + atom.toString());
                    errorCallback.println(" ".repeat(trim + atom.getErrorOffset(substring)) + "^");
                    
                    errorCallback.println("=".repeat(LINE_LENGTH));
                    
                    errorCallback.println("Found: \"" + substring + "\"");
                    errorCallback.println("  (This is your output:)");
                    errorCallback.println("-".repeat(LINE_LENGTH));
                    doneOutputs.forEach(errorCallback::println);
                    errorCallback.println(line);
                    errorCallback.println(" ".repeat(trim + atom.getErrorOffset(substring)) + "^");
                    
                    errorCallback.println("=".repeat(LINE_LENGTH));
                    
                    errorCallback.println("With the following input:");
                    errorCallback.println("-".repeat(LINE_LENGTH));
                    Arrays.stream(input).forEach(a -> errorCallback.println(a.toString()));
                    
                    errorCallback.println("=".repeat(LINE_LENGTH));
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
