package Model.Payment;

import Model.Room.RoomStatus;
import Model.RoomServiceOrder.RoomServiceOrder;
import Model.RoomServiceOrder.RoomServiceOrderStatus;
import Model.Stay.Stay;
import Persistence.Entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class Payment extends Entity {

    private DiscountType discountType;
    private double discount;
    private double weekendSurcharge;
    private double tax;
    private PaymentType paymentType;
    private Stay stay;

    public Payment(DiscountType discountType, double discount, double weekendSurcharge, double tax, Stay stay) {
        this.discountType = discountType;
        this.discount = discount;
        this.weekendSurcharge = weekendSurcharge;
        this.tax = tax;
        this.stay = stay;
    }

    public double getWeekendSurcharge() {
        return weekendSurcharge;
    }

    public void setWeekendSurcharge(double weekendSurcharge) {
        this.weekendSurcharge = weekendSurcharge;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String toString()
    {
        // create a formatter for date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String stringCheckInDate = stay.getCheckInDate().format(formatter);

        String stringCheckOutDate = stay.getCheckOutDate().format(formatter);

        // convert room type enum to String
        String stringRoomTypeEnum = capitalizeFirstLetter(stay.getRoom().getRoomType().getRoomTypeEnum().toString());

        // convert bed type to String
        String stringBedType = capitalizeFirstLetter(stay.getRoom().getBedType().toString());

        String stringEnabledWifi;

        // convert enabled Wifi to String
        if(stay.getRoom().getEnabledWifi()) {
            stringEnabledWifi = "Yes";
        }
        else
        {
            stringEnabledWifi = "No";
        }

        String stringWithView;

        // convert enabled Wifi to String
        if(stay.getRoom().getWithView()) {
            stringWithView = "Yes";
        }
        else
        {
            stringWithView = "No";
        }

        String stringSmoking;

        // convert enabled Wifi to String
        if(stay.getRoom().getSmoking()) {
            stringSmoking = "Yes";
        }
        else
        {
            stringSmoking = "No";
        }

        // get the number of days of the stay
        long numberOfDays = ChronoUnit.DAYS.between(stay.getCheckInDate(), stay.getCheckOutDate());

        // get room rate
        double roomRate = stay.getRoom().getRoomType().getRoomRate();

        // format room rate
        String stringRoomRate = String.format("%.2f",roomRate);

        // calculate weekend room rate
        double weekendRoomRate = stay.getRoom().getRoomType().getRoomRate() * (1 + weekendSurcharge/100);

        // format weekend room rate
        String stringWeekendRoomRate = String.format("%.2f",weekendRoomRate);

        // declare number of weekdays, weekenddays
        int noOfWeekDays = 0;
        int noOfWeekEndDays = 0;

        //iterate through days
        for (LocalDate date = stay.getCheckInDate(); date.isBefore(stay.getCheckOutDate()); date = date.plusDays(1))
        {

            // check whether day is on the weekend
            if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            {

                // increment number of weekend days
                noOfWeekEndDays++;
            }
            else
            {
                // number of week days
                noOfWeekDays++;
            }
        }

        // calculate price from room
        double totalRoom = noOfWeekDays * roomRate + noOfWeekEndDays * weekendRoomRate;

        // calculate net price
        double netPrice = totalRoom;

        // iterate through room orders
        for(RoomServiceOrder roomServiceOrder : stay.getRoomServiceOrders())
        {
            netPrice += roomServiceOrder.getTotalPrice();
        }

        // format net price
        String stringNetPrice = String.format("%.2f",netPrice);

        // format tax
        String stringTax = String.format("%.2f",tax);

        // calculate total price without discount
        double subtotal = netPrice * (1 + tax/100);

        // format tax
        String stringSubtotal = String.format("%.2f",subtotal);

        // calculate total price
        double total = subtotal;

        // take discount into account
        if(discountType == DiscountType.FIXED)
        {
            total -= discount;
        }
        else if (discountType == DiscountType.PERCENTAGE)
        {
            total *= (1 - discount/100);
        }

        // format tax
        String stringTotal = String.format("%.2f",total);

        // String for room information
        StringBuffer stringPayment = new StringBuffer();
        stringPayment.append("\n==============RECEIPT=============="
                + "\n-----------Guest Details-----------"
                + "\nName             : " + stay.getGuest().getName()
                + "\n-----------Room Details------------"
                + "\nRoom Number      : " + stay.getRoom().getRoomNumber()
                + "\nRoom Type        : " + stringRoomTypeEnum
                + "\nBed Type         : " + stringBedType
                + "\nEnabled Wifi     : " + stringEnabledWifi
                + "\nWith View        : " + stringWithView
                + "\nSmoking          : " + stringSmoking
                + "\n-----------Stay Details------------"
                + "\nCheck In Date    : " + stringCheckInDate
                + "\nCheck Out Date   : " + stringCheckOutDate
                + "\nNo. of Nights    : " + numberOfDays
                + "\n   Weekdays      : " + noOfWeekDays
                + "\n   Weekends      : " + noOfWeekEndDays
                + "\nNo. of Adults    : " + stay.getNumberOfAdults()
                + "\nNo. of Children  : " + stay.getNumberOfChildren()
                + "\nNo of Room Orders: " + stay.getRoomServiceOrders().size()
                + "\n-----------Cost Details------------"
                + "\nRate per Night"
                + "\n   Weekdays      : SGD " + stringRoomRate
                + "\n   Weekends      : SGD " + stringWeekendRoomRate
                + "\nCost of Orders"
                );

        // initialize iterator
        int iterator = 1;

        // iterate through room service orders
        for(RoomServiceOrder roomServiceOrder : stay.getRoomServiceOrders())
        {
            // format total cost
            String stringRoomServiceOrderCost = String.format("%.2f",roomServiceOrder.getTotalPrice());

            // print price of room service order
            stringPayment.append("\n   " + iterator + ". order      : SGD " + stringRoomServiceOrderCost);

            // increment iterator
            iterator++;
        }

        stringPayment.append("\n-------------Subtotal--------------"
                + "\nNet Price        : SGD " + stringNetPrice
                + "\nGST              : " + stringTax + " %"
                + "\nSubtotal         : SGD " + stringSubtotal);


        if(discountType != null)
        {

            // convert discount type to String
            String stringDiscountType = capitalizeFirstLetter(discountType.toString());

            // format discount
            String stringDiscount = String.format("%.2f",discount);

            // print the discount
            stringPayment.append("\n-------------Discount--------------"
                                +"\nDiscount Type    : " + stringDiscountType
                                +"\nDiscount         : ");

            if(discountType == DiscountType.FIXED)
            {
                stringPayment.append("SGD ");
            }

            stringPayment.append(stringDiscount);

            if(discountType == DiscountType.PERCENTAGE)
            {
                stringPayment.append(" %");
            }

        }

        // print the total amount
        stringPayment.append("\n---------------Total---------------" +
                "\nTotal            : SGD " + stringTotal + "\n\n");

        return stringPayment.toString();
    }

}
