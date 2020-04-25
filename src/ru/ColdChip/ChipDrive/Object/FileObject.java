package ru.ColdChip.ChipDrive.Object;

import org.JSON.JSONObject;

public class FileObject extends Node {
	public FileObject() {
		
	}
	public FileObject(String id) {
		if(id.equals("")) {
			id = "home";
		}
		super.setID(id);
	}
	public FileObject(JSONObject json) {
		super.setName(json.getString("displayName"));
		super.setID(json.getString("id"));
		super.setOwner(json.getString("owner"));
		super.setParentID(json.getString("parentid"));
		super.setType(json.getInt("objectType"));
		super.setDeletable(json.getBoolean("deletable"));
		super.setCreationDate(json.getLong("creationDate"));
	}
}