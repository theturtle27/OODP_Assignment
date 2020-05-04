package Model.Payment;

import Model.RoomServiceOrder.RoomServiceOrder;
import Model.RoomServiceOrder.RoomServiceOrderStatus;
import Model.Stay.Stay;
import Persistence.Entity;

import java.util.ArrayList;

public class Payment extends Entity {
    // private final List<Reservation> reservations;
    private ArrayList<Stay> stays;
    private double weekendSurcharge;
    private double discount;
    private double tax;
    private DiscountType discountType;
    private PaymentType paymentType;

    public Payment(ArrayList<Stay> stays) {
        this.stays = stays;
    }

    public double getWeekendSurcharge() {
        return weekendSurcharge;
    }

    public void setWeekendSurcharge(double weekendSurcharge) {
        this.weekendSurcharge = weekendSurcharge;
    }

    public void setDiscount(DiscountType discountType, double discount) {
        this.discount = discount;
        this.discountType = discountType;
    }

    public double getTotalSum() {
        double totalSum = 0;

        // Loop through all reservations and add up all the room prices
        for(Stay stay: stays) {
            long numOfDays = (stay.getCheckOutDate().toEpochDay() - stay.getCheckInDate().toEpochDay());
            totalSum += stay.getRoom().getRoomType().getRoomRate() * numOfDays;

            for (RoomServiceOrder order : stay.getRoomServiceOrders()) {
                // Loop through all the service orders and add up all the service orders.
                if (order.getStatus() == RoomServiceOrderStatus.DELIVERED)
                    totalSum += order.getTotalPrice();
            }
        }


        // Perform discount operation
        if(discountType != null) {
            if(discountType == DiscountType.FIXED)
                totalSum -= discount;
            else
                totalSum *= (100 - discount) / 100;
        }

        return totalSum;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

}
