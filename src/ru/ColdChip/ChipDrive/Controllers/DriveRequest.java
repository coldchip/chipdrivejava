package ru.ColdChip.ChipDrive.Controllers;

import org.JSON.*;
import java.io.IOException;
import ru.ColdChip.WebServer.*;

public class DriveRequest {
	private Request request;
	private String folderid = null;
	private String fileid = null;
	private String itemid = null;
	private String name = null;
	private boolean hasRange = false;
	private long rangeStart = -1;
	private long rangeEnd = -1;
	private long size = 0l;
	public DriveRequest(Request request) {
		this.request = request;
		if(request.hasHeader("content-length")) {
			this.size = Long.parseLong(request.getHeader("content-length").replaceAll("[^0-9]", ""));
		}
		if(this.getRequest().hasRangeStart()) {
			this.rangeStart = this.getRequest().getRangeStart();
		} 
		if(this.getRequest().hasRangeEnd()) {
			this.rangeEnd = this.getRequest().getRangeEnd();
		}
		
		try {
			JSONObject props = new JSONObject(request.getValue("props"));
			try {
				this.folderid = props.getString("folderid");
			} catch(JSONException e) {}
			try {
				this.fileid = props.getString("fileid");
			} catch(JSONException e) {}
			try {
				this.name = props.getString("name");
			} catch(JSONException e) {}
			try {
				this.itemid = props.getString("itemid");
			} catch(JSONException e) {}
		} catch(JSONException e) {

		}
	}
	
	public Request getRequest() {
		return this.request;
	}
	public String getArgs(String data) {
		return this.getRequest().getArgs(data);
	}
	public int read(byte[] buf, int offset, int length) throws IOException {
		return this.getRequest().stream.read(buf, offset, length);
	}
	public boolean hasRangeStart() {
		return this.rangeStart != -1;
	}
	public boolean hasRangeEnd() {
		return this.rangeEnd != -1;
	}
	public long getRangeStart() {
		return this.rangeStart;
	}
	public long getRangeEnd() {
		return this.rangeEnd;
	}
	public long getSize() {
		return this.size;
	}
	public boolean hasFolderID() {
		return this.folderid != null;
	}
	public String getFolderID() {
		return this.folderid;
	}
	public boolean hasFileID() {
		return this.fileid != null;
	}
	public String getFileID() {
		return this.fileid;
	}
	public boolean hasName() {
		return this.name != null;
	}
	public String getName() {
		return this.name;
	}
	public boolean hasItemID() {
		return this.itemid != null;
	}
	public String getItemID() {
		return this.itemid;
	}
}