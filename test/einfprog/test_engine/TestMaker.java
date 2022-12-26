package einfprog.test_engine;

import einfprog.test_engine.base.*;
import einfprog.test_engine.output.Atom;
import einfprog.test_engine.output.Compound;
import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;
import einfprog.test_engine.params.ParamSet1;
import einfprog.test_engine.tests.*;

import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TestMaker<C, T, PC extends IParamTypeSet, PM extends IParamTypeSet>
{
    private IClassGetter<C> classGetter;
    private IInstanceGetter<C> instanceGetter;
    private IValueGetter<T> valueGetter;
    private IValueTester<T> valueTester;
    
    private IFeedback lastTest;
    private Atom[] input;
    private Compound[] output;
    
    private StringBuilder stackedFeedback;
    
    public TestMaker()
    {
        classGetter = null;
        instanceGetter = null;
        valueGetter = null;
        valueTester = null;
        
        input = new Atom[0];
        output = new Compound[0];
        
        stackedFeedback = new StringBuilder();
    }
    
    public TestMaker<C, T, PC, PM> findClass(String className)
    {
        classGetter = new ClassTest<>(className);
        lastTest = classGetter;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> withClass(Class<? extends C> clazz)
    {
        classGetter = new ClassGetter<>(clazz);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> makeInstance(IParamSet<? extends PC> constructorParams)
    {
        assert classGetter != null;
        instanceGetter = new ConstructorInvokeTest<C, PC>(classGetter, Modifier.PUBLIC, constructorParams);
        lastTest = instanceGetter;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> withInstance(C instance)
    {
        instanceGetter = new InstanceGetter<>(instance);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> statically()
    {
        assert classGetter != null;
        instanceGetter = new StaticInstance<>(classGetter);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> checkMethodSignature(String methodName, int modifiers, Class<? extends T> returnValueType, PM methodTypeParams)
    {
        assert classGetter != null;
        lastTest = new MethodTest<>(classGetter, methodName, modifiers, returnValueType, methodTypeParams);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> callMethod(String methodName, Class<? extends T> returnValueType, int modifiers, IParamSet<? extends PM> methodParams)
    {
        assert classGetter != null && instanceGetter != null;
        valueGetter = new MethodInvokeTest<>(classGetter, instanceGetter, methodName, modifiers, returnValueType, methodParams);
        lastTest = valueGetter;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> callMethod(String methodName, Class<? extends T> returnValueType, IParamSet<? extends PM> methodParams)
    {
        return callMethod(methodName, returnValueType, Modifier.PUBLIC | (instanceGetter instanceof StaticInstance ? Modifier.STATIC : 0), methodParams);
    }
    
    public TestMaker<C, T, PC, PM> checkFieldSignature(String fieldName, int modifiers, Class<? extends T> fieldType)
    {
        assert classGetter != null;
        lastTest = new FieldTest<>(classGetter, fieldName, modifiers, fieldType);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testField(String fieldName, int modifiers, Class<? extends T> returnType)
    {
        assert classGetter != null && instanceGetter != null;
        valueGetter = new FieldValueTest<>(classGetter, instanceGetter, fieldName, modifiers, returnType);
        lastTest = valueGetter;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testField(String fieldName, Class<? extends T> returnType)
    {
        return testField(fieldName, Modifier.PUBLIC | (instanceGetter instanceof StaticInstance ? Modifier.STATIC : 0), returnType);
    }
    
    public TestMaker<C, T, PC, PM> forValue(T value)
    {
        valueGetter = new ValueGetter<>(value);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testValue(Predicate<T> predicate, String expected)
    {
        assert valueGetter != null;
        valueTester = new ValueTester<>(valueGetter, predicate, expected);
        lastTest = valueTester;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testValue(T expected)
    {
        assert valueGetter != null;
        valueTester = new ValueTester<>(valueGetter, v -> Util.objectsEquals(expected, v), Util.objectToString(expected));
        lastTest = valueTester;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> runnable(Runnable r, String onError)
    {
        lastTest = new RunnableTest(r, onError);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> runnable(Runnable r)
    {
        lastTest = new RunnableTest(r);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> setConsoleInput(Atom... input)
    {
        this.input = input;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> expectConsoleOutput(Compound... output)
    {
        this.output = output;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> runTest()
    {
        assert lastTest != null;
        IOutputProcessor out = new AtomTest(input, output);
        Engine.ENGINE.checkTest(lastTest, out, unusedFeedback ->
                {
                    stackedFeedback.insert(0, unusedFeedback);
                },
                errorCallback ->
                {
                    errorCallback.println(stackedFeedback.toString());
                });
        return this;
    }
    
    public C getInstance()
    {
        assert instanceGetter != null;
        return instanceGetter.getInstance();
    }
    
    public TestMaker<C, T, PC, PM> forInstance(Consumer<C> consumer)
    {
        consumer.accept(getInstance());
        return this;
    }
    
    public static <C, T, PC extends IParamTypeSet, PM extends IParamTypeSet> TestMaker<C, T, PC, PM> builder()
    {
        return new TestMaker<>();
    }
    
    public static TestMaker<?, ?, ?, ?> callMain(String clazz)
    {
        return builder().findClass(clazz).statically().callMethod("main", void.class, new ParamSet1<String[]>(new String[0]));
    }
}
