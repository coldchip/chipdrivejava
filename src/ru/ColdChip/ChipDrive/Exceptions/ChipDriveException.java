package ru.ColdChip.ChipDrive.Exceptions;

public class ChipDriveException extends Exception {

	public String msg = "ChipDriveException.Message.None";

	public ChipDriveException() {
        this.msg = msg;
        // Call super(parent) class
    }

    public ChipDriveException(String msg) {
        this.msg = msg;
        // Call super(parent) class
    }

    public String toString() {
    	return this.msg;
    }

}