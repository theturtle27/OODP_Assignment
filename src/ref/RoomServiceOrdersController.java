package ref;

import java.util.ArrayList;

public class RoomServiceOrdersController {
	
	private final ArrayList<RoomServiceOrder> roomServiceOrders;
	private final Menu menu;
	private final MenuView menuView;
	
	public RoomServiceOrdersController(ArrayList<RoomServiceOrder> roomServiceOrders, Menu menu, MenuView menuView)
	{
		this.roomServiceOrders = roomServiceOrders;
		this.menu = menu;
		this.menuView = menuView;
		
	}
	
	public void manageRoomServiceOrders(int option, RoomServiceOrdersView roomServiceOrdersView)
	{
		
		switch(option)
		{
			case 1: addRoomServiceOrder(roomServiceOrdersView);
					break;
			
			case 2: updateRoomServiceOrderStatus(roomServiceOrdersView);
					break;
					
			case 3: removeRoomServiceOrder(roomServiceOrdersView);
					break;
					
			case 4: exitRoomServiceOrdersEditor(roomServiceOrdersView);
					break;
				
			default:roomServiceOrdersView.displayText("\nThis option is not available. Please enter a number between [1] and [4].\n\n\n\n\n\n");
					break;
					
					
		}
	}
	
	public void addRoomServiceOrder(RoomServiceOrdersView roomServiceOrdersView)
	{
		//create new room service order
		RoomServiceOrder roomServiceOrder = new RoomServiceOrder("302754");
		
		//create new controller for new room service order
		RoomServiceOrderController roomServiceOrderController = new RoomServiceOrderController(roomServiceOrder, menu, menuView);
		
		//create new view for new controller
		RoomServiceOrderView roomServiceOrderView = new RoomServiceOrderView(roomServiceOrderController);
		
		//display the options for the room service order
		roomServiceOrderView.displayOptions();
		
		//add the room service order to the array of room service orders
		roomServiceOrders.add(roomServiceOrder);
		
		//print all existing room service orders
		roomServiceOrdersView.printRoomServiceOrders(roomServiceOrders);
	}
	
	public void updateRoomServiceOrderStatus(RoomServiceOrdersView roomServiceOrdersView)
	{
		// print all existing room service orders
		roomServiceOrdersView.printRoomServiceOrders(roomServiceOrders);
		
		// get index of the room service order for which the status needs to be updated
		int roomServiceOrderIndex = roomServiceOrdersView.getInputInteger("Enter the number of the room service order: ");
		
		// get room service order
		RoomServiceOrder roomServiceOrder = roomServiceOrders.get(roomServiceOrderIndex-1);
		
		// create a controller for the room service order
		RoomServiceOrderController roomServiceOrderController = new RoomServiceOrderController(roomServiceOrder, menu, menuView);
		
		// create a view for the room service order
		RoomServiceOrderView roomServiceOrderView = new RoomServiceOrderView(roomServiceOrderController);
		
		// print the room service order
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		
		// print the options for the room service order status
		roomServiceOrdersView.printRoomServiceOrderStatus();
		
		// get the updated room service order status
		int roomServiceOrderStatus = roomServiceOrdersView.getInputInteger("Enter the number of the updated room service order status: ");
		
		// set the updated room service order status
		roomServiceOrder.setRoomServiceOrderStatus(RoomServiceOrderStatus.values()[roomServiceOrderStatus-1]);
		
		// print the updated room service order status
		roomServiceOrdersView.displayText("\nThe status of the room service order has been changed to " + roomServiceOrder.getRoomServiceOrderStatus() + ".\n");
		
		// print the updated room service order
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		
		// print the updated list of room service orders
		roomServiceOrdersView.printRoomServiceOrders(roomServiceOrders);
		
		// print text to keep formatting consistent
		roomServiceOrderView.displayText("\n\n\n\n");
	}
	
	public void removeRoomServiceOrder(RoomServiceOrdersView roomServiceOrdersView)
	{
		// print all existing room service orders
		roomServiceOrdersView.printRoomServiceOrders(roomServiceOrders);
		
		// get index of the room service order which needs to be removed
		int roomServiceOrderIndex = roomServiceOrdersView.getInputInteger("Enter the number of the room service order: ");
		
		// get room service order
		RoomServiceOrder roomServiceOrder = roomServiceOrders.get(roomServiceOrderIndex-1);
		
		// create a controller for the room service order
		RoomServiceOrderController roomServiceOrderController = new RoomServiceOrderController(roomServiceOrder, menu, menuView);
				
		// create a view for the room service order
		RoomServiceOrderView roomServiceOrderView = new RoomServiceOrderView(roomServiceOrderController);
		
		// check whether the room service order status is confirmed.
		// If the room service order status is already preparing or delivered,
		// then the room service order cannot be removed
		if(roomServiceOrder.getRoomServiceOrderStatus() == RoomServiceOrderStatus.CONFIRMED)
		{
			// print the room service order
			roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
			
			// get the confirmation that this room service order should be removed
			boolean roomServiceOrderRemove = roomServiceOrdersView.getInputBoolean("Are you sure you want to remove this room service order([True] Yes, [False] No)? ");
			
			// remove the room service order, if the request is confirmed
			if(roomServiceOrderRemove)
			{
				// remove room service order from the list of room service orders
				roomServiceOrders.remove(roomServiceOrderIndex-1);
				
				// print the confirmation for the removal of the room service order
				roomServiceOrderView.displayText("\nThe room service order has been removed.\n");
			}
		}
		else
		{
			// print message that the room service order cannot be removed
			roomServiceOrdersView.displayText("\nThe room service order cannot be removed since the room service order is already " + roomServiceOrder.getRoomServiceOrderStatus() + ".\n");
		}
		// print the updated list of room service orders
		roomServiceOrdersView.printRoomServiceOrders(roomServiceOrders);
		
		// print text to keep formatting consistent
		roomServiceOrdersView.displayText("\n\n\n\n");
	}
	
	public void exitRoomServiceOrdersEditor(RoomServiceOrdersView roomServiceOrdersView)
	{
		// print message that all room service orders have been changed successfully
		roomServiceOrdersView.displayText("\nThe room service orders have been changed successfully.\n");
		
		// print the list of room service orders
		roomServiceOrdersView.printRoomServiceOrders(roomServiceOrders);
		
		// print text to keep formatting consistent
		roomServiceOrdersView.displayText("\n\n\n\n");
	}
	
	
	
	

}
