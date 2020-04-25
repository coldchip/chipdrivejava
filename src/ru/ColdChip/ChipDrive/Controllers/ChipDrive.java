package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;

import ru.ColdChip.ChipDrive.api.ChipFS;

import ru.ColdChip.ChipDrive.Exceptions.*;
import ru.ColdChip.ChipDrive.Object.*;
import ru.ColdChip.ChipDrive.Constants.MimeTypes;
import java.io.*;
import java.util.*;
import java.net.URLEncoder;
import org.JSON.*;

class DriveRequest {

}

class DriveResponse {
	
}

public class ChipDrive extends ChipFS implements IChipDrive {

	public static final int VERSION = 1;
	public static final int CONFIG  = 2;
	public static final int LIST    = 3;
	public static final int LINK    = 4;
	public static final int UPLOAD  = 5;
	public static final int DELETE  = 6;
	public static final int FOLDER  = 7;
	public static final int RENAME  = 8;
	public static final int INFO    = 9;
	public static final int QUOTA   = 10;
	public static final int STREAM  = 11;
	public static final int UNKNOWN = 12;

	private static volatile int threads = 0;

	public ChipDrive() {

	}

	public void enqueue(int method, Request request, Response response) throws IOException {
		try {
			threads++;
			if(threads < 255) {
				this.dispatch(method, request, response);
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Server is overloaded");
				error.put("login", false);
				sendError(response, error);
			}
		} finally {
			threads--;
		}
	}

