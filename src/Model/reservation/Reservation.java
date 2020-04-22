package Model.reservation;

import Model.Guest.Guest;
import Model.Room.Room;
import Model.StatusEntity;
import Persistence.Entity;

import java.time.LocalDate;
import java.util.Date;


public class Reservation extends StatusEntity<ReservationStatus>{

	private Guest guest;
	private Room room;
	private Date checkInDate;
	private Date checkOutDate;
	private short numberOfAdults;
	private short numberOfChildren;

	// constructor for use in program
	public Reservation(Guest guest, Room room, Date checkInDate, Date checkOutDate, short numberOfAdults, short numberOfChildren)
	{

		this.guest = guest;
		this.room = room;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.numberOfAdults = numberOfAdults;
		this.numberOfChildren = numberOfChildren;
		super.setStatus(ReservationStatus.CONFIRMED);
		this.room.setReservation(this);
	}

	public Guest getGuest()
	{
		return guest;
	}

	public Room getRoom()
	{
		return room;
	}

	public Date getCheckInDate()
	{
		return checkInDate;
	}

	public Date getCheckOutDate()
	{
		return checkOutDate;
	}

	public int getNumberOfAdults()
	{
		return numberOfAdults;
	}

	public int getNumberOfChildren()
	{
		return numberOfChildren;
	}


}
