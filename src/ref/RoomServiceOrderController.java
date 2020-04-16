package ref;

import java.util.Scanner;

public class RoomServiceOrderController {

	private final RoomServiceOrder roomServiceOrder;
	private final Menu menu;
	private final MenuView menuView;

	public RoomServiceOrderController(RoomServiceOrder roomServiceOrder, Menu menu, MenuView menuView)
	{
		this.roomServiceOrder = roomServiceOrder;
		this.menu = menu;
		this.menuView = menuView;
	}

	public boolean checkRoomCheckedIn(RoomServiceOrderView roomServiceOrderView)
	{
		String roomNumber = roomServiceOrderView.getInputString("\nEnter the room number: ");
		roomServiceOrderView.displayText("\n\n\n\n\n");
		
		//TODO: extend this code
		//check whether room is checked in
		boolean roomCheckedIn = true;
		
		if(!roomCheckedIn)
		{
			roomServiceOrderView.displayText("Room is not checked in.");
			return false;
		}
		return true;
	}
	
	public void manageRoomServiceOrder(int option, RoomServiceOrderView roomServiceOrderView)
	{

		switch(option)
		{
			case 1: addOrderItem(roomServiceOrderView);
					break;

			case 2: setOrderItemRemark(roomServiceOrderView);
					break;

			case 3: removeOrderItem(roomServiceOrderView);
					break;
			case 4: exitRoomServiceOrderEditor(roomServiceOrderView);
					break;
			default:roomServiceOrderView.displayText("\nThis option is not available. Please enter a number between [1] and [4].\n\n\n\n\n");
					break;

		}
	}
	
	private void addOrderItem(RoomServiceOrderView roomServiceOrderView)
	{
		menuView.printMenu(menu);
		int menuItemIndex = roomServiceOrderView.getInputInteger("\nEnter the number of the menu item: ");
		MenuItem menuItem = menu.getMenuItem(menuItemIndex-1);
		String orderRemark = roomServiceOrderView.getInputString("Enter an optional remark for the menu item: ");
		OrderItem orderItem = new OrderItem(menuItem, orderRemark);
		roomServiceOrder.addOrderItem(orderItem);
		roomServiceOrderView.displayText("\nThe menu item has been added to the order.\n");
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		roomServiceOrderView.displayText("\n\n\n\n");
	}
	
	private void setOrderItemRemark(RoomServiceOrderView roomServiceOrderView)
	{
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		int orderItemIndex = roomServiceOrderView.getInputInteger("Enter the number of the order item: ");
		String orderItemRemark = roomServiceOrderView.getInputString("Enter the updated remark for the order item: ");
		roomServiceOrder.setOrderItemRemark(orderItemIndex-1, orderItemRemark);
		roomServiceOrderView.displayText("\nThe remark of the order item has been updated.\n");
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		roomServiceOrderView.displayText("\n\n\n\n");
	}
	
	private void removeOrderItem(RoomServiceOrderView roomServiceOrderView)
	{
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		int orderItemIndex = roomServiceOrderView.getInputInteger("Enter the number of the order item: ");
		roomServiceOrder.removeOrderItem(orderItemIndex-1);
		roomServiceOrderView.displayText("\nThe order item has been removed from the order.\n");
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		roomServiceOrderView.displayText("\n\n\n\n");
	}
	
	private void exitRoomServiceOrderEditor(RoomServiceOrderView roomServiceOrderView)
	{
		if(roomServiceOrder.getTotalPrice() != 0)
		{
			roomServiceOrder.confirmRoomServiceOrder();
			roomServiceOrderView.displayText("\nThe order has been placed.\n");
		}
		else
		{
			roomServiceOrderView.displayText("\nThe order is empty and will not be placed.\n\n");
		}
		roomServiceOrderView.printRoomServiceOrder(roomServiceOrder);
		//roomServiceOrderView.displayText("\n\n\n\n");
	}
	
	
	
	

}