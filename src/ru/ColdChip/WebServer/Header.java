/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.util.*;

public class Header {
	public String method;
	public String path;
	public String version;
	public HashMap<String, String> postQuery = new HashMap<String, String>();
	public HashMap<String, String> query = new HashMap<String, String>();
	public HashMap<String, String> headers = new HashMap<String, String>();

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return this.method;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public boolean containsHeader(String key) {
		return headers.containsKey(key);
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

}