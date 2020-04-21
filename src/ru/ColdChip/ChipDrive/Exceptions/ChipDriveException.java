package ru.ColdChip.ChipDrive.Exceptions;

public class ChipDriveException extends Exception {

	private String errorMsg = "";

	public ChipDriveException() {
		
	}

	public ChipDriveException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String toString() {
		return "CustomException[]";
	}
}