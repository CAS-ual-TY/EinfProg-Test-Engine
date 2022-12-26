package einfprog;

public class Item {
    
    private String description;
    private String owner;
    
    public Item(String description, String owner) { //parameters maybe null
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
