package Model.RoomServiceOrder;

import Model.Menu.MenuItem;

public class OrderItem {
    private MenuItem menuItem;
    private String orderRemark;

    public OrderItem(MenuItem menuItems, String remarks) {
        this.menuItem = menuItems;
        this.orderRemark = remarks;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItems) {
        this.menuItem = menuItems;
    }

    public String getRemark() {
        return orderRemark;
    }

    public void setRemark(String remarks) {
        this.orderRemark = remarks;
    }

    @Override
    public String toString() {
        return "=========OrderItem========" +
                "\nmenuItems:\n" + menuItem +
                "\nremarks='" + orderRemark +
                '\n';
    }
}
