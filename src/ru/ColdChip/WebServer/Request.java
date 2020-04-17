/*

	@copyright: ColdChip
	@Author: Ryan Loh 

*/
package ru.ColdChip.WebServer;

import java.util.*;
import java.io.*;
import java.net.HttpCookie;
import java.util.regex.*;
import ru.ColdChip.WebServer.ChipSession.SimpleSession;
import ru.ColdChip.WebServer.Exceptions.TokenNotFoundException;

public class Request {
	public Header header;
	public InputStream stream;
	public Response res;
	LinkedHashMap<String, String> args;
	public Request(InputStream stream, Header header) {
		this.header = header;
		this.stream = stream;
	}
	public Header getHeader() {
		return this.header;
	}
	public String getValue(String key) {
		String results = "";
		if(getHeader().query.containsKey(key) == true) {
			results = getHeader().query.get(key);
		}
		return results;
	}
	public long getRangeStart() throws Exception {
		return Long.parseLong(getHeader().getHeader("range").split("=")[1].split("-")[0].replaceAll("[^0-9]", ""));
	}
	public long getRangeEnd() throws Exception {
		return Long.parseLong(getHeader().getHeader("range").split("=")[1].split("-")[1].replaceAll("[^0-9]", ""));
	}
	public String getArgs(String val) {
		return this.args.get(val);
	}
	public String getPost(String key) {
		String results = "";
		if(getHeader().postQuery.containsKey(key) == true) {
			results = getHeader().postQuery.get(key);
		}
		return results;
	}
	public boolean isPost(String key) {
		String results = "";
		if(getHeader().postQuery.containsKey(key) == true) {
			return true;
		}
		return false;
	}
}