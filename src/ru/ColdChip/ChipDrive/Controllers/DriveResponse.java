package ru.ColdChip.ChipDrive.Controllers;

import java.io.IOException;
import ru.ColdChip.WebServer.*;

public class DriveResponse {
	private Response response;
	private String contentType = "application/octet-stream";
	public DriveResponse(Response response) {
		this.response = response;
	}
	public Response getResponse() {
		return this.response;
	}
	public void setStatus(int status) {
		this.getResponse().setStatus(status);
	}
	public void setHeader(String key, String val) {
		this.getResponse().setHeader(key, val);
	}
	public void write(String text) throws IOException {
		this.getResponse().write(text);
	}
	public void write(byte[] buf) throws IOException {
		this.getResponse().write(buf);
	}
	public void write(byte[] buf, int offset, int length) throws IOException {
		this.getResponse().write(buf, offset, length);
	}
	public void flush() throws IOException {
		this.getResponse().flush();
	}
}