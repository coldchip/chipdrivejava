package ru.ColdChip.ChipDrive.Controllers;

import java.util.Iterator;
import ru.ColdChip.ChipDrive.Exceptions.ChipDriveException;
import java.util.ArrayList;
import org.JSON.*;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import ru.ColdChip.ChipDrive.api.ChipFS;

public class Node extends NodeRoot {
	public static final int FOLDER = 0;
	public static final int FILE = 1;

	protected String name = "";
	protected String id = "";
	protected int type = 0;
	protected boolean deletable = true;
	protected String parentid = "";
	protected JSONArray childrens = new JSONArray();
	protected String owner = "";
	protected long creationDate = 0l;
	public Node() {
		
	}
	public Node(String id) {
		if(id.equals("")) {
			id = "home";
		}
		this.id = id;
	}
	public Node(JSONObject json) {
		this.name = json.getString("displayName");
		this.id = json.getString("id");
		this.owner = json.getString("owner");
		this.parentid = json.getString("parentid");
		this.type = json.getInt("objectType");
		this.deletable = json.getBoolean("deletable");
		this.creationDate = json.getLong("creationDate");
		this.childrens = json.getJSONArray("children");
	}
	
	public JSONObject export() {
		JSONObject json = new JSONObject();
		json.put("displayName", this.name);
		json.put("id", this.id);
		json.put("deletable", this.deletable);
		json.put("owner", this.owner);
		json.put("parentid", this.parentid);
		json.put("children", this.childrens);
		json.put("creationDate", this.creationDate);
		if(this instanceof FolderObject) {
			json.put("objectType", 0);
		} else {
			json.put("objectType", 1);
		}
		return json;
	}
	public int getType() {
		return this.type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setParentID(String parentid) {
		this.parentid = parentid;
	}
	public String getParentID() {
		return this.parentid;
	}
	public String getID() {
		return this.id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return this.owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public long getCreationDate() {
		return this.creationDate;
	}
	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}
	public boolean getDeletable() {
		return this.deletable;
	}
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}
}