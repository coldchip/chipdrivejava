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
	public boolean containsHeader(String key) {
		return this.header.containsHeader(key);
	}
	public String getPath() {
		return this.header.getPath();
	}
	public long getRangeStart() throws Exception {
		return Long.parseLong(getHeader("range").split("=")[1].split("-")[0].replaceAll("[^0-9]", ""));
	}
	public long getRangeEnd() throws Exception {
		return Long.parseLong(getHeader("range").replaceAll("[^0-9]", ""));
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