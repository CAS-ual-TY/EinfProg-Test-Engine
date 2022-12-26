package einfprog;

public class ExtendedItemList extends ItemList {
    
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
