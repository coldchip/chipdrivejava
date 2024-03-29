package ru.ColdChip.ChipDrive.Controllers;

public class DriveQueue {
	private int method;
	private DriveRequest request;
	private DriveResponse response;
	private boolean ready = false;

	public DriveQueue(int method, DriveRequest request, DriveResponse response) {
		this.method = method;
		this.request = request;
		this.response = response;
	}

	public int getMethod() {
		return this.method;
	}

	public DriveRequest getRequest() {
		return this.request;
	}

	public DriveResponse getResponse() {
		return this.response;
	}

	public void setDone(boolean ready) {
		this.ready = ready;
	}

	public boolean isReady() {
		return this.ready;
	}
}