package Model.Menu;

import Persistence.Entity;

public class MenuItem extends Entity {

    private String name;
    private String description;
    private double price;

    public MenuItem(String name, String description, double price)
    {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public MenuItem copyMenuItem()
    {
        MenuItem menuItem = new MenuItem(name, description, price);
        return menuItem;
    }

    @Override
    //TODO: check how that works
    public String toString() {

        // format room rate
        String stringPrice = String.format("%.2f",price);

        return    "\n-------------Menu Item-------------"
                + "\nName             : " + name
                + "\nDescription      : " + description
                + "\nPrice            : " + stringPrice;
    }

}
