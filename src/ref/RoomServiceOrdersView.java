package ref;

import java.util.Scanner;
import java.util.ArrayList;

public class RoomServiceOrdersView {
	
	// constants defining layout of menu
	private static final int MENU_WIDTH = 50;
	private static final int PRICE_WIDTH = 7;
	private static final int NAME_PRICE_WIDTH = 5;
	
	private final Scanner sc;
	private final RoomServiceOrdersController roomServiceOrdersController;
	
	public RoomServiceOrdersView(RoomServiceOrdersController roomServiceOrdersController)
	{
		sc = new Scanner(System.in);
		this.roomServiceOrdersController = roomServiceOrdersController;
	}
	
	public void displayOptions()
	{
		//TODO: only show if non-empty
		printRoomServiceOrders(roomServiceOrdersController.getRoomServiceOrders());
		
		int option;
		
		do
		{
			
			System.out.println("|===============================================|");
			System.out.println("|                                               |");
			System.out.println("|   Manage the Summary of Room Service Orders   |");
			System.out.println("|                                               |");
			System.out.println("|-----------------------------------------------|");
			System.out.println("|                                               |");
			System.out.println("| [1] Add a room service order                  |");
			System.out.println("| [2] Update the status of a room service order |");
			System.out.println("| [3] Remove a room service order               |");
			System.out.println("| [4] Exit                                      |");
			System.out.println("|                                               |");
			System.out.println("|===============================================|");
			System.out.println();
			System.out.print("Please select an option: ");

			option = Integer.parseInt(sc.nextLine());
			
			roomServiceOrdersController.manageRoomServiceOrders(option, this);

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
	
	public boolean getInputBoolean(String OutputMessage)
	{
		System.out.print(OutputMessage);
		return Boolean.parseBoolean(sc.nextLine());
	}
	
	public void displayText(String OutputMessage)
	{
		System.out.print(OutputMessage);
	}
	
	public void printRoomServiceOrderStatus()
	{
		System.out.println("Update the status of the room service order to: ");
		
		int iterator = 0;
		for (RoomServiceOrderStatus roomServiceOrderStatus : RoomServiceOrderStatus.values()) { 
		    if(iterator>0)
		    {
		    	System.out.println(iterator + ". " + roomServiceOrderStatus);
		    }
		    iterator++;
		}
		System.out.println();
	}
	
	public void printRoomServiceOrders(ArrayList<RoomServiceOrder> roomServiceOrders)
	{
		
		//check whether there are room service orders in the array
		if(roomServiceOrders.size() == 0)
		{
			//do not print
			return;
		}
		
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
			System.out.printf("|%sSUMMARY OF ROOM SERVICE ORDERS%s|\n",spaceLine.substring(0,spaceLine.length()/2-15),spaceLine.substring(0,spaceLine.length()/2-15));
		}
		else
		{
			System.out.printf("|%sSUMMARY OF ROOM SERVICE ORDERS%s|\n",spaceLine.substring(0,spaceLine.length()/2-15),spaceLine.substring(0,spaceLine.length()/2-14));
		}
		System.out.printf("|%s|\n", spaceLine);
		System.out.printf("|%s|\n", singleLine);

		int iterator = 1;

		// print room service orders
		for(RoomServiceOrder roomServiceOrder : roomServiceOrders)
		{

			// print name and price of MenuItem
			System.out.printf("|%s|\n",spaceLine);
			System.out.printf("| %3d. %-" + (MENU_WIDTH-PRICE_WIDTH-15) + "S TOTAL: %" + PRICE_WIDTH + ".2f |\n", iterator, "Room Service Order", roomServiceOrder.getTotalPrice());
			
			System.out.printf("|%s|\n",spaceLine);
			
			//print names of menu items of each order
			int iteratorOrderItem = 1;
			
			for(OrderItem orderItem : roomServiceOrder.getOrderItems())
			{
				//get menu item
				MenuItem menuItem = orderItem.getMenuItem();
				
				//break name of MenuItem into multiple lines if necessary
				ArrayList<String> nameMenuItem = breakString(menuItem.getName(), MENU_WIDTH-PRICE_WIDTH-NAME_PRICE_WIDTH-9);
				
				//print name of menu item
				System.out.printf("|    %3d. %-" + (MENU_WIDTH-9) + "S|\n", iteratorOrderItem, nameMenuItem.get(0));
				
				for(int i = 1; i < nameMenuItem.size(); i++)
				{
					System.out.printf("|         %-" + (MENU_WIDTH-9) + "S|\n", nameMenuItem.get(i));
				}
				
				iteratorOrderItem++;
			}
			
			System.out.printf("|%s|\n",spaceLine);
			System.out.printf("|      STATUS      : %-" + (MENU_WIDTH-PRICE_WIDTH-13) + "s|\n", roomServiceOrder.getRoomServiceOrderStatus());
			System.out.printf("|      ROOM NUMBER : %-" + (MENU_WIDTH-PRICE_WIDTH-13) + "s|\n", roomServiceOrder.getRoomNumber());
			System.out.printf("|      ORDER TIME  : %-" + (MENU_WIDTH-PRICE_WIDTH-13) + "s|\n", roomServiceOrder.getOrderTime());

			System.out.printf("|%s|\n",spaceLine);


			iterator++;

		}
		
		// print room service footer
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
