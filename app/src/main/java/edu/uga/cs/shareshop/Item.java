package edu.uga.cs.shareshop;

public class Item {
    private String name;
    private String purchaser;
    private boolean isPurchased;
    private double price;
    private String priority;

    public Item() {
        this.name = null;
        this.purchaser = null;
        this.isPurchased = false;
        this.price = 0;
        this.priority = null;
    }

    public Item(String name, String priority) {
        this.name = name;
        this.purchaser = null;
        this.isPurchased = false;
        this.price = 0;
        this.priority = priority;
    }

    public Item(String name, String purchaser, boolean isPurchased, double price, String priority) {
        this.name = name;
        this.purchaser = purchaser;
        this.isPurchased = isPurchased;
        this.price = price;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurchaser() {
        return this.purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public boolean getIsPurchased() {
        return this.isPurchased;
    }

    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /*
        Return the priority value of the item.
        0 - Undefined
        1 - Please
        2 - Wanted
        3 - Urgency
     */
    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
