package ru.ColdChip.ChipDrive.Controllers;

import java.util.Iterator;
import ru.ColdChip.ChipDrive.Exceptions.ChipDriveException;
import java.util.ArrayList;
import org.JSON.*;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import ru.ColdChip.ChipDrive.api.ChipFS;

public class Node extends NodeTable {
	public static int FOLDER = 0;
	public static int FILE = 1;

	private String name = "";
	private String id = "";
	private int type = 0;
	private boolean deletable = true;
	private String parentid = "";
	private JSONArray childrens = new JSONArray();
	private String owner = "";
	private long creationDate = 0l;
	private ChipFS api;
	private static ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
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
		json.put("objectType", this.type);
		json.put("creationDate", this.creationDate);
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
	public void addChild(Node node) {
		this.childrens.put(node.getID());
	}
	private JSONArray getChild() {
		return this.childrens;
	}
	public ArrayList<String> list() throws ChipDriveException {
		if(super.has(this.getID()) == true) {
			if(this.getType() == Node.FOLDER) {
				JSONArray folderChildrens = this.getChild();
				
				ArrayList<String> results = new ArrayList<String>();
				
				for(int i = 0; i < folderChildrens.length(); i++) {
					String id = folderChildrens.getString(i);
					if(super.has(id) == true) {
						results.add(id);
					}
				}
				return results;
			} else {
				throw new ChipDriveException("::objectType:: is not a type of folder");
			}
		} else {
			throw new ChipDriveException("Folder doesn't exist");
		}
	}

	public void rename(String name) throws ChipDriveException {
		if(name.length() > 0 && name.length() < 8192) {
			String parentID = this.getParentID();
			if(super.has(parentID) == true) {
				Node parent = super.get(parentID);
				if(parent.hasChild(name) == true) {
					throw new ChipDriveException("Object already existed");
				}
			}
			this.setName(name);
		} else {
			throw new ChipDriveException("Name too long");
		}
	}

	public String createFile(String name) throws ChipDriveException {
		if(this.hasChild(name) == false && name.length() > 0 && name.length() < 8192) {
			String random = this.randomString(32);

			Node node = new Node(random);
			node.setID(random);
			node.setName(name);
			node.setType(Node.FILE);
			node.setParentID(this.getID());
			node.setOwner(this.getOwner());
			node.setCreationDate(System.currentTimeMillis() / 1000);
			super.put(node);

			this.addChild(node);
			super.put(this);

			return random;
		} else {
			throw new ChipDriveException("Object already existed");
		}
	}

	public String createFolder(String name) throws ChipDriveException {
		if(this.hasChild(name) == false && name.length() > 0 && name.length() < 8192) {
			String random = randomString(32);

			Node node = new Node(random);
			node.setID(random);
			node.setName(name);
			node.setType(Node.FOLDER);
			node.setParentID(this.getID());
			node.setOwner(this.getOwner());
			node.setCreationDate(System.currentTimeMillis() / 1000);
			super.put(node);

			this.addChild(node);
			super.put(this);

			return random;
		} else {
			throw new ChipDriveException("Object already existed");
		}
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
	public void delete() throws ChipDriveException {
		try {
			if(this.getDeletable() == true) {
				String parentID = this.getParentID();
				if(super.has(parentID) == true) {
					Node node = super.get(parentID);
					node.removeChild(this.getID());
					super.put(node);
				}
				if(this.getType() == Node.FILE) {
					super.remove(this.id);
				} else {
					ArrayList<String> items = list();
					for(String item : items) {
						Node currentNode = super.get(item);
						currentNode.delete();
					}
					super.remove(this.id);
				}
			} else {
				throw new ChipDriveException("Item is not deletable");
			}
		} catch(Exception e) {
			throw new ChipDriveException("Unable to delete item(s)");
		}
	}

	private static String randomString(int length) {
		final String list = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
		StringBuilder results = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int character = (int)(Math.random() * list.length());
			results.append(list.charAt(character));
		}
		return results.toString();
	}
}