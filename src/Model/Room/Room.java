package Model.Room;

import Model.StatusEntity;
import Model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

public class Room extends StatusEntity<RoomStatus> {
	private final String number;
	private final List<Reservation> reservations;
	private final RoomDescription description;

	protected Room() {
		this.number = null;
		this.reservations = null;
		this.description = null;
	}

	public Room(String number) {
		this.number = number;
		this.reservations = new ArrayList<Reservation>();
		this.description = new RoomDescription();
		this.setStatus(RoomStatus.Vacant);
	}

	/**
	 * Gets Room Number.
	 * @return number
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Gets a list of {@link Reservation} associated with this Room instance.
	 * @return reservations
	 */
	public List<Reservation> getReservationList() {
		return reservations;
	}

	/**
	 * Gets Room View.
	 * @return view
	 */
	public String getView() {
		return description.getView();
	}
	
	/**
	 * Sets Room View.
	 * @param view
	 */
	public void setView(String view) {
		this.description.setView(view);
	}

	/**
	 * Gets is Wifi-Enabled.
	 * @return wifi
	 */
	public boolean isWifi() {
		return description.isWifi();
	}

	/**
	 * Sets Wifi-Enabled.
	 * @param wifi
	 */
	public void setWifi(boolean wifi) {
		this.description.setIsWifi(wifi);
	}

	/**
	 * Gets is Smoking-Room.
	 * @return
	 */
	public boolean isSmoking() {
		return description.isSmoking();
	}

	/**
	 * Sets is Smoking-Room.
	 * @param smoking
	 */
	public void setSmoking(boolean smoking) {
		this.description.setIsSmoking(smoking);
	}

	/**
	 * Gets Room Type.
	 * @return type
	 */
	public RoomType getType() {
		return description.getRoomType();
	}

	/**
	 * Sets Room Type.
	 * @param type
	 */
	public void setType(RoomType type) {
		this.description.setRoomType(type);
	}
	
	/**
	 * Gets Bed Type.
	 * @return
	 */
	public BedType getBedType() {
		return description.getBedType();
	}

	/**
	 * Sets Bed Type.
	 * @param bedType
	 */
	public void setBedType(BedType bedType) {
		this.description.setBedType(bedType);
	}
	
	/**
	 * Gets the {@link RoomDescription} of this Room instance.
	 * @return description
	 */
	public RoomDescription getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"Room Number: " + this.getNumber() + "\n" +
				"Room View: " + this.getView() + "\n" +
				"Room Status: " + this.getStatus() +"\n" +
				"Bed Type: " + this.getBedType() + "\n" +
				"Wifi-Enabled: " + this.isWifi() + "\n" +
				"Smoking-Room: " + this.isSmoking() + "\n";
	}
}
