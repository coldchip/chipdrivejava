package ru.ColdChip.ChipDrive.Controllers;

import org.JSON.*;
import java.io.IOException;
import ru.ColdChip.WebServer.*;
import java.util.*;

public class DriveRequest {
	private Request request;
	private HashMap<Integer, String> property = new HashMap<Integer, String>();

	public static final int STATUS              = 0 << 0;
	public static final int CONTENT_LENGTH      = 1 << 0;
	public static final int RANGE_START         = 2 << 0;
	public static final int RANGE_END           = 3 << 0;
	public static final int FOLDER_ID           = 4 << 0;
	public static final int FILE_ID             = 5 << 0;
	public static final int NAME                = 6 << 0;
	public static final int ITEM_ID             = 7 << 0;
	public static final int USERNAME            = 8 << 0;
	public static final int PASSWORD            = 9 << 0;
	public static final int AUTH_TOKEN          = 10 << 0;

	public DriveRequest(Request request) {
		this.request = request;
		if(request.hasHeader("content-length")) {
			property.put(DriveRequest.CONTENT_LENGTH, request.getHeader("content-length"));
		}
		if(request.hasRangeStart()) {
			property.put(DriveRequest.RANGE_START, Long.toString(request.getRangeStart()));
		} 
		if(request.hasRangeEnd()) {
			property.put(DriveRequest.RANGE_END, Long.toString(request.getRangeEnd()));
		}
		if(request.hasPost("username")) {
			property.put(DriveRequest.USERNAME, request.getPost("username"));
		}
		if(request.hasPost("password")) {
			property.put(DriveRequest.PASSWORD, request.getPost("password"));
		}
		if(request.hasCookie("token")) {
			property.put(DriveRequest.AUTH_TOKEN, request.getCookie("token"));
		}
		
		try {
			if(request.hasValue("props")) {
				JSONObject props = new JSONObject(request.getValue("props"));
				if(props.has("folderid")) {
					property.put(DriveRequest.FOLDER_ID, props.getString("folderid"));
				}
				if(props.has("fileid")) {
					property.put(DriveRequest.FILE_ID, props.getString("fileid"));
				}
				if(props.has("name")) {
					property.put(DriveRequest.NAME, props.getString("name"));
				}
				if(props.has("itemid")) {
					property.put(DriveRequest.ITEM_ID, props.getString("itemid"));
				}
			}
		} catch(JSONException e) {
			
		}
	}
	
	public int read(byte[] buf, int offset, int length) throws IOException {
		return this.request.stream.read(buf, offset, length);
	}
	public boolean hasParam(int key) {
		return this.property.containsKey(key);
	}
	public String getParam(int key) {
		return this.property.get(key);
	}
}