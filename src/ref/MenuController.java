package ref;

public class MenuController {
	
	private Menu menu;
	
	public MenuController(Menu menu)
	{
		this.menu = menu;
	}
	
	public void manageMenu(int option, MenuView menuView)
	{
		
		switch(option)
		{
			case 1: addMenuItem(menuView);
					break;
				
			case 2: setMenuItemPrice(menuView);
					break;
					
			case 3: removeMenuItem(menuView);
					break;
					
			case 4: exitMenuEditor(menuView);
					break;
					
			default:menuView.displayText("\nThis option is not available. Please enter a number between [1] and [4].\n\n\n\n\n\n");
					break;
		}
	}
	
	private void addMenuItem(MenuView menuView)
	{
		String menuItemName = menuView.getInputString("\nEnter the name of the menu item: ");
		String menuItemDescription = menuView.getInputString("Enter the description of the menu item: ");
		double menuItemPrice = menuView.getInputDouble("Enter the price of the menu item: ");
		MenuItem menuItem = new MenuItem(menuItemName, menuItemDescription, menuItemPrice);
		menu.addMenuItem(menuItem);
		menuView.displayText("\nThe menu item has been added to the menu.\n");
		menuView.printMenu(menu);
		menuView.displayText("\n\n\n\n");
	}
	
	private void setMenuItemPrice(MenuView menuView)
	{
		menuView.printMenu(menu);
		int menuItemIndex = menuView.getInputInteger("Enter the number of the menu item: "); 
		double menuItemPrice = menuView.getInputDouble("Enter the new price of the menu item: ");
		menu.setMenuItemPrice(menuItemIndex-1, menuItemPrice);
		menuView.displayText("\nThe price of the menu item has been changed.\n");
		menuView.printMenu(menu);
		menuView.displayText("\n\n\n\n");
	}
	
	private void removeMenuItem(MenuView menuView)
	{
		menuView.printMenu(menu);
		int menuItemIndex = menuView.getInputInteger("Enter the number of the menu item: ");
		menu.removeMenuItem(menuItemIndex-1);
		menuView.displayText("\nThe menu item has been removed from the menu.\n");
		menuView.printMenu(menu);
		menuView.displayText("\n\n\n\n");
	}
	
	private void exitMenuEditor(MenuView menuView)
	{
		menuView.displayText("\nThe menu has been changed successfully.\n");
		menuView.printMenu(menu);
		menuView.displayText("\n\n\n\n");
	}
	
	public Menu getMenu()
	{
		return menu;
	}

}
