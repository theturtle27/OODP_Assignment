package Model;

import Model.Guest.Guest;
import Model.Room.Room;

import java.util.Date;

public class Reservation {
    String reservationID;
    String status;
    Guest assosiateGuest;
    Room[] rooms;
    Date checkInDate;
    Date checkOutDate;
    int noOfAdults;
    int noOfChildren;

    public Reservation(String reservationID, String status, Guest assosiateGuest, Room[] rooms, Date checkInDate, Date checkOutDate, int noOfAdults, int noOfChildren) {
        this.reservationID = reservationID;
        this.status = status;
        this.assosiateGuest = assosiateGuest;
        this.rooms = rooms;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.noOfAdults = noOfAdults;
        this.noOfChildren = noOfChildren;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Guest getAssosiateGuest() {
        return assosiateGuest;
    }

    public void setAssosiateGuest(Guest assosiateGuest) {
        this.assosiateGuest = assosiateGuest;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNoOfAdults() {
        return noOfAdults;
    }

    public void setNoOfAdults(int noOfAdults) {
        this.noOfAdults = noOfAdults;
    }

    public int getNoOfChildren() {
        return noOfChildren;
    }

    public void setNoOfChildren(int noOfChildren) {
        this.noOfChildren = noOfChildren;
    }
}
