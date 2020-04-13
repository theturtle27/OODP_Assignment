package Model.Menu;

import Persistence.Entity;

public class MenuItem extends Entity {

    private String name;
    private String description;
    private double price;
    //

    protected MenuItem() {
        this.name = null;
        this.description = null;
        this.price = 0;
    }

    public MenuItem(String name, String description, double price)
    {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    //
    public String getName() {
        return name;
    }
    //
    public String getDescription() {
        return description;
    }
    //
    public double getPrice() {
        return price;
    }
    //
    public void setPrice(double price)
    {
        this.price = price;
    }
    //
    public MenuItem copyMenuItem()
    {
        MenuItem menuItem = new MenuItem(name, description, price);
        return menuItem;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Name: " + this.getName() + "\n" +
                "description: " + this.getDescription() + "\n" +
                "price: " + this.getPrice() + "\n";
    }

}
