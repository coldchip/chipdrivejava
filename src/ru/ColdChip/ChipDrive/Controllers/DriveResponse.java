package ru.ColdChip.ChipDrive.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import ru.ColdChip.WebServer.*;

public class DriveResponse {
	private Response response;
	public static final int STATUS              = 0 << 8;
	public static final int CONTENT_TYPE        = 1 << 8;
	public static final int CONTENT_LENGTH      = 2 << 8;
	public static final int ACCEPT_RANGES       = 3 << 8;
	public static final int CONTENT_RANGE       = 4 << 8;
	public static final int CONTENT_NAME        = 5 << 8;
	public static final int TOKEN               = 6 << 8;

	public DriveResponse(Response response) {
		this.response = response;
	}

	public void setParam(int key, String val) throws UnsupportedEncodingException {
		switch(key) {
			case DriveResponse.STATUS: {
				this.response.setStatus(Integer.parseInt(val));
			} break;
			case DriveResponse.CONTENT_TYPE: {
				this.response.setHeader("Content-Type", val);
			} break;
			case DriveResponse.CONTENT_LENGTH: {
				this.response.setHeader("Content-Length", val);
			} break;
			case DriveResponse.ACCEPT_RANGES: {
				this.response.setHeader("Accept-Ranges", val);
			} break;
			case DriveResponse.CONTENT_RANGE: {
				this.response.setHeader("Content-Range", val);
			} break;
			case DriveResponse.CONTENT_NAME: {
				this.response.setHeader("Content-Disposition", val);
			} break;
			case DriveResponse.TOKEN: {
				this.response.setCookie("token", val);
			} break;
		}
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