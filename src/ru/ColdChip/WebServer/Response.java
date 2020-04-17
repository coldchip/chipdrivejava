/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.io.*;
import java.nio.file.*;
import java.net.*;
import ru.ColdChip.WebServer.ChipSession.SimpleSession;

public class Response {
	public OutputStream stream;
	private int status = 200;
	private boolean setSession = false;
	private String sessionKey = "";
	private String contentType = "text/html";
	public Request req;
	public Response(OutputStream stream) {
		this.stream = stream;
	}
	public void writeHeader(String data) throws IOException {
		data += "\r\n";
		this.stream.write(data.getBytes(), 0, data.length());
	}
	public void writeByte(byte[] data) throws IOException {
		this.stream.write(data, 0, data.length);
	}
	public void writeByte(byte[] data, int offset, int length) throws IOException {
		this.stream.write(data, offset, length);
	}
	public void setSession(String data) {
		this.sessionKey = data;
		this.setSession = true;
	}
	public void destroySession() {
		this.sessionKey = "null";
		this.setSession = true;
	}
	public void flush() throws IOException {
		this.stream.flush();
	}
	public void writeText(String data) throws IOException {

		writeHeader("HTTP/1.1 " + this.status + " OK");
		writeHeader("Content-Type: " + this.contentType);
		writeHeader("Content-Length: " + data.length());
		writeHeader("Cache-Control: no-store");
		writeHeader("Connection: Keep-Alive");
		writeHeader("Keep-Alive: timeout=5, max=97");
		writeHeader("Server: ColdChip Web Servlet/CWS 1.2");
		writeHeader("");
		if(this.setSession == true) {
			writeHeader("Set-Cookie: session=" + this.sessionKey + "; path=/; expires=Fri, 31 Dec 9999 23:59:59 GMT\r\n");
		}
		this.stream.write(data.getBytes(), 0, data.length());
		this.stream.flush();
	}
	public void redirect(String loc) throws IOException {
		writeHeader("HTTP/1.1 302 Found");
		writeHeader("Content-Type: " + this.contentType);
		writeHeader("Content-Length: 0");
		writeHeader("Cache-Control: no-store");
		writeHeader("Connection: Keep-Alive");
		writeHeader("Keep-Alive: timeout=5, max=97");
		writeHeader("Location: " + loc);
		writeHeader("Server: ColdChip Web Servlet/CWS 1.2");
		writeHeader("");
		if(this.setSession == true) {
			writeHeader("Set-Cookie: session=" + this.sessionKey + "; path=/; expires=Fri, 31 Dec 9999 23:59:59 GMT\r\n");
		}
		this.stream.flush();
	}
	public void status(int code) {
		this.status = code;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getMimeType(File file) throws IOException {
		String mimeType = Files.probeContentType(file.toPath());
		if(mimeType == null) {
			return "application/octet-stream";
		} else {
			return mimeType;
		}
	}

	public void serve(String root) throws IOException {
		try {
			String path = pathNormalize(this.req.getHeader().getPath());
			File target = new File(pathNormalize(root + "/" + path));
			if(target.exists() == true && target.isDirectory() == false) {
				writeFile(pathNormalize(root + "/" + path));
			} else {
				writeFile(pathNormalize(root + "/" + path + "/index.html"));
			}
		} catch(FileNotFoundException e) {
			setContentType("application/json");
			status(404);
			writeText("Error 404");
		} catch(Exception e) {
			setContentType("application/json");
			status(500);
			writeText("500 Internal server error");
		}
	}

	public static String pathNormalize(String path) {
		Path result = Paths.get(path).normalize();
		return result.toString().replaceAll("^/*", "").replaceAll("/*$", "").replaceAll("\\\\", "/");
	}

	public void writeFile(String fileName) throws IOException, FileNotFoundException {
		File file = new File(fileName);
		RandomAccessFile handler = new RandomAccessFile(file, "r");
		long objectSize = file.length();
		long start = 0;
		long end = objectSize - 1;
		if(this.req.getHeader().containsHeader("range")) {
			try {
				start = Integer.parseInt(this.req.getHeader().getHeader("range").split("=")[1].split("-")[0].replaceAll("[^0-9]", ""));
				end = Integer.parseInt(this.req.getHeader().getHeader("range").split("=")[1].split("-")[1].replaceAll("[^0-9]", ""));
			} catch(Exception e) {}
			if((start >= 0 && start < objectSize) && (end > 0 && end <= objectSize)) {
				writeHeader("HTTP/1.1 206 Partial Content");
				writeHeader("Accept-Ranges: bytes");
				writeHeader("Content-Range: bytes " + start + "-" + (end) + "/" + objectSize);
			} else {
				writeHeader("HTTP/1.1 416 Requested Range Not Satisfiable");
				writeHeader("Accept-Ranges: bytes");
				return;
			}
		} else {
			writeHeader("HTTP/1.1 200 OK");
			writeHeader("Content-Disposition: inline; filename=\"" + new File(fileName).getName() + "\"");
		}
		writeHeader("Content-Type: " + getMimeType(file));
		writeHeader("Content-Length: " + ((end - start) + 1));
		writeHeader("Cache-Control: no-store");
		writeHeader("Connection: Keep-Alive");
		writeHeader("Keep-Alive: timeout=5, max=97");
		writeHeader("Server: ColdChip Web Servlet/CWS 1.2");
		writeHeader("");
		int buffer = 1048576 * 10;
		byte[] b;
		handler.seek(start);
		for(long p = start; p < end; p += buffer) {
			if((p + buffer) > end) {
				buffer = (int)(end - p) + 1;
				b = new byte[buffer];
			} else {
				b = new byte[buffer];
			}
			handler.read(b, 0, buffer);
			writeByte(b);
			
		}
		handler.close();
	}
}