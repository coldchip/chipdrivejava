/*

	@copyright: ColdChip
	@Author: Ryan Loh 

*/
package ru.ColdChip.WebServer;

import java.util.*;
import java.io.*;
import java.net.HttpCookie;

public class Request {
	// TODO: Move header parser into this class
	public Header header;
	public InputStream stream;
	public Response res;
	private LinkedHashMap<String, String> args;
	private HashMap<String, String> cookies = new HashMap<String, String>();
	public Request(InputStream stream, Header header) {
		this.header  = header;
		this.stream  = stream;
		if(this.hasHeader("cookie")) {
			parseCookie(this.getHeader("cookie"));
		}
	}
	private void parseCookie(String input) {
		String[] cookiePairs = input.split(";");
		for (int i = 0; i < cookiePairs.length; i++)  {
			String[] cookieValue = cookiePairs[i].trim().split("=");
			if(cookieValue.length == 2) {
				this.cookies.put(cookieValue[0], cookieValue[1]);
			}
		}
	}
	public String getCookie(String key) {
		return this.cookies.get(key);
	}
	public boolean hasCookie(String key) {
		return this.cookies.containsKey(key);
	}
	public String getHeader(String key) {
		return this.header.getHeader(key);
	}
	public boolean hasHeader(String key) {
		return this.header.hasHeader(key);
	}
	public String getPath() {
		return this.header.getPath();
	}
	public boolean hasRangeStart() {
		try {
			Long.parseLong(getHeader("range").split("=")[1].split("-")[0].replaceAll("[^0-9]", ""));
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	public boolean hasRangeEnd() {
		try {
			Long.parseLong(getHeader("range").split("=")[1].split("-")[1].replaceAll("[^0-9]", ""));
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	public long getRangeStart() {
		if(this.hasRangeStart()) {
			return Long.parseLong(getHeader("range").split("=")[1].split("-")[0].replaceAll("[^0-9]", ""));
		} else {
			return 0;
		}
	}
	public long getRangeEnd() {
		if(this.hasRangeEnd()) {
			return Long.parseLong(getHeader("range").split("=")[1].split("-")[1].replaceAll("[^0-9]", ""));
		} else {
			return 0;
		}
	}
	public void setArgs(LinkedHashMap<String, String> val) {
		this.args = val;
	}
	public String getArgs(String val) {
		return this.args.get(val);
	}
	public boolean hasValue(String key) {
		return this.header.query.containsKey(key);
	}
	public String getValue(String key) {
		return this.header.query.get(key);
	}
	public boolean hasPost(String key) {
		return this.header.postQuery.containsKey(key);
	}
	public String getPost(String key) {
		return this.header.postQuery.get(key);
	}
	
}