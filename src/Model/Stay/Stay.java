package Model.Stay;

import Model.Guest.CreditCard;
import Model.Guest.Guest;
import Model.Room.Room;
import Model.Room.RoomStatus;
import Model.RoomServiceOrder.RoomServiceOrder;
import Model.Reservation.Reservation;
import Model.Reservation.ReservationStatus;
import Persistence.Entity;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;

public class Stay extends Entity {

    private Guest guest;
    private CreditCard creditCard;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private ArrayList<RoomServiceOrder> roomServiceOrders;

    //TODO: Add room service orders

    //check-in for walk-in guest
    public Stay(Guest guest, CreditCard creditCard, Room room, LocalDate checkInDate, LocalDate checkOutDate, int numberOfAdults, int numberOfChildren)
    {
        this.guest = guest;
        this.creditCard = creditCard;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
        this.roomServiceOrders = new ArrayList<>();

        // set room status of rooms to occupied
        room.setStatus(RoomStatus.OCCUPIED);
        room.setStay(this);
    }

    // check in for guest with reservation
    public Stay(Reservation reservation)
    {
        // pass details from reservation to stay
        guest = reservation.getGuest();
        creditCard = reservation.getCreditCard();
        room = reservation.getRoom();
        checkInDate = reservation.getCheckInDate();
        checkOutDate = reservation.getCheckOutDate();
        numberOfAdults = reservation.getNumberOfAdults();
        numberOfChildren = reservation.getNumberOfChildren();
        this.roomServiceOrders = new ArrayList<>();

        // set reservation status to checked in
        reservation.setStatus(ReservationStatus.CHECKED_IN);

        // set room status of rooms to occupied
        room.setStatus(RoomStatus.OCCUPIED);
        room.setStay(this);
    }

    public void checkOut()
    {

        room.setStatus(RoomStatus.VACANT);

        System.out.println(room.getReservations().size());

        // iterate through reservations of this room
        for(Reservation reservation : room.getReservations())
        {

            // find reservation
            if(reservation.getStatus().equals(ReservationStatus.CHECKED_IN))
            {

                reservation.setStatus(ReservationStatus.CHECKED_OUT);
            }
        }

    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void addRoomSerciceOrder(RoomServiceOrder roomServiceOrder){
        this.roomServiceOrders.add(roomServiceOrder);
    }

    public ArrayList<RoomServiceOrder> getRoomServiceOrders() {
        return roomServiceOrders;
    }

    public String toString()
    {

        // create a formatter for date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String stringCheckInDate = checkInDate.format(formatter);

        String stringCheckOutDate = checkOutDate.format(formatter);

        // get the number of days of the stay
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // convert room type enum to String
        String stringRoomTypeEnum = capitalizeFirstLetter(room.getRoomType().getRoomTypeEnum().toString());

        // convert bed type to String
        String stringBedType = capitalizeFirstLetter(room.getBedType().toString());

        String stringEnabledWifi;

        // convert enabled Wifi to String
        if(room.getEnabledWifi()) {
            stringEnabledWifi = "Yes";
        }
        else
        {
            stringEnabledWifi = "No";
        }

        String stringWithView;

        // convert enabled Wifi to String
        if(room.getWithView()) {
            stringWithView = "Yes";
        }
        else
        {
            stringWithView = "No";
        }

        String stringSmoking;

        // convert enabled Wifi to String
        if(room.getSmoking()) {
            stringSmoking = "Yes";
        }
        else
        {
            stringSmoking = "No";
        }

        // format room rate
        String stringRoomRate = String.format("%.2f",room.getRoomType().getRoomRate());

        // format total cost
        String stringTotalCost = String.format("%.2f",room.getRoomType().getRoomRate()*numberOfDays);

        return    "\n===============Stay================"
                + "\nCheck In Date    : " + stringCheckInDate
                + "\nCheck Out Date   : " + stringCheckOutDate
                + "\nNo. of Adults    : " + numberOfAdults
                + "\nNo. of Children  : " + numberOfChildren
                + "\n-----------Room Details------------"
                + "\nRoom Number      : " + room.getRoomNumber()
                + "\nRoom Type        : " + stringRoomTypeEnum
                + "\nBed Type         : " + stringBedType
                + "\nEnabled Wifi     : " + stringEnabledWifi
                + "\nWith View        : " + stringWithView
                + "\nSmoking          : " + stringSmoking
                + "\n-----------Guest Details-----------"
                + "\nName             : " + guest.getName()
                + creditCard.toString()
                + "-----------Cost Details------------"
                + "\nNo. of nights    : " + numberOfDays
                + "\nRate per night   : SGD " + stringRoomRate
                + "\nTotal cost       : SGD " + stringTotalCost;


    }
}

