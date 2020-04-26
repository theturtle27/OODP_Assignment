package ref;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RoomServiceOrder {

	private ArrayList<OrderItem> order;
	private Date orderTime;
	private RoomServiceOrderStatus roomServiceOrderStatus;
	//TODO: are these necessary?
	private double totalPrice;
	private String roomNumber;
	//
	public RoomServiceOrder(String roomNumber)
	{
		order = new ArrayList<OrderItem>();
		totalPrice = 0;
		this.roomNumber = roomNumber;
		this.roomServiceOrderStatus = RoomServiceOrderStatus.ORDERING;
		//TODO: problem with date
		this.orderTime = new Date();
	}
	//
	public void addOrderItem(OrderItem orderItem)
	{
		order.add(orderItem);
		
		MenuItem menuItem = orderItem.getMenuItem();

		//TODO: is this necessary
		totalPrice += menuItem.getPrice();
	}
	//
	public void removeOrderItem(int index)
	{
		OrderItem orderItem = order.get(index);
		MenuItem menuItem = orderItem.getMenuItem();
		
		//TODO: is this necessary
		totalPrice -= menuItem.getPrice();
		
		order.remove(index);
	}
	//
	public void setOrderItemRemark(int index, String orderItemRemark)
	{
		OrderItem orderItem = order.get(index);
		orderItem.setRemark(orderItemRemark);
	}
	//
	public void confirmRoomServiceOrder()
	{
		// sets date and time to now
		orderTime = new Date();
		this.roomServiceOrderStatus = RoomServiceOrderStatus.CONFIRMED;
	}
	//
	public ArrayList<OrderItem> getOrderItems()
	{
		return order;
	}
	//
	public void setRoomServiceOrderStatus(RoomServiceOrderStatus roomServiceOrderStatus)
	{
		this.roomServiceOrderStatus = roomServiceOrderStatus;
	}
	//
	public RoomServiceOrderStatus getRoomServiceOrderStatus()
	{
		return roomServiceOrderStatus;
	}
	
	public double getTotalPrice()
	{
		return totalPrice;
	}
	
	public String getRoomNumber()
	{
		return roomNumber;
	}
	
	public String getOrderTime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");      
		return formatter.format(orderTime);
	}
	
	/*
	//TODO: test methods
	public String toString()
	{
	    int i = 1;
		String results = "----ROOM SERVICE ORDER----\n";
	    for(OrderItem orderItem : order) {
	        results += "----ID" + i + "----\n" + orderItem.toString() + "\n"; //if you implement toString() for Dog then it will be added here
	        i++;
	    }
	    return results
	    		+ "orderTime: " + orderTime
	    		+ "\nRoomServiceOrderStatus: " + roomServiceOrderStatus
	    		+ "\ntotal price: " + totalPrice
	    		+ "\nroom number: " + roomNumber;
	}
	
	public static void main(String args[])
	{
		MenuItem menuItem = new MenuItem("Classic Smash", "6 oz. beef, american cheese, onion, lettuce, mayo, mustard & pickle", 7.5);
		//OrderItem orderItem = new OrderItem(menuItem, "less salt");
		//System.out.println(orderItem.toString());
		RoomServiceOrder roomServiceOrder = new RoomServiceOrder("202");
		roomServiceOrder.addOrderItem(menuItem, "less salt");
		roomServiceOrder.addOrderItem(menuItem, "less salt");
		roomServiceOrder.confirmRoomServiceOrder();
		System.out.println(roomServiceOrder.toString());
	}*/


}

