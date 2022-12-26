package einfprog;

public class ItemList {
    
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
