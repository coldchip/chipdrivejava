package ru.ColdChip.ChipDrive.Exceptions;

public class ChipDriveLoginException extends Exception {

	private String errorMsg = "";

	public ChipDriveLoginException() {
		
	}

	public ChipDriveLoginException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String toString() {
		return this.errorMsg;
	}
}