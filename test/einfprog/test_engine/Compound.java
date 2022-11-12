package einfprog.test_engine;

import java.util.LinkedList;
import java.util.List;

public class Compound
{
    private List<Atom> atoms;
    
    private Compound()
    {
        atoms = new LinkedList<>();
    }
    
    private Compound(Atom atom)
    {
        this();
        atoms.add(atom);
    }
    
    public List<Atom> getAtoms()
    {
        return atoms;
    }
    
    public static Builder builder()
    {
        return new Builder();
    }
    
    public static Compound construct(Object... atoms)
    {
        Compound compound = new Compound();
        
        for(Object o : atoms)
        {
            compound.getAtoms().add(Atom.detect(o));
        }
        
        return compound;
    }
    
    public Compound intAtom(int i)
    {
        atoms.add(Atom.intAtom(i));
        return this;
    }
    
    public Compound doubleAtom(double d, double error)
    {
        atoms.add(Atom.doubleAtom(d, error));
        return this;
    }
    
    public Compound doubleAtom(double d)
    {
        atoms.add(Atom.doubleAtom(d));
        return this;
    }
    
    public Compound stringAtom(String s)
    {
        atoms.add(Atom.stringAtom(s));
        return this;
    }
    
    public static Compound start()
    {
        return new Compound();
    }
    
    public static Compound start(int i)
    {
        return new Compound(Atom.intAtom(i));
    }
    
    public static Compound start(double d)
    {
        return new Compound(Atom.doubleAtom(d));
    }
    
    public static Compound start(String s)
    {
        return new Compound(Atom.stringAtom(s));
    }
    
    @Override
    public String toString()
    {
        return atoms.toString();
    }
    
    public static class Builder
    {
        private LinkedList<Compound> compounds;
        
        private Builder()
        {
            compounds = new LinkedList<>();
        }
        
        public Builder add(Object... atoms)
        {
            compounds.add(Compound.construct(atoms));
            return this;
        }
        
        // adding x and then repeating n times will end up with exactly n-amount of x, NOT n+1
        // repeat(n) does nothing  iff.  n <= 1
        public Builder repeat(int totalTimes)
        {
            if(compounds.isEmpty())
            {
                throw new IllegalStateException();
            }
            
            for(int i = 1; i < totalTimes; i++)
            {
                compounds.add(compounds.getLast());
            }
            
            return this;
        }
        
        public Compound[] build()
        {
            return compounds.toArray(Compound[]::new);
        }
    }
}
