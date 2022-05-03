package edu.uga.cs.shareshop;

public class User {
    // Private variables
    private String name;
    private Double total;

    /*
        Default constructor
     */
    public User() {
        this.name = null;
        this.total = 0.0;
    } // User()

    /*
        Constructor used for adding name and first total
     */
    public User(String name, Double total) {
        this.name = name;
        this.total = total;
    } // User(string, double)


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
        Getter for Total.
     */
    public Double getTotal() {
        return this.total;
    }

    /*
        Setter for Total.
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /*
        Updater for Total.
     */
    public void addTotal(Double total) {
        this.total = this.total + total;
    }

} // User
