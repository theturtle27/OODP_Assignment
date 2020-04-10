package ref;

import java.util.ArrayList;

public class Menu {

	private ArrayList<MenuItem> menu;
	//
	public Menu()
	{
		menu = new ArrayList<MenuItem>();
	}
	//
	public void addMenuItem(MenuItem menuItem)
	{
		menu.add(menuItem);
	}
	//
	public void removeMenuItem(int index)
	{
		menu.remove(index);
	}
	//
	public void setMenuItemPrice(int index, double price)
	{
		MenuItem menuItem = menu.get(index);
		menuItem.setPrice(price);
	}
	//
	public MenuItem getMenuItem(int index)
	{
		return menu.get(index);
	}
	//
	public ArrayList<MenuItem> getMenu()
	{
		return menu;
	}
	
	/*
	// TODO: test method
	public String toString()
	{
		int i = 1;
		String results = "----MENU----\n";
		for(MenuItem menuItem : menu) {
			results += "----ID " + i + "----\n" + menuItem.toString() + "\n";
			i++;
		}
		return results;
	}*/
}
