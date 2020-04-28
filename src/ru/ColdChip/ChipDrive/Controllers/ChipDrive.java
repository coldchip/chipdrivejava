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
				this.dispatch(method, new DriveRequest(request), new DriveResponse(response));
			}
		} finally {
			threads--;
		}
	}

	private void dispatch(int method, DriveRequest driveRequest, DriveResponse driveResponse) throws IOException {
		try {
			switch(method) {
				case ChipDrive.VERSION: {
					this.version(driveRequest, driveResponse);
				}
				break;
				case ChipDrive.CONFIG: {
					this.config(driveRequest, driveResponse);
				}
				break;
				case ChipDrive.LIST: {
					if(isAuthed() == true) {
						this.list(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.LINK: {
					if(isAuthed() == true) {
						this.link(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.UPLOAD: {
					if(isAuthed() == true) {
						this.upload(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.DELETE: {
					if(isAuthed() == true) {
						this.delete(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.FOLDER: {
					if(isAuthed() == true) {
						this.folder(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.RENAME: {
					if(isAuthed() == true) {
						this.rename(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.INFO: {
					if(isAuthed() == true) {
						this.info(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.QUOTA: {
					if(isAuthed() == true) {
						this.quota(driveRequest, driveResponse);
					} else {
						throw new ChipDriveLoginException("Login Required");
					}
				}
				break;
				case ChipDrive.STREAM: {
					if(isAuthed() == true) {
						this.stream(driveRequest, driveResponse);
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
			sendError(driveResponse, error);
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(driveResponse, error);
		} catch(ChipDriveLoginException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", true);
			sendError(driveResponse, error);
		}
	}

	private void version(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		JSONObject config = new JSONObject();
		config.put("version", "1.3.1");
		response.setHeader("Content-Type", "application/json");
		response.write(config.toString(4));
	}

	private void config(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
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
		response.setHeader("Content-Type", "application/json");
		response.write(config.toString(4));
	}

	private void list(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasFolderID()) {
			String folderid = request.getFolderID();

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
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void link(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasFileID()) {
			String fileid = request.getFileID();

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
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void upload(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasFolderID() && request.hasName()) {
			String folderid = request.getFolderID();
			String name = request.getName();
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

				long contentSize = request.getSize();
				if(contentSize > 0) {
					long i = 0;
					byte[] data = new byte[8192];
					while (i < contentSize) {
						int read = request.read(data, 0, data.length);
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
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void deleteRecursive(Node node) throws ChipDriveException, ChipDriveException {
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

	private void delete(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasItemID()) {
			String itemid = request.getItemID();

			NodeRoot root = new NodeRoot();
			Node node = root.get(itemid);
			deleteRecursive(node);

			sendMessage(response, new JSONObject());
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void folder(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasFolderID() && request.hasName()) {
			String folderid = request.getFolderID();
			String name = request.getName();

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
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void rename(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasItemID() && request.hasName()) {
			String itemid = request.getItemID();
			String name = request.getName();

			NodeRoot root = new NodeRoot();
			Node node = root.get(itemid);
			node.setName(name);
			root.put(node);

			sendMessage(response, new JSONObject());
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void info(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasFileID()) {
			String fileid = request.getFileID();

			NodeRoot root = new NodeRoot();

			Node node = root.get(fileid);

			JSONObject data = new JSONObject();
			data.put("size", 0);

			sendMessage(response, data);
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void quota(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		NodeRoot root = new NodeRoot();

		Node node = root.get("");
		long size = 0;

		JSONObject data = new JSONObject();
		data.put("used", size);
		data.put("available", "1073741824");

		sendMessage(response, data);
	}

	private void stream(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		String fileid = request.getArgs("object");
		
		NodeRoot root = new NodeRoot();
		
		if(root.has(fileid)) {
			Node node = root.get(fileid);
			if(node instanceof FileObject) {
				String name = node.getName();
				long size = size(fileid);
				long start = 0;
				long end = size - 1;
				if(request.hasRangeStart() || request.hasRangeEnd()) {
					if(request.hasRangeStart()) {
						start = request.getRangeStart();
					}
					if(request.hasRangeEnd()) {
						end = request.getRangeEnd();
					}
					if((start >= 0 && start < size) && (end > 0 && end <= size)) {
						response.setStatus(206);
						response.setHeader("Accept-Ranges", "bytes");
						response.setHeader("Content-Range", "bytes " + (start) + "-" + (end) + "/" + size);
					} else {
						response.setStatus(416);
						response.setHeader("Accept-Ranges", "bytes");
						return;
					}
				} else {
					response.setStatus(200);
					response.setHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(name, "UTF-8") + "\"");
				}
				response.setHeader("Content-Type", MimeTypes.get(getExtension(name).toLowerCase()));
				response.setHeader("Content-Length", Long.toString(((end - start) + 1)));
				response.setHeader("Cache-Control", "no-store");
				response.setHeader("Connection", "Keep-Alive");
				response.setHeader("Keep-Alive", "timeout=5, max=97");
				response.setHeader("Server", " ColdChip Web Servlet/CWS 1.2");
				int buffer = 8192 * 8;
				byte[] b = new byte[buffer];
				for(long p = start; p < end; p += buffer) {
					int toRead = (int)Math.min(buffer, (end - p) + 1);
					read(fileid, b, p, toRead);
					response.write(b, 0, toRead); 
					response.flush();
				}
			} else {
				throw new ChipDriveException("Object not a type of file");
			}
		} else {
			throw new ChipDriveException("Object doesn't exists");
		}
	}

	private void sendError(DriveResponse response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", true);
		stub.put("errorMsg", data.getString("errorMsg"));
		stub.put("login", data.getBoolean("login"));
		stub.put("data", new JSONObject());
		response.setHeader("Content-Type", "application/json");
		response.write(stub.toString(4));
	}

	private void sendMessage(DriveResponse response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", false);
		stub.put("errorMsg", "");
		stub.put("login", false);
		stub.put("data", data);
		response.setHeader("Content-Type", "application/json");
		response.write(stub.toString(4));
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