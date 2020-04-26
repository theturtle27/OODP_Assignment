package ref;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args)
	{
		
		//TODO: retrieve from Persistence
		//-------------------------------
		
		
		//%%%%%%%%%%%%%%%%%%%%%%%%%
		//menu stored in persistence
		Menu menu  = new Menu();
		MenuItem menuItem1 = new MenuItem("Classic Smash", "6 oz. beef, american cheese, onion, lettuce, mayo, mustard & pickle", 7.5);
		menu.addMenuItem(menuItem1);
		MenuItem menuItem2 = new MenuItem("Burlington", "6 oz. beef, wisconsin cheddar sauce, twice-cooked onions, pickle relish, shredded lettuce & dijonnaise", 9);
		menu.addMenuItem(menuItem2);
		MenuItem menuItem3 = new MenuItem("Bitter South", "6 oz. beef, comeback sauce, hash browns, fried onions & emmentaler cheese on toasted rye", 5);
		menu.addMenuItem(menuItem3);
		MenuItem menuItem4 = new MenuItem("Green Street (V)", "impossible burger, white cheddar, parsley/shallot yogurt & lettuce on an english muffin", 10);
		menu.addMenuItem(menuItem4);
		MenuItem menuItem5 = new MenuItem("Fast Eddie", "loose meat, sharp cheddar, shredded lettuce & mayo mix on a new england-style roll", 8);
		menu.addMenuItem(menuItem5);
		
		//%%%%%%%%%%%%%%%%%%%%%%%%%
		//ArrayList of RoomServiceOrders for One Stay stored in persistence
		OrderItem orderItem1 = new OrderItem(menuItem3, "orderItem1");
		OrderItem orderItem2 = new OrderItem(menuItem1, "orderItem2");
		OrderItem orderItem3 = new OrderItem(menuItem4, "orderItem3");
		OrderItem orderItem4 = new OrderItem(menuItem5, "orderItem4");
		OrderItem orderItem5 = new OrderItem(menuItem2, "orderItem5");
		
		RoomServiceOrder roomServiceOrder1 = new RoomServiceOrder("202");
		roomServiceOrder1.addOrderItem(orderItem1);
		roomServiceOrder1.addOrderItem(orderItem4);
		roomServiceOrder1.confirmRoomServiceOrder();
		
		RoomServiceOrder roomServiceOrder2 = new RoomServiceOrder("305");
		roomServiceOrder2.addOrderItem(orderItem5);
		roomServiceOrder2.addOrderItem(orderItem2);
		roomServiceOrder2.addOrderItem(orderItem3);
		roomServiceOrder2.confirmRoomServiceOrder();
		
		ArrayList<RoomServiceOrder> roomServiceOrders = new ArrayList<RoomServiceOrder>();
		roomServiceOrders.add(roomServiceOrder2);
		roomServiceOrders.add(roomServiceOrder1);
		//-------------------------------------------------
			
		//Create menu controller
		MenuController menuController = new MenuController(menu);
		
		//Create a view : to write menu details on console
		MenuView menuView = new MenuView(menuController);
		
		//menu
		//menuView.displayOtions();
		
		//-------------------------------------------------
		
		//Create room service order controller
		//RoomServiceOrderController roomServiceOrderController = new RoomServiceOrderController(roomServiceOrder1, menu, menuView);
		
		//Create a view :  to write room service order details on console
		//RoomServiceOrderView roomServiceOrderView = new RoomServiceOrderView(roomServiceOrderController);
		
		//roomServiceOrderView.displayOptions();
		
		//-------------------------------------------------
		
		//Create room service orders controller
		/**
		 * roomServiceOrders and menu are part of the Persistence
		 */
		//TODO: menuView should not be passed into roomServiceOrdersController
		RoomServiceOrdersController roomServiceOrdersController = new RoomServiceOrdersController(roomServiceOrders, menu, menuView);
		
		//Create a view :  to write room service orders details on console
		RoomServiceOrdersView roomServiceOrdersView = new RoomServiceOrdersView(roomServiceOrdersController);
		
		roomServiceOrdersView.displayOptions();
		
	}

	
}
