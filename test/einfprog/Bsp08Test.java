package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.TestMaker;
import einfprog.test_engine.Util;
import einfprog.test_engine.params.ParamSet0;
import einfprog.test_engine.params.ParamSet1;
import einfprog.test_engine.params.ParamSet2;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public class Bsp08Test
{
    @Test
    public void testMain()
    {
        TestItem[] items = new TestItem[] {new TestItem("Item1", "Owner-A"), new TestItem("AnotherItem", "Owner-B"), new TestItem("ItemNo3", "SomeOwner")};
        
        for(TestItem item : items)
        {
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>(item.getDescription(), item.getOwner()))
                    .callMethod("toString", String.class, new ParamSet0())
                    .testValue(item.toString())
                    .runTest();
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>(item.getDescription(), item.getOwner()))
                    .testField("description", Modifier.PRIVATE, String.class)
                    .testValue(item.getDescription())
                    .runTest();
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>(item.getDescription(), item.getOwner()))
                    .testField("owner", Modifier.PRIVATE, String.class)
                    .testValue(item.getOwner())
                    .runTest();
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>(item.getDescription(), item.getOwner()))
                    .callMethod("getDescription", String.class, new ParamSet0())
                    .testValue(item.getDescription())
                    .runTest();
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>(item.getDescription(), item.getOwner()))
                    .callMethod("getOwner", String.class, new ParamSet0())
                    .testValue(item.getOwner())
                    .runTest();
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>("dummy", "dummy"))
                    .callMethod("setDescription", void.class, new ParamSet1<>(item.getDescription()))
                    .runTest()
                    .callMethod("getDescription", String.class, new ParamSet0())
                    .testValue(item.getDescription())
                    .runTest();
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>("dummy", "dummy"))
                    .callMethod("setOwner", void.class, new ParamSet1<>(item.getOwner()))
                    .runTest()
                    .callMethod("getOwner", String.class, new ParamSet0())
                    .testValue(item.getOwner())
                    .runTest();
        }
        
        int[] sizes = new int[] {3, 6, 11};
        TestItem[] moreItems = new TestItem[] {
                new TestItem("Item1", "Owner-A"),
                new TestItem("AnotherItem", "Owner-B"),
                new TestItem("ItemNo3", "SomeOwner"),
                new TestItem("ItemFour", "FunnyOwner"),
                new TestItem("ItemThe5th", "Owner-V"),
                new TestItem("CursedItem6", "CursedOwner")
        };
        TestItem unused0 = new TestItem("BestAndCoolestItemPossible", "Luis");
        TestItem unused1 = new TestItem("MyItem7", "MyOwner");
        TestItem unused2 = new TestItem("LastItem8", "FinalOwner");
        
        for(int size : sizes)
        {
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .runTest()
                    .forClass(itemClass ->
                    {
                        Class<?> itemArrayClass = itemClass.arrayType();
                        
                        TestMaker.builder()
                                .findClass("einfprog.ItemList")
                                .makeInstance(new ParamSet1<>(size))
                                .testField("items", Modifier.PRIVATE, itemArrayClass)
                                .testValue(arr -> Array.getLength(arr) == size && Arrays.stream((Object[]) arr).allMatch(Objects::isNull), Util::objectToString, Util.arraysToString(new Object[size]))
                                .runTest();
                    });
            
            TestMaker.newMaker(maker ->
            {
                maker.findClass("einfprog.ItemList")
                        .makeInstance(new ParamSet1<>(size))
                        .runTest();
                
                TestItemList myList = new TestItemList(size);
                
                Arrays.stream(moreItems).forEach(i ->
                {
                    maker.hardInstanceFork()
                            .callMethod("isEmpty", boolean.class, new ParamSet0())
                            .testValue(myList.isEmpty())
                            .runTest();
                    
                    maker.callMethod("add", int.class, new ParamSet2<>(i.getDescription(), i.getOwner()))
                            .testValue(myList.add(i))
                            .runTest();
                    
                    maker.hardInstanceFork()
                            .callMethod("isFull", boolean.class, new ParamSet0())
                            .testValue(myList.isFull())
                            .runTest();
                    
                    maker.hardInstanceFork()
                            .callMethod("hasGaps", boolean.class, new ParamSet0())
                            .testValue(myList.hasGaps())
                            .runTest();
                    
                    maker.hardInstanceFork()
                            .callMethod("getCapacity", int.class, new ParamSet0())
                            .testValue(myList.getCapacity())
                            .runTest();
                    
                    maker.hardInstanceFork()
                            .callMethod("getSize", int.class, new ParamSet0())
                            .testValue(myList.getSize())
                            .runTest();
                    
                    maker.hardInstanceFork()
                            .callMethod("toString", String.class, new ParamSet0())
                            .testValue(myList.toString())
                            .runTest();
                    
                    myList.clear();
                    
                    maker.hardInstanceFork()
                            .callMethod("clear", void.class, new ParamSet0())
                            .runTest();
                    
                    maker.hardInstanceFork()
                            .callMethod("toString", String.class, new ParamSet0())
                            .testValue(myList.toString())
                            .runTest();
                });
            });
            
            TestItemList myList = new TestItemList(3);
            myList.add(unused1);
            myList.add(unused0);
            myList.add(unused2);
            
            TestMaker.builder()
                    .findClass("einfprog.Item")
                    .makeInstance(new ParamSet2<>(unused0.getDescription(), unused0.getOwner()))
                    .runTest()
                    .forClassAndInstance((itemClass, item1) ->
                    {
                        TestMaker.newMaker(maker -> {
                            maker.findClass("einfprog.ItemList")
                                    .makeInstance(new ParamSet1<>(size))
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("set", itemClass, new ParamSet2<>(itemClass, int.class, null, 0))
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("set", itemClass, new ParamSet2<>(item1, 0))
                                    .testValue(null)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("set", itemClass, new ParamSet2<>(itemClass, int.class, null, 0))
                                    .testValue(item1)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("set", itemClass, new ParamSet2<>(item1, 1))
                                    .testValue(null)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("get", itemClass, new ParamSet1<>(1))
                                    .testValue(item1)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("getFirstIndex", int.class, new ParamSet1<>(itemClass, item1))
                                    .testValue(1)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("hasGaps", boolean.class, new ParamSet0())
                                    .testValue(true)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("add", int.class, new ParamSet2<>(unused1.getDescription(), unused1.getOwner()))
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("hasGaps", boolean.class, new ParamSet0())
                                    .testValue(false)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("add", int.class, new ParamSet2<>(unused2.getDescription(), unused2.getOwner()))
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("hasGaps", boolean.class, new ParamSet0())
                                    .testValue(false)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .testField("items", Modifier.PRIVATE, itemClass.arrayType())
                                    .runTest()
                                    .forValue(itemsField ->
                                    {
                                        maker.hardInstanceFork()
                                                .callMethod("getItems", itemClass.arrayType(), new ParamSet0())
                                                .testValue(arr -> Util.objectToString(arr).equals(Util.objectToString(myList.getItems())), arr -> Util.objectToString(arr), Util.objectToString(myList.getItems()))
                                                .runTest();
                                        
                                        maker.hardInstanceFork()
                                                .callMethod("getItems", itemClass.arrayType(), new ParamSet0())
                                                .testValue(returned -> (returned != itemsField), ret -> "Same Reference! You may not return the \"items\" attribute!", "A New Array!")
                                                .runTest();
                                    });
                            
                            maker.hardInstanceFork()
                                    .callMethod("remove", boolean.class, new ParamSet1<>(0))
                                    .testValue(true)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("hasGaps", boolean.class, new ParamSet0())
                                    .testValue(true)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("remove", boolean.class, new ParamSet1<>(0))
                                    .testValue(false)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("set", itemClass, new ParamSet2<>(itemClass, int.class, null, -1))
                                    .testValue(null)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("set", itemClass, new ParamSet2<>(itemClass, int.class, null, size))
                                    .testValue(null)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("get", itemClass, new ParamSet1<>(-1))
                                    .testValue(null)
                                    .runTest();
                            
                            maker.hardInstanceFork()
                                    .callMethod("get", itemClass, new ParamSet1<>(size))
                                    .testValue(null)
                                    .runTest();
                        });
                    });
        }
    }
    
    @Test
    public void testErweiterung()
    {
        Engine.ENGINE.requiresDoesNotFail(this::testMain, "Part 1 must pass first.");
    }
    
    private static class TestItem
    {
        private String description;
        private String owner;
        
        public TestItem(String description, String owner)
        { //parameters maybe null
            this.description = description;
            this.owner = owner;
        }
        
        public String getDescription()
        {
            return description;
        }
        
        public void setDescription(String description)
        {
            this.description = description;
        }
        
        public String getOwner()
        {
            return owner;
        }
        
        public void setOwner(String owner)
        {
            this.owner = owner;
        }
        
        @Override
        public String toString()
        {
            return description + " (" + owner + ")";
        }
    }
    
    private static class TestItemList
    {
        
        private TestItem[] items;
        private int size;
        
        public TestItemList(int capacity)
        {
            items = new TestItem[capacity];
        }
        
        public int add(TestItem item)
        {
            if(item != null && !isFull())
            {
                for(int i = 0; i < items.length; i++)
                {
                    if(items[i] == null)
                    {
                        items[i] = item;
                        size++;
                        return i;
                    }
                }
            }
            return -1;
        }
        
        public int add(String description, String owner)
        {
            return add(new TestItem(description, owner));
        }
        
        private boolean isValidIdx(int idx)
        {
            return idx >= 0 && idx < items.length;
        }
        
        public boolean remove(int idx)
        {
            if(!isValidIdx(idx) || items[idx] == null)
                return false;
            
            items[idx] = null;
            size--;
            return true;
        }
        
        public String toString()
        {
            String s = "";
            for(int i = 0; i < items.length; i++)
            {
                TestItem n = items[i];
                s += (n == null) ? "<empty>" : n.getDescription() + " (" + n.getOwner() + ")";
                if(i < items.length - 1)
                    s += ", ";
            }
            return s;
        }
        
        public int getCapacity()
        {
            return items.length;
        }
        
        public int getSize()
        {
            return size;
        }
        
        public boolean isFull()
        {
            return size == items.length;
        }
        
        public boolean isEmpty()
        {
            return size == 0;
        }
        
        public void clear()
        {
            for(int i = 0; i < items.length; i++)
            {
                remove(i);
            }
        }
        
        public TestItem set(TestItem item, int idx)
        {
            if(!isValidIdx(idx))
                return null;
            
            TestItem oldMaybeNull = items[idx];
            if(item == null) //remove (may decrease size)
                remove(idx);
            else
            { //replace (may increase size)
                items[idx] = item;
                if(oldMaybeNull == null)
                    size++;
            }
            return oldMaybeNull;
        }
        
        public TestItem get(int idx)
        {
            if(!isValidIdx(idx))
                return null;
            
            return items[idx];
        }
        
        public int getFirstIndex(TestItem item)
        {
            for(int i = 0; i < items.length; i++)
                if(item == items[i])
                    return i;
            return -1;
        }
        
        public TestItem[] getItems()
        { //may return an array of size 0
            TestItem[] retVal = new TestItem[size];
            int idx = 0;
            for(TestItem n : items)
            {
                if(n != null)
                    retVal[idx++] = n;
            }
            return retVal;
        }
        
        public boolean hasGaps()
        {
            if(isFull() || isEmpty())
                return false;
            
            for(int i = size; i < items.length; i++)
            {
                if(items[i] != null)
                    return true;
            }
            return false;
        }
        
        //Task 2
        public void enlarge()
        {
            TestItem[] newItems = new TestItem[items.length * 2];
            for(int i = 0; i < items.length; i++)
                newItems[i] = items[i];
            items = newItems;
        }
        
        /* **** NOT part of the assignment (discuss in class: == vs. equals) */
        public boolean contains(TestItem item)
        {
            for(TestItem n : items)
            {
                if(n != null && n.equals(item))
                {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static class TestExtendedItemList extends TestItemList
    {
        private final String title;
        
        public TestExtendedItemList(String title, int capacity)
        {
            super(capacity);
            this.title = title;
        }
        
        @Override
        public String toString()
        {
            return title + ": " + super.toString();
        }
        
        @Override
        public int add(TestItem item)
        {
            int idx = super.add(item);
            if(idx != -1 && isFull())
                enlarge();
            return idx;
        }
        
        public boolean compact()
        {
            if(!hasGaps())
                return false;
            
            int capacity = getCapacity();
            int writeIdx = 0;
            for(int readIdx = 0; readIdx < capacity; readIdx++)
            {
                TestItem curItem = get(readIdx);
                if(curItem != null)
                {
                    set(curItem, writeIdx++);
                }
            }
            while(writeIdx < capacity)
            {
                set(null, writeIdx++);
            }
            return true;
        }
    }
}
