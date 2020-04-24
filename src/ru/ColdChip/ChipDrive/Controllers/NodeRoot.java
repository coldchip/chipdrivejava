package ru.ColdChip.ChipDrive.Controllers;

import org.JSON.*;
import ru.ColdChip.ChipDrive.Exceptions.*;

public class NodeRoot {
	private static JSONObject list = new JSONObject();

	public void put(Node node) {
		JSONObject json = node.export();
		list.put(node.getID(), json);
	}

	public Node get(String id) throws ChipDriveException {
		System.out.println(this.list.toString(4));
		if(id.equals("")) {
			id = "home";
		}
		if(list.has(id)) {
			JSONObject json = list.getJSONObject(id);
			Node node = new Node(json);
			if(node.getType() == Node.FILE) {
				return (FileObject)new FileObject(json);
			} else if(node.getType() == Node.FOLDER) {
				return (FolderObject)new FolderObject(json);
			} else {
				return (Node)node;
			}
		} else {
			throw new ChipDriveException("Object not found");
		}
	}

	public boolean has(String id) {
		if(id.equals("")) {
			id = "home";
		}
		return list.has(id);
	}

	public void remove(String id) throws ChipDriveException {
		if(list.has(id)) {
			list.remove(id);
		} else {
			throw new ChipDriveException("Object not found");
		}
	}

}