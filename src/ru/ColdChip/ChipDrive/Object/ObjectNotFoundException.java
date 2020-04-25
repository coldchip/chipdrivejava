package ru.ColdChip.ChipDrive.Object;

public class ObjectNotFoundException extends Exception {

	private String errorMsg = "";

	public ObjectNotFoundException() {
		
	}

	public ObjectNotFoundException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String toString() {
		return this.errorMsg;
	}
}