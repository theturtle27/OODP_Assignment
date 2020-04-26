package Model.Room;

import Model.Reservation.Reservation;
import Model.StatusEntity;
import Model.Stay.Stay;
import Model.StatusEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Room extends StatusEntity<RoomStatus> {

	private String roomNumber;
	private RoomType roomType;
	private BedType bedType;
	private boolean enabledWifi;
	private boolean withView;
	private boolean smoking;
	private LocalDate maintenanceEndDate;
	private ArrayList<Reservation> reservations;
	private Stay stay;

	public Room(String roomNumber, RoomType roomType, BedType bedType, boolean enabledWifi, boolean withView, boolean smoking)
	{
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.bedType = bedType;
		this.enabledWifi = enabledWifi;
		this.withView = withView;
		this.smoking = smoking;
		super.setStatus(RoomStatus.VACANT);
		this.reservations = new ArrayList<>();
	}

	public String getRoomNumber()
	{
		return roomNumber;
	}

	public RoomType getRoomType()
	{
		return roomType;
	}

	public void setRoomType(RoomType roomType)
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

	public void setStay(Stay stay)
	{
		this.stay = stay;
	}

	// test method
	public String toString()
	{
		// convert bed type to String
		String stringBedType = capitalizeFirstLetter(bedType.toString());

		String stringEnabledWifi;

		// convert enabled Wifi to String
		if(enabledWifi) {
			stringEnabledWifi = "Yes";
		}
		else
		{
			stringEnabledWifi = "No";
		}

		String stringWithView;

		// convert enabled Wifi to String
		if(withView) {
			stringWithView = "Yes";
		}
		else
		{
			stringWithView = "No";
		}

		String stringSmoking;

		// convert enabled Wifi to String
		if(smoking) {
			stringSmoking = "Yes";
		}
		else
		{
			stringSmoking = "No";
		}

		// convert room status to String
		String stringRoomStatus = capitalizeFirstLetter(getStatus().toString());

		// String for room information
		StringBuffer stringRoom = new StringBuffer();
		stringRoom.append("\n===============Room================"
				+ "\nRoom Number      : " + roomNumber
				+ roomType.toString()
				+ "\n---------Room Description----------"
				+ "\nBed Type         : " + stringBedType
				+ "\nEnabled Wifi     : " + stringEnabledWifi
				+ "\nWith View        : " + stringWithView
				+ "\nSmoking          : " + stringSmoking
				+ "\n------------Room Status------------"
				+ "\nRoom Status      : " + stringRoomStatus);

		if(getStatus() == RoomStatus.UNDER_MAINTENANCE)
		{

			// create a formatter for date
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			// convert maintenance end date to String
			String stringMaintenanceEndDate = maintenanceEndDate.format(formatter);

			// append maintenance dates to room
			stringRoom.append("\nEnd Date         : " + stringMaintenanceEndDate);

		}

		return stringRoom.toString();

	}

}
