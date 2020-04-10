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
		int option;
		
		do
		{
			System.out.println("|================================================|");
			System.out.println("|                                                |");
			System.out.println("|           Manage Room Service Orders           |");
			System.out.println("|                                                |");
			System.out.println("|------------------------------------------------|");
			System.out.println("|                                                |");
			System.out.println("| [1] Add a room service order                   |");
			System.out.println("| [2] Update the status of a room service order  |");
			System.out.println("| [3] Remove a room service order                |");
			System.out.println("| [4] Exit                                       |");
			System.out.println("|                                                |");
			System.out.println("|================================================|");
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
		
		//TODO: get rid of first enum option (ORDERING)
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
			System.out.printf("|%sROOM SERVICE ORDERS%s|\n",spaceLine.substring(0,spaceLine.length()/2-10),spaceLine.substring(0,spaceLine.length()/2-9));
		}
		else
		{
			System.out.printf("|%sROOM SERVICE ORDERS%s|\n",spaceLine.substring(0,spaceLine.length()/2-9),spaceLine.substring(0,spaceLine.length()/2-9));
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

}
