package Model.RoomServiceOrder;

import Model.Menu.MenuItem;

public class OrderItem {
    private MenuItem menuItems;
    private String remarks;

    public OrderItem(MenuItem menuItems, String remarks) {
        this.menuItems = menuItems;
        this.remarks = remarks;
    }

    public MenuItem getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItem menuItems) {
        this.menuItems = menuItems;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "=========OrderItem========" +
                "\nmenuItems=" + menuItems +
                "\nremarks='" + remarks +
                '\n';
    }
}
