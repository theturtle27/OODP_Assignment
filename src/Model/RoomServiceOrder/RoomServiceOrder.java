package Model.RoomServiceOrder;

import Model.Menu.MenuItem;
import Model.StatusEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomServiceOrder extends StatusEntity<RoomServiceOrderStatus> {
    private ArrayList<OrderItem> orderItems;
    private Date orderTime;
    private String roomNumber;

    public RoomServiceOrder(String roomNumber)
    {
        orderItems = new ArrayList<OrderItem>();
        this.roomNumber = roomNumber;
        super.setStatus(RoomServiceOrderStatus.ORDERING);
        //TODO: problem with date
        this.orderTime = new Date();
    }

    public void addOrderItem(OrderItem orderItem)
    {
        orderItems.add(orderItem);

        MenuItem menuItem = orderItem.getMenuItem();
    }

    public void removeOrderItem(int index)
    {
        OrderItem orderItem = orderItems.get(index);
        MenuItem menuItem = orderItem.getMenuItem();

        orderItems.remove(index);
    }

    public double getTotalPrice(){
        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getMenuItem().getPrice();
        }
        return totalPrice;
    }
    //
    public void setOrderItemRemark(int index, String orderItemRemark)
    {
        OrderItem orderItem = orderItems.get(index);
        orderItem.setRemark(orderItemRemark);
    }
    //
    public void confirmRoomServiceOrder()
    {
        // sets date and time to now
        orderTime = new Date();
        super.setStatus(RoomServiceOrderStatus.CONFIRMED);
    }
    //
    public ArrayList<OrderItem> getOrderItems()
    {
        return orderItems;
    }
    //
    public void setRoomServiceOrderStatus(RoomServiceOrderStatus roomServiceOrderStatus)
    {
        super.setStatus(roomServiceOrderStatus);
    }
    //
    public RoomServiceOrderStatus getRoomServiceOrderStatus()
    {
        return super.getStatus();
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

    @Override
    public String toString() {
        return "==========RoomServiceOrder============" +
                "\norderItems=\n" + orderItems.toString() +
                "\norderTime=" + orderTime +
                "\ntotalPrice=" + getTotalPrice() +
                '\n';
    }
}
