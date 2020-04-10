package ru.ColdChip.ChipDrive.Controllers;

import java.util.Iterator;
import ru.ColdChip.ChipDrive.Exceptions.ChipDriveException;
import java.util.ArrayList;
import org.JSON.JSONObject;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.JSON.JSONArray;
import ru.ColdChip.ChipDrive.api.ChipFS;

public class Node {
	private String name = "";
	private String id = "";
	private String type = "";
	private boolean deletable = true;
	private String parentid = "";
	private JSONArray childrens = new JSONArray();
	private String owner = "";
	private long creationDate = 0l;
	private ChipFS api;
	private static ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	public Node(String id, ChipFS api) throws IOException {
		if(id.equals("")) {
			id = "home";
		}
		this.api = api;
		this.id = id;
	}
	public boolean exists() throws ChipDriveException {
		return api.exists(this.id + ".db");
	}
	public void load() throws ChipDriveException {
		if(exists()) {
			rwlock.readLock().lock();
			try {
				long dataSize = api.size(this.id + ".db");
				byte[] b = new byte[(int)dataSize];
				api.read(this.id + ".db", b, 0, (int)dataSize);
				JSONObject data = new JSONObject(new String(b));
				this.name = data.getString("displayName");
				this.id = data.getString("id");
				this.owner = data.getString("owner");
				this.parentid = data.getString("parentid");
				this.type = data.getString("objectType");
				this.deletable = data.getBoolean("deletable");
				this.creationDate = data.getLong("creationDate");
				this.childrens = data.getJSONArray("children");
			} catch(IOException e) {
				throw new ChipDriveException("Database connection error");
			} finally {
				rwlock.readLock().unlock();
			}
		} else {
			throw new ChipDriveException("Object doesn't exist");
		}
	}
	public void update() throws IOException {
		rwlock.writeLock().lock();
		try {
			JSONObject returnData = new JSONObject();
			returnData.put("displayName", this.name);
			returnData.put("id", this.id);
			returnData.put("deletable", this.deletable);
			returnData.put("owner", this.owner);
			returnData.put("parentid", this.parentid);
			returnData.put("children", this.childrens);
			returnData.put("objectType", this.type);
			returnData.put("creationDate", this.creationDate);
			String stringifiedData = returnData.toString();
			api.delete(this.id + ".db");
			api.create(this.id + ".db");
			api.write(this.id + ".db", stringifiedData.getBytes(), 0, stringifiedData.length());
		} finally {
			rwlock.writeLock().unlock();
		}
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
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
		try {
			if(exists() == true) {
				if(getType().equals("folder")) {
					JSONArray folderChildrens = getChild();
					
					ArrayList<String> results = new ArrayList<String>();
					
					for(int i = 0; i < folderChildrens.length(); i++) {
						String id = folderChildrens.getString(i);
						if(new Node(id, api).exists() == true) {
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
		} catch(IOException e) {
			throw new ChipDriveException("Database Connection Error");
		}
	}

	public void rename(String name) throws ChipDriveException {
		try {
			if(name.length() > 0 && name.length() < 8192) {
				Node parent = new Node(getParentID(), api);
				if(parent.exists() == true) {
					parent.load();
					if(parent.hasChild(name) == true) {
						throw new ChipDriveException("Object already existed");
					}
				}
				setName(name);
				update();
			} else {
				throw new ChipDriveException("Name too long");
			}
		} catch(IOException e) {
			throw new ChipDriveException("Unable to connect to database");
		}
	}

	public long getSize() throws ChipDriveException {
		try {
			if(getType().equals("file")) {
				return api.size(getID());
			} else {
				long folderSize = 0l;
				ArrayList<String> items = list();
				for(String item : items) {
					Node currentNode = new Node(item, api);
					currentNode.load();
					folderSize += currentNode.getSize();
				}
				return folderSize;
			}
		} catch(IOException e) {
			throw new ChipDriveException("Compute value error");
		} 
	}

	public String createFile(String name) throws ChipDriveException {
		try {
			if(hasChild(name) == false && name.length() > 0 && name.length() < 8192) {
				String random = randomString(32);

				Node fn = new Node(random, api);
				fn.setID(random);
				fn.setName(name);
				fn.setType("file");
				fn.setParentID(getID());
				fn.setOwner(getOwner());
				fn.setCreationDate(System.currentTimeMillis() / 1000);
				fn.update();

				addChild(fn);
				update();

				return random;
			} else {
				throw new ChipDriveException("Object already existed");
			}
		} catch(IOException e) {
			throw new ChipDriveException("Unable to connect to database");
		}
	}

	public String createFolder(String name) throws ChipDriveException {
		try {
			if(hasChild(name) == false && name.length() > 0 && name.length() < 8192) {
				String random = randomString(32);

				Node fn = new Node(random, api);
				fn.setID(random);
				fn.setName(name);
				fn.setType("folder");
				fn.setParentID(getID());
				fn.setOwner(getOwner());
				fn.setCreationDate(System.currentTimeMillis() / 1000);
				fn.update();

				addChild(fn);
				update();

				return random;
			} else {
				throw new ChipDriveException("Object already existed");
			}
		} catch(IOException e) {
			throw new ChipDriveException("Unable to connect to database");
		}
	}

	public boolean hasChild(String name) throws ChipDriveException {
		
		ArrayList<String> files = list();
		try {
			for(String id : files) {
				Node currentID = new Node(id, api);
				currentID.load();
				if(currentID.getName().equals(name)) {
					return true;
				}
			}
			return false;
		} catch(IOException e) {
			throw new ChipDriveException("Unable to connect to database");
		}
	}

	public boolean hasChildID(String id) throws ChipDriveException {
		
		ArrayList<String> files = list();
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
			if(getDeletable() == true) {
				Node parent = new Node(getParentID(), api);
				if(parent.exists() == true) {
					parent.load();
					parent.removeChild(getID());
					parent.update();
				}
				if(getType().equals("file")) {
					api.delete(this.id + ".db");
					api.delete(this.id);
				} else {
					ArrayList<String> items = list();
					for(String item : items) {
						Node currentNode = new Node(item, api);
						currentNode.load();
						currentNode.delete();
					}
					api.delete(this.id + ".db");
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