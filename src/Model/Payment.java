package Model;

public class Payment {
    int paymentID;
    double charge;
    double tax;
    RoomService roomService;
    double discount;
    boolean weekend;
    String paymentMethod;
    String billingAddress;

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public void setWeekend(boolean weekend) {
        this.weekend = weekend;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Payment(int paymentID, double charge, double tax, RoomService roomService, double discount, boolean weekend, String paymentMethod, String billingAddress) {
        this.paymentID = paymentID;
        this.charge = charge;
        this.tax = tax;
        this.roomService = roomService;
        this.discount = discount;
        this.weekend = weekend;
        this.paymentMethod = paymentMethod;
        this.billingAddress = billingAddress;
    }
}
