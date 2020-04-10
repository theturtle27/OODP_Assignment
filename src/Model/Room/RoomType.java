package Model.Room;

import Persistence.Entity;

public class RoomType extends Entity {
	private final String name;
	private double price;

	protected RoomType() {
		this.name = null;
		this.price = 0;
	}

	public RoomType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return super.toString() +
				"Type: " + this.name + "\n" +
				"Price: " + String.format("%.2f", this.price) + "\n";
	}
}
