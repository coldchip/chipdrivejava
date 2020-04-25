package ru.ColdChip.ChipDrive.Object;

import org.JSON.JSONObject;
import org.JSON.JSONArray;
import java.util.*;
import ru.ColdChip.ChipDrive.Exceptions.ChipDriveException;;

public class FolderObject extends Node {
	public FolderObject() {
		
	}
	public FolderObject(String id) {
		if(id.equals("")) {
			id = "home";
		}
		super.setID(id);
	}
	public FolderObject(JSONObject json) {
		super.setName(json.getString("displayName"));
		super.setID(json.getString("id"));
		super.setOwner(json.getString("owner"));
		super.setParentID(json.getString("parentid"));
		super.setType(json.getInt("objectType"));
		super.setDeletable(json.getBoolean("deletable"));
		super.setCreationDate(json.getLong("creationDate"));
		this.setChild(json.getJSONArray("children"));
	}

	public ArrayList<String> list() throws ChipDriveException {
		JSONArray folderChildrens = this.getChild();
		
		ArrayList<String> results = new ArrayList<String>();
		
		for(int i = 0; i < folderChildrens.length(); i++) {
			String id = folderChildrens.getString(i);
			results.add(id);
		}
		return results;
	}
	public boolean hasChild(String name) throws ChipDriveException {
		ArrayList<String> files = this.list();
		for(String id : files) {
			Node currentID = super.get(id);
			if(currentID.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	public boolean hasChildID(String id) throws ChipDriveException {
		
		ArrayList<String> files = this.list();
		for(String currentID : files) {
			if(currentID.equals(id)) {
				return true;
			}
		}
		return false;	
	}

	public void removeChild(String id) {
		for(int i = 0; i < this.childrens.length(); i++) {
			String currentID = this.childrens.getString(i);
			if(id.equals(currentID)) {
				this.childrens.remove(i);
				return;
			}
		}
	}
	public void addChild(Node node) {
		this.childrens.put(node.getID());
	}
	public void setChild(JSONArray child) {
		this.childrens = child;
	}
	public JSONArray getChild() {
		return this.childrens;
	}
}