	private void dispatch(int method, Request request, Response response) throws IOException {
		try {
			switch(method) {
				case ChipDrive.VERSION: {
					this.version(request, response);
				}
				break;
				case ChipDrive.CONFIG: {
					this.config(request, response);
				}
				break;
				case ChipDrive.LIST: {
					if(isAuthed() == true) {
						this.list(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.LINK: {
					if(isAuthed() == true) {
						this.link(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.UPLOAD: {
					if(isAuthed() == true) {
						this.upload(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.DELETE: {
					if(isAuthed() == true) {
						this.delete(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.FOLDER: {
					if(isAuthed() == true) {
						this.folder(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.RENAME: {
					if(isAuthed() == true) {
						this.rename(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.INFO: {
					if(isAuthed() == true) {
						this.info(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.QUOTA: {
					if(isAuthed() == true) {
						this.quota(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.STREAM: {
					if(isAuthed() == true) {
						this.stream(request, response);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.UNKNOWN: {
					throw new ChipDriveException("Unknown Mode");
				}
			}
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		} catch(ChipDriveLoginException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", true);
			sendError(response, error);
		}
	}

	private void version(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		JSONObject config = new JSONObject();
		config.put("version", "1.3.1");
		response.setContentType("application/json");
		response.writeText(config.toString(4));
	}

	private void config(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		JSONObject methods = new JSONObject();
		methods.put("config", "config");
		methods.put("list", "files.list");
		methods.put("streamLink", "file.link");
		methods.put("upload", "new.upload");
		methods.put("delete", "file.delete");
		methods.put("newFolder", "new.folder");
		methods.put("rename", "item.rename");
		methods.put("fileInfo", "item.info");
		methods.put("driveQuota", "drive.quota");

		JSONObject config = new JSONObject();
		config.put("version", "2.2");
		config.put("logo", "https://coldchip.ru/admin/img/logo.png");
		config.put("gateway", "/api/v1/drive/{method}");
		config.put("methods", methods);
		response.setContentType("application/json");
		response.writeText(config.toString(4));
	}

	private void list(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String folderid = propsJSON.getString("folderid");

		NodeRoot root = new NodeRoot();
		if(folderid.equals("") && !root.has(folderid)) {
			FolderObject folder = new FolderObject();
			folder.setName("home");
			folder.setID("home");
			root.put(folder);
		}

		Node node = root.get(folderid);
		if(node instanceof FolderObject) {
			FolderObject folder = (FolderObject)node;
			ArrayList<String> files = folder.list();

			JSONArray list = new JSONArray();

			for(String fileID : files) {
				JSONObject objectData = new JSONObject();
				Node currentNode = root.get(fileID);
				if(currentNode instanceof FolderObject) {
					objectData.put("kind", 0);
				} else {
					objectData.put("kind", 1);
				}
				objectData.put("displayName", currentNode.getName());
				objectData.put("id", fileID);
				list.put(objectData);
			}

			JSONObject meta = new JSONObject();
			meta.put("list", list);
			meta.put("displayName", node.getName());

			sendMessage(response, meta);
		} else {
			throw new ChipDriveException("Not a folder");
		}
	}

	private void link(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String fileid = propsJSON.getString("fileid");

		NodeRoot root = new NodeRoot();
		Node node = root.get(fileid);
		if(node instanceof FileObject) {
			FileObject folder = (FileObject)node;
			String filename = folder.getName();
			JSONObject data = new JSONObject();
			data.put("type", MimeTypes.get(getExtension(filename).toLowerCase()));
			data.put("ext", getExtension(filename).toLowerCase());
			data.put("displayName", filename);
			data.put("url", "/api/v1/drive/item.stream/" + fileid);

			sendMessage(response, data);
		} else {
			throw new ChipDriveException("Not a folder");
		}
	}

	private void upload(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String folderid = propsJSON.getString("folderid");
		String name = propsJSON.getString("name");

		try {
			String randomID = randomString(32);

			NodeRoot root = new NodeRoot();

			FileObject file = new FileObject();
			file.setName(name);
			file.setID(randomID);
			file.setParentID(folderid);
			file.put(file);

			FolderObject rootDir = (FolderObject)root.get(folderid);
			rootDir.addChild(file);
			root.put(rootDir);

			long contentSize = Long.parseLong(request.getHeader("content-length").replaceAll("[^0-9]", ""));
			if(contentSize > 0) {
				long i = 0;
				byte[] data = new byte[8192];
				while (i < contentSize) {
					int read = request.stream.read(data, 0, data.length);
					if(read > 0) {
						write(randomID, data, i, read);
						i += read;
					} else {
						throw new IOException("");
					}
				}
			}	
		} catch(IOException e) {
			throw new ChipDriveException("I/O Error");
		} catch (Exception e) {
			throw new ChipDriveException("Upload Failed");
		}
		sendMessage(response, new JSONObject());
	}

	private void deleteRecursive(Node node) throws ChipDriveException, ChipDriveException, JSONException {
		NodeRoot root = new NodeRoot();
		
		FolderObject parent = (FolderObject)root.get(node.getParentID());
		parent.removeChild(node.getID());
		root.put(parent);

		if(node instanceof FileObject) {
			root.remove(node.getID());
		} else {
			FolderObject folder = (FolderObject)node;
			ArrayList<String> items = folder.list();
			for(String item : items) {
				Node currentNode = root.get(item);
				deleteRecursive(currentNode);
			}
			root.remove(node.getID());
		}
	}

	private void delete(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String itemid = propsJSON.getString("itemid");

		NodeRoot root = new NodeRoot();
		Node node = root.get(itemid);
		deleteRecursive(node);

		sendMessage(response, new JSONObject());
	}

	private void folder(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String folderid = propsJSON.getString("folderid");
		String name = propsJSON.getString("name");

		String randomID = randomString(32);

		NodeRoot root = new NodeRoot();

		FolderObject folder = new FolderObject();
		folder.setName(name);
		folder.setID(randomID);
		folder.setParentID(folderid);
		root.put(folder);

		FolderObject rootDir = (FolderObject)root.get(folderid);
		rootDir.addChild(folder);
		root.put(rootDir);

		sendMessage(response, new JSONObject());
	}

	private void rename(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String itemid = propsJSON.getString("itemid");
		String name = propsJSON.getString("name");

		NodeRoot root = new NodeRoot();
		Node node = root.get(itemid);
		node.setName(name);
		root.put(node);

		sendMessage(response, new JSONObject());
	}

	private void info(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String props = request.getValue("props");
		JSONObject propsJSON = new JSONObject(props);
		String fileid = propsJSON.getString("fileid");

		NodeRoot root = new NodeRoot();

		Node node = root.get(fileid);

		JSONObject data = new JSONObject();
		data.put("size", 0);

		sendMessage(response, data);
	}

	private void quota(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		NodeRoot root = new NodeRoot();

		Node node = root.get("");
		long size = 0;

		JSONObject data = new JSONObject();
		data.put("used", size);
		data.put("available", "1073741824");

		sendMessage(response, data);
	}

	private void stream(Request request, Response response) throws IOException, ChipDriveException, JSONException {
		String fileid = request.getArgs("object");
		
		NodeRoot root = new NodeRoot();
		
		if(root.has(fileid)) {
			Node node = root.get(fileid);
			if(node instanceof FileObject) {
				String name = node.getName();
				long size = size(fileid);
				long start = 0;
				long end = size - 1;
				if(request.containsHeader("range")) {
					try {
						start = request.getRangeStart();
						end = request.getRangeEnd();
					} catch(Exception e) {}
					if((start >= 0 && start < size) && (end > 0 && end <= size)) {
						response.writeHeader("HTTP/1.1 206 Partial Content");
						response.writeHeader("Accept-Ranges: bytes");
						response.writeHeader("Content-Range: bytes " + (start) + "-" + (end) + "/" + size);
					} else {
						response.writeHeader("HTTP/1.1 416 Requested Range Not Satisfiable");
						response.writeHeader("Accept-Ranges: bytes");
						return;
					}
				} else {
					response.writeHeader("HTTP/1.1 200 OK");
					response.writeHeader("Content-Disposition: inline; filename=\"" + URLEncoder.encode(name, "UTF-8") + "\"");
				}
				response.writeHeader("Content-Type: " + MimeTypes.get(getExtension(name).toLowerCase()));
				response.writeHeader("Content-Length: " + ((end - start) + 1));
				response.writeHeader("Cache-Control: no-store");
				response.writeHeader("Connection: Keep-Alive");
				response.writeHeader("Keep-Alive: timeout=5, max=97");
				response.writeHeader("Server: ColdChip Web Servlet/CWS 1.2");
				response.writeHeader("");
				int buffer = 8192 * 8;
				byte[] b = new byte[buffer];
				for(long p = start; p < end; p += buffer) {
					int toRead = (int)Math.min(buffer, (end - p) + 1);
					read(fileid, b, p, toRead);
					response.writeByte(b, 0, toRead); 
					response.flush();
				}
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Object not a type of file");
				error.put("login", false);
				sendError(response, error);
			}
		} else {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Object doesn't exists");
			error.put("login", false);
			sendError(response, error);
		}
	}

	private void sendError(Response response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", true);
		stub.put("errorMsg", data.getString("errorMsg"));
		stub.put("login", data.getBoolean("login"));
		stub.put("data", new JSONObject());
		response.setContentType("application/json");
		response.writeText(stub.toString(4));
	}

	private void sendMessage(Response response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", false);
		stub.put("errorMsg", "");
		stub.put("login", false);
		stub.put("data", data);
		response.setContentType("application/json");
		response.writeText(stub.toString(4));
	}

	private static String getExtension(String name) {
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
		    return ""; // empty extension
		}
		return name.substring(lastIndexOf + 1);
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

	private boolean isAuthed() {
		return true;
	}

} 