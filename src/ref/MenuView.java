package ref;

import java.util.Scanner;
import java.util.ArrayList;

public class MenuView {

	// constants defining layout of menu
	private static final int MENU_WIDTH = 50;
	private static final int PRICE_WIDTH = 7;
	private static final int NAME_PRICE_WIDTH = 5;
	
	private final Scanner sc;
	private final MenuController menuController;
	
	public MenuView(MenuController menuController)
	{
		sc = new Scanner(System.in);
		this.menuController = menuController;
	}
	
	public void displayOtions()
	{
		int option;

		do
		{

			printMenu(menuController.getMenu());
			System.out.println();
			
			System.out.println("|=======================================|");
			System.out.println("|                                       |");
			System.out.println("|              Manage Menu              |");
			System.out.println("|                                       |");
			System.out.println("|---------------------------------------|");
			System.out.println("|                                       |");
			System.out.println("| [1] Add a menu item to the menu       |");
			System.out.println("| [2] Update the price of a menu item   |");
			System.out.println("| [3] Remove a menu item from the menu  |");
			System.out.println("| [4] Exit                              |");
			System.out.println("|                                       |");
			System.out.println("|=======================================|");
			System.out.println();
			System.out.print("Please select an option: ");

			option = Integer.parseInt(sc.nextLine());
			
			
			menuController.manageMenu(option, this);
			
		}while(option != 4);
	}
	
	public String getInputString(String OutputMessage)
	{
		System.out.print(OutputMessage);
		return sc.nextLine();		
	}
	
	public Double getInputDouble(String OutputMessage)
	{
		System.out.print(OutputMessage);
		return Double.parseDouble(sc.nextLine());
	}
	
	public Integer getInputInteger(String OutputMessage)
	{
		System.out.print(OutputMessage);
		return Integer.parseInt(sc.nextLine());
	}
	
	public void displayText(String OutputMessage)
	{
		System.out.print(OutputMessage);
	}
	
	public void printMenu(Menu menu)
	{
		// define different types of lines
		String doubleLine = buildString('=',MENU_WIDTH);
		String singleLine = buildString('-', MENU_WIDTH);
		String spaceLine = buildString(' ', MENU_WIDTH);

		// print menu header
		System.out.println();
		System.out.printf("|%s|\n", doubleLine);
		System.out.printf("|%s|\n", spaceLine);
		if(MENU_WIDTH % 2 == 0)
		{
			System.out.printf("|%sMENU%s|\n",spaceLine.substring(0,spaceLine.length()/2-2),spaceLine.substring(0,spaceLine.length()/2-2));
		}
		else
		{
			System.out.printf("|%sMENU%s|\n",spaceLine.substring(0,spaceLine.length()/2-1),spaceLine.substring(0,spaceLine.length()/2-2));
		}
		System.out.printf("|%s|\n", spaceLine);
		System.out.printf("|%s|\n", singleLine);

		int iterator = 1;

		// print menu items
		for(MenuItem menuItem : menu.getMenu())
		{
			//break name of MenuItem into multiple lines if necessary
			ArrayList<String> nameMenuItem = breakString(menuItem.getName(), MENU_WIDTH-PRICE_WIDTH-NAME_PRICE_WIDTH-7);

			// print name and price of MenuItem
			System.out.printf("|%s|\n",spaceLine);
			System.out.printf("| %3d.%-" + (MENU_WIDTH-PRICE_WIDTH-7) + "S %" + PRICE_WIDTH + ".2f |\n", iterator, nameMenuItem.get(0), menuItem.getPrice());
			for(int i = 1; i < nameMenuItem.size(); i++)
			{
				System.out.printf("|     %-" + (MENU_WIDTH-5) + "S|\n", nameMenuItem.get(i));
			}
			System.out.printf("|%s|\n",spaceLine);

			//break description of MenuItem into multiple lines if necessary
			ArrayList<String> descriptionMenuItem = breakString(menuItem.getDescription(),MENU_WIDTH-PRICE_WIDTH-NAME_PRICE_WIDTH-7);

			// print description of MenuItem
			for(String string : descriptionMenuItem)
			{
				System.out.printf("|     %-" + (MENU_WIDTH-5) + "s|\n", string);
			}
			System.out.printf("|%s|\n",spaceLine);

			iterator++;

		}

		// print menu footer
		System.out.printf("|%s|\n",spaceLine);
		System.out.printf("|%s|\n", doubleLine);
		System.out.println();
	}

	// create a String which is made up of one char being repeated a given amount of times
	private String buildString(char stringChar,int stringLength)
	{
		StringBuilder sb = new StringBuilder(stringLength);
		for (int i=0; i < stringLength; i++){
			sb.append(stringChar);
		}

		return sb.toString();
	}

	// break String into multiple lines
	private ArrayList<String> breakString(String string, int stringLength)
	{	
		// container to save the multiple strings
		ArrayList<String> stringLines = new ArrayList<String>();

		// starting position of temporary String
		int j = 0;

		// indicates where to split the String
		int split = 0;

		// iterate through each Char of the given string
		for(int i = 0; i < string.length(); i++)
		{
			// create temporary String containing all Chars of one line 
			String temp = string.substring(j, i+1);

			// if the newly added char is a ' ', then save the location of that space
			if(temp.charAt(temp.length()-1) == ' ')
			{
				split = temp.length()-1;
			}

			// check whether the String has reached a certain length
			if(temp.length() == stringLength-2)
			{
				// String is added to the container
				stringLines.add(" " + temp.substring(0, split));

				// starting position of the next line's String
				j += split+1;

				// reset int indicating where to split String
				split = 0;
			}
		}
		// add remaining part of String into new line
		stringLines.add(" " + string.substring(j, string.length()));

		// return the String broken up into multiple lines
		return stringLines;
	}

}
