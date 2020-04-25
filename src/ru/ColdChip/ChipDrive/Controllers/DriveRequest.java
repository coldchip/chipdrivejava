// Class to bridge between the request of ChipDrive API

package ru.ColdChip.ChipDrive.Controllers;

import org.JSON.*;
import ru.ColdChip.WebServer.Request;
import ru.ColdChip.ChipDrive.Exceptions.*;

public class DriveRequest {
	private String folderid = null;
	private String fileid = null;
	private String itemid = null;
	private String name = null;
	private boolean range = false;
	private long rangeStart = 0;
	private long rangeEnd = 0;
	public DriveRequest(Request request) {
		String propsJSON = request.getValue("props");
		try {
			JSONObject json = new JSONObject(propsJSON);
			if(json.has("folderid")) {
				this.setFolderID(json.getString("folderid"));
			}
			if(json.has("fileid")) {
				this.setFileID(json.getString("fileid"));
			}
			if(json.has("itemid")) {
				this.setItemID(json.getString("itemid"));
			}
			if(json.has("name")) {
				this.setName(json.getString("name"));
			}
			if(request.containsHeader("range")) {
				this.setRange(true);
				try {
					this.setRangeStart(request.getRangeStart());
					this.setRangeStart(request.getRangeEnd());
				} catch(Exception e) {}
			}
		} catch(JSONException e) {

		}
	}
	public void setFolderID(String folderid) {
		this.folderid = folderid;
	}
	public String getFolderID() throws ChipDriveException {
		return this.folderid;
	}
	public void setFileID(String fileid) {
		this.fileid = fileid;
	}
	public String getFileID() throws ChipDriveException {
		return this.fileid;
	}
	public void setItemID(String itemid) {
		this.itemid = itemid;
	}
	public String getItemID() throws ChipDriveException {
		return this.itemid;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() throws ChipDriveException {
		return this.name;
	}
	public void setRange(boolean range) {
		this.range = range;
	}
	public boolean getRange() throws ChipDriveException {
		return this.range;
	}
	public void setRangeStart(long range) {
		this.rangeStart = range;
	}
	public long getRangeStart() throws ChipDriveException {
		return this.rangeStart;
	}
	public void setRangeEnd(long range) {
		this.rangeEnd = range;
	}
	public long getRangeEnd() throws ChipDriveException {
		return this.rangeEnd;
	}
	
}