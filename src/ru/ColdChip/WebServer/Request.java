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
	public String getValue(String key) {
		String results = "";
		if(this.header.query.containsKey(key) == true) {
			results = this.header.query.get(key);
		}
		return results;
	}
	public long getRangeStart() throws Exception {
		return Long.parseLong(getSession("range").split("=")[1].split("-")[0].replaceAll("[^0-9]", ""));
	}
	public long getRangeEnd() throws Exception {
		return Long.parseLong(getSession("range").split("=")[1].split("-")[1].replaceAll("[^0-9]", ""));
	}
	public String getArgs(String val) {
		return this.args.get(val);
	}
	public String getPost(String key) {
		String results = "";
		if(this.header.postQuery.containsKey(key) == true) {
			results = this.header.postQuery.get(key);
		}
		return results;
	}
	public boolean isPost(String key) {
		String results = "";
		if(this.header.postQuery.containsKey(key) == true) {
			return true;
		}
		return false;
	}
	public String getSession() {
		return getCookie("session");
	}
	public String getSession(String key) {
		key = key.toLowerCase();
		String results = new String();
		if(this.header.headers.containsKey(key) == true) {
			results = this.header.headers.get(key).replaceAll("\r", "");
		}
		return results;
	}
	public String getSessionData() throws TokenNotFoundException {
		String session = getSession();
		SimpleSession ss = new SimpleSession();
		if(ss.Validate(session) == true) {
			return ss.GetTokenData(session);
		} else {
			throw new TokenNotFoundException();
		}
	}
	public String getCookie(String key) {
		String command = "(.*)" + key + "=([^;]*)(.*)";
		String input = getSession("cookie");
		Pattern pattern = Pattern.compile(command);
		Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
        	return matcher.group(2);
        }
		return "";
	}
}