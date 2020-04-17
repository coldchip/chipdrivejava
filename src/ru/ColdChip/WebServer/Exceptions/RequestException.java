package ru.ColdChip.WebServer.Exceptions;

public class RequestException extends Exception {

	private String errorMsg = "";

	public RequestException() {
		
	}

	public RequestException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String toString() {
		return "CustomException[]";
	}
}