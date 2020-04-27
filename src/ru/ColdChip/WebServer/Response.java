/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.util.*;
import ru.ColdChip.ChipDrive.Constants.MimeTypes;

public class Response {
	private OutputStream stream;
	private int status = 200;
	private String contentType = "text/html";
	public Request req;
	private boolean isHeaderSent = false;
	private HashMap<String, String> headers = new HashMap<String, String>();

	public Response(OutputStream stream) {
		this.stream = stream;
	}
	public Request getRequest() {
		return this.req;
	}
	public void setHeader(String key, String val) {
		key = key.toLowerCase();
		this.headers.put(key, val);
	}
	public String buildHeader() {
		String output = "";
		switch(this.status) {
			case 200:{
				output += "HTTP/1.1 200 OK\r\n";
			}
			break;
			case 404:{
				output += "HTTP/1.1 404 Not Found\r\n";
			}
			break;
			case 206:{
				output += "HTTP/1.1 206 Partial Content\r\n";
			}
			break;
			case 416:{
				output += "HTTP/1.1 416 Range Not Satisfiable\r\n";
			}
			break;
			default: {
				output += "HTTP/1.1 501 Not Implemented\r\n";
			}
			break;
		}
		for(Map.Entry<String, String> entry : this.headers.entrySet()) {
			output += (entry.getKey() + ": " + entry.getValue() + "\r\n");
		}
		output += "\r\n";
		return output;
	}
	public void writeByte(byte[] data) throws IOException {
		if(this.isHeaderSent == false) {
			String header = buildHeader();
			this.stream.write(header.getBytes(), 0, header.length());
			this.isHeaderSent = true;
		}
		this.stream.write(data, 0, data.length);
	}
	public void writeByte(byte[] data, int offset, int length) throws IOException {
		if(this.isHeaderSent == false) {
			String header = buildHeader();
			this.stream.write(header.getBytes(), 0, header.length());
			this.isHeaderSent = true;
		}
		this.stream.write(data, offset, length);
	}
	public void flush() throws IOException {
		this.stream.flush();
	}
	public void writeText(String data) throws IOException {

		setStatus(getStatus());
		setHeader("Content-Type", getContentType());
		setHeader("Content-Length", Integer.toString(data.length()));
		setHeader("Cache-Control", "no-store");
		setHeader("Connection", "Keep-Alive");
		setHeader("Keep-Alive", "timeout=5, max=97");
		setHeader("Server", "ColdChip Web Servlet/CWS 1.2");
		writeByte(data.getBytes(), 0, data.length());
		this.stream.flush();
	}
	public void writeText(byte[] data) throws IOException {

		setStatus(getStatus());
		setHeader("Content-Type", getContentType());
		setHeader("Content-Length", Integer.toString(data.length));
		setHeader("Cache-Control", "no-store");
		setHeader("Connection", "Keep-Alive");
		setHeader("Keep-Alive", "timeout=5, max=97");
		setHeader("Server", "ColdChip Web Servlet/CWS 1.2");
		writeByte(data, 0, data.length);
		this.stream.flush();
	}
	public void redirect(String loc) throws IOException {
		setStatus(302);
		setHeader("Content-Type", getContentType());
		setHeader("Content-Length", "0");
		setHeader("Cache-Control", "no-store");
		setHeader("Connection", "Keep-Alive");
		setHeader("Keep-Alive", "timeout=5, max=97");
		setHeader("Location", loc);
		setHeader("Server", "ColdChip Web Servlet/CWS 1.2");
		if(this.isHeaderSent == false) {
			String header = buildHeader();
			this.stream.write(header.getBytes(), 0, header.length());
			this.stream.flush();
			this.isHeaderSent = true;
		}
	}
	public void setStatus(int code) {
		this.status = code;
	}
	public int getStatus() {
		return this.status;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentType() {
		return this.contentType;
	}

	public void serve(String root) throws IOException {
		try {
			String path = pathNormalize(getRequest().getPath());
			File target = new File(pathNormalize(root + "/" + path));
			if(target.exists() == true && target.isDirectory() == false) {
				writeFile(pathNormalize(root + "/" + path));
			} else {
				writeFile(pathNormalize(root + "/" + path + "/index.html"));
			}
		} catch(FileNotFoundException e) {
			setContentType("application/json");
			setStatus(404);
			writeText("Error 404");
		} catch(Exception e) {
			setContentType("application/json");
			setStatus(500);
			writeText("500 Internal server error");
		}
	}

	public static String pathNormalize(String path) {
		Path result = Paths.get(path).normalize();
		return result.toString().replaceAll("^/*", "").replaceAll("/*$", "").replaceAll("\\\\", "/");
	}

	private static String getExtension(String name) {
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
		    return ""; // empty extension
		}
		return name.substring(lastIndexOf + 1);
	}

	public void writeFile(String fileName) throws IOException, FileNotFoundException {
		File file = new File(fileName);
		RandomAccessFile handler = new RandomAccessFile(file, "r");
		long objectSize = file.length();
		long start = 0;
		long end = objectSize - 1;
		if(getRequest().hasRangeStart() || getRequest().hasRangeEnd()) {
			if(getRequest().hasRangeStart()) {
				start = getRequest().getRangeStart();
			}
			if(getRequest().hasRangeEnd()) {
				end = getRequest().getRangeEnd();
			}
			if((start >= 0 && start < objectSize) && (end > 0 && end <= objectSize)) {
				setStatus(206);
				setHeader("Accept-Ranges", "bytes");
				setHeader("Content-Range", "bytes " + (start) + "-" + (end) + "/" + objectSize);
			} else {
				setStatus(416);
				setHeader("Accept-Ranges", "bytes");
				return;
			}
		} else {
			setStatus(200);
			setHeader("Content-Disposition", "inline; filename=\"" + new File(fileName).getName() + "\"");
		}
		setHeader("Content-Type", MimeTypes.get(getExtension(new File(fileName).getName()).toLowerCase()));
		setHeader("Content-Length", Long.toString(((end - start) + 1)));
		setHeader("Cache-Control", "no-store");
		setHeader("Connection", "Keep-Alive");
		setHeader("Keep-Alive", "timeout=5, max=97");
		setHeader("Server", " ColdChip Web Servlet/CWS 1.2");
		int buffer = 1048576 * 10;
		byte[] b = new byte[buffer];
		handler.seek(start);
		for(long p = start; p < end; p += buffer) {
			int toRead = (int)Math.min(buffer, (end - p) + 1);
			handler.read(b, 0, toRead);
			writeByte(b, 0, toRead);
			flush();
		}
		handler.close();
	}
}