package ru.ColdChip.ChipDrive.Controllers;

import java.util.*;

public class DriveSession {
	private HashMap<String, DriveUser> sessions = new HashMap<String, DriveUser>();

	public void addUser(String token, DriveUser user) {
		synchronized(this.sessions) {
 			this.sessions.put(token, user);
 		}
	}
	public void removeUser(DriveUser user) {
		synchronized(this.sessions) {
			this.sessions.values().remove(user);
		}
	}
	public boolean hasUser(String token) {
		synchronized(this.sessions) {
			return this.sessions.containsKey(token);
		}
	}
	public DriveUser getUser(String token) {
		synchronized(this.sessions) {
			return this.sessions.get(token);
		}
	}
}