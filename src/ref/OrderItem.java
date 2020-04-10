package ref;

public class OrderItem {
	
	private MenuItem menuItem;
	private String orderRemark;
	//	
	public OrderItem(MenuItem menuItem, String orderRemark)
	{
		this.menuItem = menuItem.copyMenuItem();
		this.orderRemark = orderRemark;
	}
	//
	public void setRemark(String orderRemark)
	{
		this.orderRemark = orderRemark;
	}
	//
	public MenuItem getMenuItem()
	{
		return menuItem;
	}
	//
	public String getOrderRemark()
	{
		return orderRemark;
	}
	
	//test method
	public String toString()
	{
		return "----ORDER ITEM----\n"
				+ menuItem.toString()
				+ "\norder remark: " + orderRemark;
	}
	
	public static void main(String args[])
	{
		MenuItem menuItem = new MenuItem("Classic Smash", "6 oz. beef, american cheese, onion, lettuce, mayo, mustard & pickle", 7.5);
		System.out.println(menuItem.toString());
		OrderItem orderItem = new OrderItem(menuItem, "less salt");
		System.out.println(orderItem.toString());
	}
	

}
