package Model.RoomServiceOrder;

import Model.StatusEntity;

import java.util.Arrays;
import java.util.Date;

public class RoomServiceOrder extends StatusEntity<RoomServiceOrderStatus> {
    private OrderItem[] orderItems;
    private Date orderTime;
    private double totalPrice;

    public RoomServiceOrder(OrderItem[] orderItems) {
        this.orderItems = orderItems;
        this.orderTime = new Date();
        this.totalPrice = 0;
    }

    public OrderItem[] getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItem[] orderItems) {
        this.orderItems = orderItems;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "==========RoomServiceOrder============" +
                "\norderItems=\n" + Arrays.toString(orderItems) +
                "\norderTime=" + orderTime +
                "\ntotalPrice=" + totalPrice +
                '\n';
    }
}
