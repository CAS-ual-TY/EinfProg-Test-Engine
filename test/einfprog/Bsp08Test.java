package einfprog;

import einfprog.test_engine.Engine;
import einfprog.test_engine.TestMaker;
import einfprog.test_engine.params.ParamSet0;
import einfprog.test_engine.params.ParamSet1;
import einfprog.test_engine.params.ParamSet2;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

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
    
        public TestItem(String description, String owner) { //parameters maybe null
            this.description = description;
            this.owner = owner;
        }
    
        public String getDescription() {
            return description;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }
    
        public String getOwner() {
            return owner;
        }
    
        public void setOwner(String owner) {
            this.owner = owner;
        }
    
        @Override
        public String toString() {
            return description + " (" + owner + ")";
        }
    }
    
    private static class ItemList {
        
        private Item[] items;
        private int size;
        
        public ItemList(int capacity) {
            items = new Item[capacity];
        }
        
        public int add(Item item) {
            if (item != null && !isFull()) {
                for (int i = 0; i < items.length; i++) {
                    if (items[i] == null) {
                        items[i] = item;
                        size++;
                        return i;
                    }
                }
            }
            return -1;
        }
        
        public int add(String description, String owner) {
            return add(new Item(description, owner));
        }
        
        private boolean isValidIdx(int idx) {
            return idx >= 0 && idx < items.length;
        }
        
        public boolean remove(int idx) {
            if (!isValidIdx(idx) || items[idx] == null)
                return false;
            
            items[idx] = null;
            size--;
            return true;
        }
        
        public String toString() {
            String s = "";
            for (int i = 0; i < items.length; i++) {
                Item n = items[i];
                s += (n == null) ? "<empty>" : n.getDescription() + " (" + n.getOwner() + ")";
                if (i < items.length - 1)
                    s += ", ";
            }
            return s;
        }
        
        public int getCapacity() {
            return items.length;
        }
        
        public int getSize() {
            return size;
        }
        
        public boolean isFull() {
            return size == items.length;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public void clear() {
            for (int i = 0; i < items.length; i++) {
                remove(i);
            }
        }
        
        public Item set(Item item, int idx) {
            if (!isValidIdx(idx))
                return null;
            
            Item oldMaybeNull = items[idx];
            if (item == null) //remove (may decrease size)
                remove(idx);
            else { //replace (may increase size)
                items[idx] = item;
                if (oldMaybeNull == null)
                    size++;
            }
            return oldMaybeNull;
        }
        
        public Item get(int idx) {
            if (!isValidIdx(idx))
                return null;
            
            return items[idx];
        }
        
        public int getFirstIndex(Item item) {
            for (int i = 0; i < items.length; i++)
                if (item == items[i])
                    return i;
            return -1;
        }
        
        public Item[] getItems() { //may return an array of size 0
            Item[] retVal = new Item[size];
            int idx = 0;
            for (Item n : items) {
                if (n != null)
                    retVal[idx++] = n;
            }
            return retVal;
        }
        
        public boolean hasGaps() {
            if (isFull() || isEmpty())
                return false;
            
            for (int i = size; i < items.length; i++) {
                if (items[i] != null)
                    return true;
            }
            return false;
        }
        
        //Task 2
        public void enlarge() {
            Item[] newItems = new Item[items.length * 2];
            for (int i = 0; i < items.length; i++)
                newItems[i] = items[i];
            items = newItems;
        }
        
        /* **** NOT part of the assignment (discuss in class: == vs. equals) */
        public boolean contains(Item item) {
            for (Item n : items) {
                if (n != null && n.equals(item)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static class ExtendedItemList extends ItemList
    {
        private final String title;
    
        public ExtendedItemList(String title, int capacity) {
            super(capacity);
            this.title = title;
        }
    
        @Override
        public String toString() {
            return title + ": " + super.toString();
        }
    
        @Override
        public int add(Item item) {
            int idx = super.add(item);
            if (idx != -1 && isFull())
                enlarge();
            return idx;
        }
    
        public boolean compact() {
            if (!hasGaps())
                return false;
        
            int capacity = getCapacity();
            int writeIdx = 0;
            for (int readIdx = 0; readIdx < capacity; readIdx++) {
                Item curItem = get(readIdx);
                if (curItem != null) {
                    set(curItem, writeIdx++);
                }
            }
            while (writeIdx < capacity) {
                set(null, writeIdx++);
            }
            return true;
        }
    }
}
