package Model.reservation;

import Model.Guest.CreditCard;
import Model.Guest.Guest;
import Model.Room.Room;
import Model.Room.RoomStatus;
import Model.StatusEntity;
import Persistence.Entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class Reservation extends StatusEntity<ReservationStatus> {

	private Guest guest;
	private CreditCard creditCard;
	private Room room;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private short numberOfAdults;
	private short numberOfChildren;

	// constructor for use in program
	public Reservation(Guest guest, CreditCard creditCard, Room room, LocalDate checkInDate, LocalDate checkOutDate, short numberOfAdults, short numberOfChildren, ReservationStatus reservationStatus) {

		this.guest = guest;
		this.creditCard = creditCard;
		this.room = room;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.numberOfAdults = numberOfAdults;
		this.numberOfChildren = numberOfChildren;
		super.setStatus(reservationStatus);
		this.room.setReservation(this);
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

	public void setCreditCard(CreditCard creditCard)
	{
		this.creditCard = creditCard;
	}

	public LocalDate getCheckInDate()
	{
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate)
	{
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate()
	{
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate)
	{
		this.checkOutDate = checkOutDate;
	}

	public short getNumberOfAdults()
	{
		return numberOfAdults;
	}

	public void setNumberOfAdults(short numberOfAdults)
	{
		this.numberOfAdults = numberOfAdults;
	}

	public short getNumberOfChildren()
	{
		return numberOfChildren;
	}

	public void setNumberOfChildren(short NumberOfChildren)
	{
		this.numberOfChildren = numberOfChildren;
	}

	// test method
	public String toString()
	{

		// convert room status to String
		String stringReservationStatus = capitalizeFirstLetter(super.getStatus().toString());

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

		return    "\n===========Reservation============"
				+ "\nID              : " + this.getIdentifier()
				+ "\nStatus          : " + stringReservationStatus
				+ "\n-------Reservation Details--------"
				+ "\nCheck In Date   : " + stringCheckInDate
				+ "\nCheck Out Date  : " + stringCheckOutDate
				+ "\nNo. of Adults   : " + numberOfAdults
				+ "\nNo. of Children : " + numberOfChildren
				+ "\n-----------Room Details-----------"
				+ "\nRoom Type       : " + stringRoomTypeEnum
				+ "\nBed Type        : " + stringBedType
				+ "\nEnabled Wifi    : " + stringEnabledWifi
				+ "\nWith View       : " + stringWithView
				+ "\nSmoking         : " + stringSmoking
				+ "\n----------Guest Details-----------"
				+ "\nName            : " + guest.getName()
				+ creditCard.toString()
				+ "-----------Cost Details-----------"
				+ "\nNo. of nights   : " + numberOfDays
				+ "\nRate per night  : SGD " + stringRoomRate
				+ "\nTotal cost      : SGD " + stringTotalCost;


	}


}
