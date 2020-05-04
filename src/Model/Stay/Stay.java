package Model.Stay;

import Model.Guest.Guest;
import Model.Room.Room;
import Model.Room.RoomStatus;
import Model.RoomServiceOrder.RoomServiceOrder;
import Model.StatusEntity;
import Model.Reservation.Reservation;
import Model.Reservation.ReservationStatus;

import java.util.ArrayList;
import java.time.LocalDate;

public class Stay extends StatusEntity<StayStatus> {

    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private ArrayList<RoomServiceOrder> roomServiceOrders;

    //TODO: Add room service orders

    //check-in for walk-in guest
    public Stay(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate, int numberOfAdults, int numberOfChildren)
    {
        this.guest = guest;
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

    private void checkInRooms()
    {



    }

    public void checkOut()
    {

        room.setStatus(RoomStatus.VACANT);

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
}

