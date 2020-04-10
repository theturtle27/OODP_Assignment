package ref;

public class MenuItem {
	
	private String name;
	private String description;
	private double price;
	//
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
	
	/*
	//test method
	public String toString()
	{
		return  "----MENU ITEM----"
				+ "\nname: " + name
				+ "\ndescription: " + description
				+ "\nprice: " + price;
	}*/

}
