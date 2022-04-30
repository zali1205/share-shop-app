package edu.uga.cs.shareshop;

public class Item {
    private String name;
    private String purchaser;
    private boolean isPurchased;
    private double price;
    private int quantity;
    private int priority;

    public Item() {
        this.name = null;
        this.purchaser = null;
        this.isPurchased = false;
        this.price = 0;
        this.quantity = 0;
        this.priority = 0;
    }

    public Item(String name, String purchaser, boolean isPurchased, double price, int quantity, int priority) {
        this.name = name;
        this.purchaser = purchaser;
        this.isPurchased = isPurchased;
        this.price = price;
        this.quantity = quantity;
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*
        Return the priority value of the item.
        0 - Undefined
        1 - Please
        2 - Wanted
        3 - Urgency
     */
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
