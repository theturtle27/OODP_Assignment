package Model.Stay;

import Model.Guest.Guest;
import Model.Room.Room;
import Model.Room.RoomStatus;
import Model.reservation.Reservation;
import Model.reservation.ReservationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;

public class Stay {

    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfAdults;
    private int numberOfChildren;

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

        // set room status of rooms to occupied
        checkInRooms();
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

        // set reservation status to checked in
        reservation.setReservationStatus(ReservationStatus.CHECKED_IN);

        // set room status of rooms to occupied
        checkInRooms();
    }

    //TODO: not finished
    private void checkInRooms()
    {
        // create hashmap
        HashMap<LocalDate, RoomStatus> roomStatus = new HashMap<LocalDate, RoomStatus>();

        //iterate through all dates of the reservation up to day before check-out
        for (LocalDate date = checkInDate; date.isBefore(checkOutDate); date = date.plusDays(1))
        {
            roomStatus.put(date, RoomStatus.OCCUPIED);
        }

        // iterate through all rooms of the reservation
        for(Room room : rooms)
        {
            //pass rooms the reference of this reservation
            room.setRoomStatus(roomStatus);
        }
    }

    // TODO: not finished
    public void checkOut()
    {
        // create a new payment object
        Payment payment = new Payment();

        // calculate the total price of the stay
        payment.calculateTotalPrice(this);

        // iterate through all rooms of the reservation
        for(Room room : rooms)
        {
            // create HashMap
            HashMap<LocalDate, RoomStatus> roomStatus = new HashMap<LocalDate, RoomStatus>();
            roomStatus.put(LocalDate.now(), RoomStatus.VACANT);

            //pass rooms the reference of this reservation
            room.setRoomStatus(roomStatus);
        }
    }

}

