package ru.ColdChip.ChipDrive.Controllers;

import java.util.*;

public class DriveSession {
	private HashMap<String, DriveUser> sessions = new HashMap<String, DriveUser>();

	public void addUser(String token, DriveUser user) {
		this.sessions.put(token, user);
	}
	public boolean hasUser(String token) {
		return this.sessions.containsKey(token);
	}
	public DriveUser getUser(String token) {
		return this.sessions.get(token);
	}
}