package Model;

import Persistence.Entity;

public class StatusEntity<T extends Enum<T>> extends Entity {
	private T status;

	public T getStatus() {
		return this.status;
	}

	public void setStatus(T status) {
		this.status = status;
	}
}
