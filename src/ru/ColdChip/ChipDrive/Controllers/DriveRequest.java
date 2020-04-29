package ru.ColdChip.ChipDrive.Controllers;

import org.JSON.*;
import java.io.IOException;
import ru.ColdChip.WebServer.*;
import java.util.*;

public class DriveRequest {
	private Request request;
	private HashMap<String, String> property = new HashMap<String, String>();

	public DriveRequest(Request request) {
		this.request = request;
		if(request.hasHeader("content-length")) {
			property.put("content.length", request.getHeader("content-length"));
		}
		if(this.request.hasRangeStart()) {
			property.put("range.start", Long.toString(this.request.getRangeStart()));
		} 
		if(this.request.hasRangeEnd()) {
			property.put("range.end", Long.toString(this.request.getRangeEnd()));
		}
		
		try {
			JSONObject props = new JSONObject(request.getValue("props"));
			try {
				property.put("folder.id", props.getString("folderid"));
			} catch(JSONException e) {}
			try {
				property.put("file.id", props.getString("fileid"));
			} catch(JSONException e) {}
			try {
				property.put("name", props.getString("name"));
			} catch(JSONException e) {}
			try {
				property.put("item.id", props.getString("itemid"));
			} catch(JSONException e) {}
		} catch(JSONException e) {

		}
	}
	
	public int read(byte[] buf, int offset, int length) throws IOException {
		return this.request.stream.read(buf, offset, length);
	}
	public boolean hasHeader(String key) {
		return this.property.containsKey(key);
	}
	public String getHeader(String key) {
		return this.property.get(key);
	}
}