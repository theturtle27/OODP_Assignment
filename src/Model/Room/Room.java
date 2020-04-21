package Model.Room;

import Model.reservation.Reservation;
import Model.Stay.Stay;
import Persistence.Entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Room extends Entity{

	private String roomNumber;
	private RoomTypeEnum roomType;
	private BedType bedType;
	private boolean enabledWifi;
	private boolean withView;
	private boolean smoking;
	private RoomStatus roomStatus;
	private LocalDate maintenanceEndDate;
	private ArrayList<Reservation> reservations;
	private Stay stay;

	// TODO: all rooms as vacant initialized
	public Room(String roomNumber, RoomTypeEnum roomType, BedType bedType, boolean enabledWifi, boolean withView, boolean smoking)
	{
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.bedType = bedType;
		this.enabledWifi = enabledWifi;
		this.withView = withView;
		this.smoking = smoking;
		this.roomStatus = RoomStatus.VACANT;
	}

	public String getRoomNumber()
	{
		return roomNumber;
	}

	public RoomTypeEnum getRoomType()
	{
		return roomType;
	}

	public void setRoomType(RoomTypeEnum roomType)
	{
		this.roomType = roomType;
	}

	public BedType getBedType()
	{
		return bedType;
	}

	public void setBedType(BedType bedType)
	{
		this.bedType = bedType;
	}

	public boolean getEnabledWifi()
	{
		return enabledWifi;
	}

	public void setEnabledWifi(boolean enabledWifi)
	{
		this.enabledWifi = enabledWifi;
	}

	public boolean getWithView()
	{
		return withView;
	}

	public void setWithView(boolean withView)
	{
		this.withView = withView;
	}

	public boolean getSmoking()
	{
		return smoking;
	}

	public void setSmoking(boolean smoking)
	{
		this.smoking = smoking;
	}

	public RoomStatus getRoomStatus()
	{
		return roomStatus;
	}

	public void setRoomStatus(RoomStatus roomStatus)
	{
		this.roomStatus = roomStatus;
	}

	public LocalDate getMaintenanceEndDate()
	{
		return maintenanceEndDate;
	}

	public void setMaintenanceEndDate(LocalDate maintenanceEndDate)
	{
		this.maintenanceEndDate = maintenanceEndDate;
	}

	public ArrayList<Reservation> getReservations()
	{
		return reservations;
	}

	public void setReservation(Reservation reservation)
	{
		reservations.add(reservation);
	}

	public Stay getStay()
	{
		return stay;
	}

	// test method
	public String toString()
	{
		// convert bed type to String
		String stringBedType = capitalizeFirstLetter(bedType.toString());

		// convert enabled Wifi to String
		String stringEnabledWifi = capitalizeFirstLetter(String.valueOf(enabledWifi));

		// convert with View to String
		String stringWithView = capitalizeFirstLetter(String.valueOf(withView));

		// convert smoking to String
		String stringSmoking = capitalizeFirstLetter(String.valueOf(smoking));

		// convert room status to String
		String stringRoomStatus = capitalizeFirstLetter(roomStatus.toString());

		// String for room information
		StringBuffer stringRoom = new StringBuffer();
		stringRoom.append("\n===============Room==============="
				+ "\nRoom Number     : " + roomNumber
				+ roomType.toString()
				+ "\n---------Room Description---------"
				+ "\nBed Type        : " + stringBedType
				+ "\nEnabled Wifi    : " + stringEnabledWifi
				+ "\nWith View       : " + stringWithView
				+ "\nSmoking         : " + stringSmoking
				+ "\n------------Room Status-----------"
				+ "\nRoom Status    : " + stringRoomStatus);

		if(roomStatus == RoomStatus.UNDER_MAINTENANCE)
		{

			// create a formatter for date
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			// convert maintenance end date to String
			String stringMaintenanceEndDate = maintenanceEndDate.format(formatter);

			// append maintenance dates to room
			stringRoom.append("\nEnd Date       : " + stringMaintenanceEndDate);

		}

		return stringRoom.toString();

	}

	// Method to convert the string
	public String capitalizeFirstLetter(String str) {
		StringBuffer s = new StringBuffer();

		// Declare a character of space
		// To identify that the next character is the starting
		// of a new word
		char ch = ' ';
		for (int i = 0; i < str.length(); i++) {

			// If previous character is space and current
			// character is not space then it shows that
			// current letter is the starting of the word
			if (ch == ' ' && str.charAt(i) != ' ')
			{
				ch = str.charAt(i);
				s.append(Character.toUpperCase(ch));
			}
			else if(str.charAt(i) == '_')
			{
				ch = ' ';
				s.append(ch);
			}
			else
			{
				ch = str.charAt(i);
				s.append(Character.toLowerCase(ch));
			}
		}

		// Return the string with trimming
		return s.toString().trim();
	}
}
