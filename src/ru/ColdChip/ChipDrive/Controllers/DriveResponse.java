package ru.ColdChip.ChipDrive.Controllers;

import java.io.IOException;
import ru.ColdChip.WebServer.*;

public class DriveResponse {
	private Response response;
	public DriveResponse(Response response) {
		this.response = response;
	}

	public void setHeader(String key, String val) {
		if(key.equals("status")) {
			this.response.setStatus(Integer.parseInt(val));
		}
		this.response.setHeader(key, val);
	}
	public void write(String text) throws IOException {
		this.response.write(text);
	}
	public void write(byte[] buf) throws IOException {
		this.response.write(buf);
	}
	public void write(byte[] buf, int offset, int length) throws IOException {
		this.response.write(buf, offset, length);
	}
	public void flush() throws IOException {
		this.response.flush();
	}
}