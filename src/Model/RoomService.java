package Model;

import java.util.Date;

public class RoomService {
    int orderID;
    Item[] items;
    double totalPrice;
    Guest guest;
    Date orderDate;
    String Remark;
    String Status;

    public RoomService(int orderID, Item[] items, double totalPrice, Guest guest, Date orderDate, String remark, String status) {
        this.orderID = orderID;
        this.items = items;
        this.totalPrice = totalPrice;
        this.guest = guest;
        this.orderDate = orderDate;
        Remark = remark;
        Status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

class Item {
    double price;
    String description;
    String name;

    public Item(double price, String description, String name) {
        this.price = price;
        this.description = description;
        this.name = name;
    }
}