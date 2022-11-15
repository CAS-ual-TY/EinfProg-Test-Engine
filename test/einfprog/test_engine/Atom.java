package einfprog.test_engine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.IntFunction;

public abstract class Atom
{
    
    public static Builder builder()
    {
        return new Builder();
    }
    
    public static Atom[] construct(Object... atoms)
    {
        return Arrays.stream(atoms).map(Atom::detect).toArray(Atom[]::new);
    }
    
    public static Atom detect(Object o)
    {
        if(o instanceof Atom atom)
        {
            return atom;
        }
        else if(o instanceof String s)
        {
            return stringAtom(s);
        }
        else if(o instanceof Number n)
        {
            if(n instanceof Double || n instanceof Float)
            {
                return doubleAtom(n.doubleValue());
            }
            else
            {
                return intAtom(n.intValue());
            }
        }
        else
        {
            throw new IllegalArgumentException("What to do with this object? \"" + o + "\" (" + o.getClass() + ")");
        }
    }
    
    public static Atom intAtom(int i)
    {
        return new IntAtom(i);
    }
    
    public static Atom doubleAtom(double d, double error)
    {
        return new DoubleAtom(d, error);
    }
    
    public static Atom doubleAtom(double d)
    {
        return doubleAtom(d, Settings.DEFAULT_DOUBLE_ERROR);
    }
    
    public static Atom stringAtom(String s)
    {
        return new StringAtom(s);
    }
    
    public abstract boolean test(String s);
    
    // assumes that test(String) already passed
    public abstract int trimOffString(String s);
    
    public int getErrorOffset(String s)
    {
        return 0;
    }
    
    public static class IntAtom extends Atom
    {
        public int atom;
        
        public IntAtom(int i)
        {
            atom = i;
        }
        
        @Override
        public boolean test(String s)
        {
            String[] split = s.split("\\s", 2);
            String numberString = split[0];
            
            if(numberString == null || !Util.isInt(numberString))
            {
                return false;
            }
            
            int number = Integer.parseInt(numberString);
            
            return number == atom;
        }
        
        @Override
        public int trimOffString(String s)
        {
            return s.split("\\s", 2)[0].length();
        }
        
        @Override
        public String toString()
        {
            return String.valueOf(atom);
        }
    }
    
    public static class DoubleAtom extends Atom
    {
        public double atom;
        public double error;
        
        public DoubleAtom(double d, double error)
        {
            atom = d;
            this.error = error;
        }
        
        @Override
        public boolean test(String s)
        {
            String[] split = s.split("\\s", 2);
            String numberString = split[0];
            
            if(numberString == null || !Util.isDouble(numberString))
            {
                return false;
            }
            
            return Util.doubleEquals(atom, Double.parseDouble(numberString), error);
        }
        
        @Override
        public int trimOffString(String s)
        {
            return s.split("\\s", 2)[0].length();
        }
        
        @Override
        public String toString()
        {
            return String.valueOf(atom);
        }
    }
    
    public static class StringAtom extends Atom
    {
        public String atom;
        
        public StringAtom(String s)
        {
            atom = s;
        }
        
        @Override
        public boolean test(String s)
        {
            return s.startsWith(atom);
        }
        
        @Override
        public int trimOffString(String s)
        {
            return atom.length();
        }
        
        @Override
        public String toString()
        {
            return atom;
        }
        
        @Override
        public int getErrorOffset(String s)
        {
            int max = Math.min(s.length(), atom.length());
            
            for(int i = 0; i < max; i++)
            {
                if(s.charAt(i) != atom.charAt(i))
                {
                    return i;
                }
            }
            
            return max;
        }
    }
    
    public static class Builder
    {
        private LinkedList<Atom> atoms;
        
        private Builder()
        {
            atoms = new LinkedList<>();
        }
        
        public Builder add(Object o)
        {
            atoms.add(Atom.detect(o));
            return this;
        }
        
        // adding x and then repeating n times will end up with exactly n-amount of x, NOT n+1
        // repeat(n) does nothing  iff.  n <= 1
        // repeats the last element added, NOT the last function call on the builder
        public Builder repeat(int totalTimes)
        {
            if(atoms.isEmpty())
            {
                throw new IllegalStateException();
            }
            
            for(int i = 1; i < totalTimes; i++)
            {
                atoms.add(atoms.getLast());
            }
            
            return this;
        }
        
        public Builder add(int totalTimes, IntFunction<Object> atomFactory)
        {
            for(int i = 0; i < totalTimes; i++)
            {
                atoms.add(Atom.detect(atomFactory.apply(i)));
            }
            
            return this;
        }
        
        public Atom[] build()
        {
            return atoms.toArray(Atom[]::new);
        }
    }
}
