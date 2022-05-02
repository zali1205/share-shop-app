package edu.uga.cs.shareshop;

import java.io.Serializable;

/**
 * This class represents a single Item, including the name, purchaser (who purchased it), isPurchased (whether it has been
 * purchased or not), price, priority, and the detail (aka description). It also implements Serializable which allows
 * it to be transferred between activities and fragments.
 */
public class Item implements Serializable {

    // Private variables
    private String name;
    private String purchaser;
    private boolean isPurchased;
    private double price;
    private String priority;
    private String detail;

    /*
        Default constructor
     */
    public Item() {
        this.name = null;
        this.purchaser = null;
        this.isPurchased = false;
        this.price = 0;
        this.priority = null;
        this.detail = null;
    }

    /*
        Constructor used for when the item is first added into the database.
     */
    public Item(String name, String priority, String detail) {
        this.name = name;
        this.purchaser = null;
        this.isPurchased = false;
        this.price = 0;
        this.priority = priority;
        this.detail = detail;
    }

    /*
        Constructor which is used when all of the values are known.
     */
    public Item(String name, String purchaser, boolean isPurchased, double price, String priority, String detail) {
        this.name = name;
        this.purchaser = purchaser;
        this.isPurchased = isPurchased;
        this.price = price;
        this.priority = priority;
        this.detail = detail;
    }

    /*
        Getter for Name.
     */
    public String getName() {
        return this.name;
    }

    /*
        Setter for Name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
        Getter for Purchaser.
     */
    public String getPurchaser() {
        return this.purchaser;
    }

    /*
        Setter for Purchaser.
     */
    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    /*
        Getter for isPurchased.
     */
    public boolean getIsPurchased() {
        return this.isPurchased;
    }

    /*
        Setter for isPurchased.
     */
    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    /*
        Getter for Price.
     */
    public double getPrice() {
        return this.price;
    }

    /*
        Setter for Price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /*
        Getter for Priority.
        1 - Please
        2 - Wanted
        3 - Urgent
     */
    public String getPriority() {
        return this.priority;
    }

    /*
        Setter for Priority.
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /*
        Getter for Detail.
     */
    public String getDetail() {
        return this.detail;
    }

    /*
        Setter for Detail.
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
