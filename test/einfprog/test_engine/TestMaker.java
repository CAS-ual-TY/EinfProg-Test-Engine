package einfprog.test_engine;

import einfprog.test_engine.base.*;
import einfprog.test_engine.output.Atom;
import einfprog.test_engine.output.Compound;
import einfprog.test_engine.params.IParamSet;
import einfprog.test_engine.params.IParamTypeSet;
import einfprog.test_engine.params.ParamSet1;
import einfprog.test_engine.tests.*;

import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestMaker<C, T, PC extends IParamTypeSet, PM extends IParamTypeSet>
{
    public TestMaker<C, T, PC, PM> fork()
    {
        TestMaker<C, T, PC, PM> maker = new TestMaker<>();
        maker.classGetter = classGetter;
        maker.instanceGetter = instanceGetter;
        maker.valueGetter = valueGetter;
        maker.valueTester = valueTester;
        maker.lastTest = lastTest;
        maker.input = input;
        maker.output = output;
        maker.stackedFeedback.append(stackedFeedback.toString());
        return maker;
    }
    
    public TestMaker<C, T, PC, PM> hardInstanceFork()
    {
        TestMaker<C, T, PC, PM> maker = fork();
        maker.instanceGetter = new InstanceGetter<>(maker.getInstance());
        maker.stackedFeedback.delete(0, maker.stackedFeedback.length());
        return maker;
    }
    
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
    
    public TestMaker<C, T, PC, PM> setValue(T value)
    {
        valueGetter = new ValueGetter<>(value);
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testValue(Predicate<T> predicate, Function<T, String> toStringFunc, String expected)
    {
        assert valueGetter != null;
        valueTester = new ValueTester<>(valueGetter, predicate, toStringFunc, expected);
        lastTest = valueTester;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testValue(Function<T, String> toStringFunc, T expectedValue)
    {
        assert valueGetter != null;
        valueTester = new ValueTester<>(valueGetter, toStringFunc, expectedValue);
        lastTest = valueTester;
        return this;
    }
    
    public TestMaker<C, T, PC, PM> testValue(T expectedValue)
    {
        assert valueGetter != null;
        valueTester = new ValueTester<>(valueGetter, expectedValue);
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
        input = new Atom[0];
        output = new Compound[0];
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
    
    public T getValue()
    {
        assert valueGetter != null;
        return valueGetter.getValue();
    }
    
    public TestMaker<C, T, PC, PM> forValue(Consumer<T> consumer)
    {
        consumer.accept(getValue());
        return this;
    }
    
    public Class<? extends C> getTheClass()
    {
        assert classGetter != null;
        return classGetter.findClass();
    }
    
    public TestMaker<C, T, PC, PM> forClass(Consumer<Class<? extends C>> consumer)
    {
        consumer.accept(getTheClass());
        return this;
    }
    
    public static <C, T, PC extends IParamTypeSet, PM extends IParamTypeSet> TestMaker<C, T, PC, PM> builder()
    {
        return new TestMaker<>();
    }
    
    public static <C, T, PC extends IParamTypeSet, PM extends IParamTypeSet> void newMaker(Consumer<TestMaker<C, T, PC, PM>> consumer)
    {
        consumer.accept(builder());
    }
    
    public TestMaker<C, T, PC, PM> forClassAndInstance(BiConsumer<Class<? extends C>, C> consumer)
    {
        consumer.accept(getTheClass(), getInstance());
        return this;
    }
    
    public TestMaker<C, T, PC, PM> forInstanceAndMaker(BiConsumer<TestMaker<C, T, PC, PM>, C> consumer)
    {
        consumer.accept(this, getInstance());
        return this;
    }
    
    public static TestMaker<?, ?, ?, ?> callMain(String clazz)
    {
        return builder().findClass(clazz).statically().callMethod("main", void.class, new ParamSet1<String[]>(new String[0]));
    }
}
