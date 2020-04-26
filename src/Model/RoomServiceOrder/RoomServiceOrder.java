package Model.RoomServiceOrder;

import Model.Menu.MenuItem;
import Model.StatusEntity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class RoomServiceOrder extends StatusEntity<RoomServiceOrderStatus> {
    private ArrayList<OrderItem> orderItems;
    private LocalDateTime orderTime;
    private double totalPrice;

    public RoomServiceOrder()
    {
        orderItems = new ArrayList<OrderItem>();
        this.orderTime = null;
    }

    public void addOrderItem(OrderItem orderItem)
    {
        orderItems.add(orderItem);

        MenuItem menuItem = orderItem.getMenuItem();
    }

    public double getTotalPrice(){
        double totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getMenuItem().getPrice();
        }
        return totalPrice;
    }
    //
    public void confirmRoomServiceOrder()
    {
        // sets date and time to now
        orderTime = LocalDateTime.now();
        super.setStatus(RoomServiceOrderStatus.CONFIRMED);
        totalPrice = getTotalPrice();
    }

    /*
    public ArrayList<OrderItem> getOrderItems()
    {
        return orderItems;
    }


    public void setRoomServiceOrderStatus(RoomServiceOrderStatus roomServiceOrderStatus)
    {
        super.setStatus(roomServiceOrderStatus);
    }
    //
    public RoomServiceOrderStatus getRoomServiceOrderStatus()
    {
        return super.getStatus();
    }*/

    @Override
    public String toString() {

        // create a formatter for date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        String stringOrderTime = orderTime.format(formatter);

        // convert room service order status to String
        String stringRoomServiceOrderStatus = capitalizeFirstLetter(getStatus().toString());

        // String for room service order information
        StringBuffer stringRoomServiceOrder = new StringBuffer();
        stringRoomServiceOrder.append("\n========Room Service Order========="
                + "\nOrder Status     : " + stringRoomServiceOrderStatus
                + "\nOrder Time       : " + stringOrderTime);

        // iterate through order items
        for(OrderItem orderItem : orderItems)
        {
            // append maintenance dates to room
            stringRoomServiceOrder.append(orderItem);
        }

        // format total price
        String stringTotalPrice = String.format("%.2f",totalPrice);

        stringRoomServiceOrder.append("\n-----------------------------------"
                                    +"\nTotal Price      : SGD " + stringTotalPrice);

        return stringRoomServiceOrder.toString();
    }
}
