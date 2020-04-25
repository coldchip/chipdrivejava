// Class to bridge between the response of ChipDrive API

package ru.ColdChip.ChipDrive.Controllers;

import java.io.*;

import ru.ColdChip.WebServer.Response;

public class DriveResponse {
	private String contentType = "";
	private ByteArrayOutputStream output = new ByteArrayOutputStream();

	private Response response;
	public DriveResponse(Response response) {
		this.response = response;
	}

	public void setContentType(String type) {
		this.contentType = type;
	}

	public void write(String data) {
		try {
			output.write(data.getBytes());
		} catch(IOException e) {
			// Ignore
		}
	}

	public void checkout() {
		try {
			byte[] data = this.output.toByteArray();
			this.response.writeText(data);
			output.close();
		} catch(IOException e) {
			// Ignore
		}
	}
}