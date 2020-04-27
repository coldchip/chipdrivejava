/*

	@copyright: ColdChip
	@Author: Ryan Loh 

*/
package ru.ColdChip.WebServer;

import java.util.*;
import java.io.*;

public class Request {
	public Header header;
	public InputStream stream;
	public Response res;
	private LinkedHashMap<String, String> args;
	public Request(InputStream stream, Header header) {
		this.header = header;
		this.stream = stream;
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
	public String getValue(String key) {
		String results = "";
		if(this.header.query.containsKey(key) == true) {
			results = this.header.query.get(key);
		}
		return results;
	}
	public String getPost(String key) {
		String results = "";
		if(this.header.postQuery.containsKey(key) == true) {
			results = this.header.postQuery.get(key);
		}
		return results;
	}
	
}