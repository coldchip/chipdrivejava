package ru.ColdChip.ChipDrive.Object;

import org.JSON.*;
import ru.ColdChip.ChipDrive.Exceptions.*;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;

public class NodeRoot {

	private String id = "home";

	public NodeRoot() {
		
	}

	public NodeRoot(String id) {
		this.id = id;
	}

	private JSONObject getdb() throws ChipDriveException {
		try {
			File f = new File("buckets/" + this.id + ".json");
			if(!f.exists()){
				f.createNewFile();
				Files.write(Paths.get("buckets/" + this.id + ".json"), new String("{}").getBytes());
			}
			return new JSONObject(new String(Files.readAllBytes(Paths.get("buckets/" + this.id + ".json"))));
		} catch (IOException e) {
			throw new ChipDriveException(e.toString());
        }
	}

	private void putdb(JSONObject data) throws ChipDriveException {
		try {
			File f = new File("buckets/" + this.id + ".json");
			if(!f.exists()){
				f.createNewFile();
				Files.write(Paths.get("buckets/" + this.id + ".json"), new String("{}").getBytes());
			}
			Files.write(Paths.get("buckets/" + this.id + ".json"), data.toString(4).getBytes());
		} catch (IOException e) {
			throw new ChipDriveException(e.toString());
        }
	}

	public void put(Node node) throws ChipDriveException {
		JSONObject json = node.export();
		JSONObject db = this.getdb();
		db.put(node.getID(), json);
        this.putdb(db);
	}

	public Node get(String id) throws ChipDriveException {
		if(id.equals("")) {
			id = "home";
		}
		if(this.has(id)) {
				JSONObject db = this.getdb();
	       		JSONObject json = db.getJSONObject(id);
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

	public boolean has(String id) throws ChipDriveException {
		if(id.equals("")) {
			id = "home";
		}
		JSONObject db = this.getdb();
		if(db.has(id)) {
			return true;
		} else {
			return false;
		}
	}

	public void remove(String id) throws ChipDriveException {
		if(this.has(id)) {
			JSONObject db = this.getdb();
			db.remove(id);
			this.putdb(db);
		} else {
			throw new ChipDriveException("Object not found");
		}
	}

}