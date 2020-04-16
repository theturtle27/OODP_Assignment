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
}
