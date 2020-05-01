package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;
import ru.ColdChip.ChipDrive.api.ChipFS;
import ru.ColdChip.ChipDrive.Exceptions.*;
import ru.ColdChip.ChipDrive.Object.*;
import ru.ColdChip.ChipDrive.Constants.MimeTypes;
import java.io.*;
import java.util.*;
import java.text.*;
import java.net.URLEncoder;
import org.JSON.*;
import java.util.concurrent.TimeUnit;

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
		log("Starting ChipDrive Ticker");
		Thread t = new Thread() {
			public void run() {
				while(true) {
					
				}
			}
		};
		t.start();
		log("ChipDrive Ticker Started");
		log("-----DONE-----");
	}

	public void enqueue(int method, Request request, Response response) throws IOException {
		try {
			threads++;
			if(threads < 255) {
				DriveRequest driveRequest = new DriveRequest(request);
				DriveResponse driveResponse = new DriveResponse(response);
				DriveQueue queue = new DriveQueue(method, driveRequest, driveResponse);
				this.dispatch(queue);
			}
		} finally {
			threads--;
		}
	}

	private void dispatch(DriveQueue queue) throws IOException {
		int method = queue.getMethod();
		DriveRequest request = queue.getRequest();
		DriveResponse response = queue.getResponse();
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
					this.list(request, response);
				}
				break;
				case ChipDrive.LINK: {
					this.link(request, response);
				}
				break;
				case ChipDrive.UPLOAD: {
					this.upload(request, response);
				}
				break;
				case ChipDrive.DELETE: {
					this.delete(request, response);
				}
				break;
				case ChipDrive.FOLDER: {
					this.folder(request, response);
				}
				break;
				case ChipDrive.RENAME: {
					this.rename(request, response);
				}
				break;
				case ChipDrive.INFO: {
					this.info(request, response);
				}
				break;
				case ChipDrive.QUOTA: {
					this.quota(request, response);
				}
				break;
				case ChipDrive.STREAM: {
					this.stream(request, response);
				}
				break;
				case ChipDrive.UNKNOWN: {
					throw new ChipDriveException("Unknown Mode");
				}
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		}
	}

	private void version(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		JSONObject config = new JSONObject();
		config.put("version", "V2.3.2");
		response.setHeader(DriveResponse.CONTENT_TYPE, "application/json");
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
		response.setHeader(DriveResponse.CONTENT_TYPE, "application/json");
		response.write(config.toString(4));
	}

	private void list(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasHeader(DriveRequest.FOLDER_ID)) {
			String folderid = request.getHeader(DriveRequest.FOLDER_ID);

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
		if(request.hasHeader(DriveRequest.FILE_ID)) {
			String fileid = request.getHeader(DriveRequest.FILE_ID);

			NodeRoot root = new NodeRoot();
			Node node = root.get(fileid);
			if(node instanceof FileObject) {
				FileObject folder = (FileObject)node;
				String filename = folder.getName();

				JSONObject property = new JSONObject();
				property.put("fileid", fileid);

				JSONObject data = new JSONObject();
				data.put("type", MimeTypes.get(getExtension(filename).toLowerCase()));
				data.put("ext", getExtension(filename).toLowerCase());
				data.put("displayName", filename);
				data.put("url", "/api/v1/drive/item.stream/?props=" + URLEncoder.encode(property.toString(), "UTF-8"));

				sendMessage(response, data);
			} else {
				throw new ChipDriveException("Not a folder");
			}
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void upload(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasHeader(DriveRequest.FOLDER_ID) && request.hasHeader(DriveRequest.NAME)) {
			String folderid = request.getHeader(DriveRequest.FOLDER_ID);
			String name = request.getHeader(DriveRequest.NAME);
			try {
				if(request.hasHeader(DriveRequest.CONTENT_LENGTH)) {
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

					long contentSize = Long.parseLong(request.getHeader(DriveRequest.CONTENT_LENGTH));
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
				} else {
					throw new ChipDriveException("Unknown Upload Length");
				}
			} catch(IOException e) {
				throw new ChipDriveException("I/O Error");
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
		if(request.hasHeader(DriveRequest.ITEM_ID)) {
			String itemid = request.getHeader(DriveRequest.ITEM_ID);

			NodeRoot root = new NodeRoot();
			Node node = root.get(itemid);
			deleteRecursive(node);

			sendMessage(response, new JSONObject());
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void folder(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasHeader(DriveRequest.FOLDER_ID) && request.hasHeader(DriveRequest.NAME)) {
			String folderid = request.getHeader(DriveRequest.FOLDER_ID);
			String name = request.getHeader(DriveRequest.NAME);

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
		if(request.hasHeader(DriveRequest.ITEM_ID) && request.hasHeader(DriveRequest.NAME)) {
			String itemid = request.getHeader(DriveRequest.ITEM_ID);
			String name = request.getHeader(DriveRequest.NAME);

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
		if(request.hasHeader(DriveRequest.FILE_ID)) {
			String fileid = request.getHeader(DriveRequest.FILE_ID);

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
		if(request.hasHeader(DriveRequest.FILE_ID)) {
			String fileid = request.getHeader(DriveRequest.FILE_ID);
			
			NodeRoot root = new NodeRoot();
			
			if(root.has(fileid)) {
				Node node = root.get(fileid);
				if(node instanceof FileObject) {
					String name = node.getName();
					long size = size(fileid);
					long start = 0;
					long end = size - 1;
					if(request.hasHeader(DriveRequest.RANGE_START) || request.hasHeader(DriveRequest.RANGE_END)) {
						if(request.hasHeader(DriveRequest.RANGE_START)) {
							start = Long.parseLong(request.getHeader(DriveRequest.RANGE_START));
						}
						if(request.hasHeader(DriveRequest.RANGE_END)) {
							end = Long.parseLong(request.getHeader(DriveRequest.RANGE_END));
						}
						if((start >= 0 && start < size) && (end > 0 && end <= size)) {
							response.setHeader(DriveResponse.STATUS, "206");
							response.setHeader(DriveResponse.ACCEPT_RANGES, "bytes");
							response.setHeader(DriveResponse.CONTENT_RANGE, "bytes " + (start) + "-" + (end) + "/" + size);
						} else {
							response.setHeader(DriveResponse.STATUS, "416");
							response.setHeader(DriveResponse.ACCEPT_RANGES, "bytes");
							return;
						}
					} else {
						response.setHeader(DriveResponse.STATUS, "200");
						response.setHeader(DriveResponse.CONTENT_NAME, "inline; filename=\"" + URLEncoder.encode(name, "UTF-8") + "\"");
					}
					response.setHeader(DriveResponse.CONTENT_TYPE, MimeTypes.get(getExtension(name).toLowerCase()));
					response.setHeader(DriveResponse.CONTENT_LENGTH, Long.toString(((end - start) + 1)));
					int buffer = 8192 * 2;
					byte[] b = new byte[buffer];
					for(long p = start; p < end; p += buffer) {
						int toRead = (int)Math.min(buffer, (end - p) + 1);
						read(fileid, b, p, toRead);
						response.write(b, 0, toRead); 
					}
					response.flush();
				} else {
					throw new ChipDriveException("Object not a type of file");
				}
			} else {
				throw new ChipDriveException("Object doesn't exists");
			}
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void sendError(DriveResponse response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", true);
		stub.put("errorMsg", data.getString("errorMsg"));
		stub.put("login", data.getBoolean("login"));
		stub.put("data", new JSONObject());
		response.setHeader(DriveResponse.CONTENT_TYPE, "application/json");
		response.write(stub.toString(4));
	}

	private void sendMessage(DriveResponse response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", false);
		stub.put("errorMsg", "");
		stub.put("login", false);
		stub.put("data", data);
		response.setHeader(DriveResponse.CONTENT_TYPE, "application/json");
		response.write(stub.toString(4));
	}

	private static String getExtension(String name) {
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
		    return ""; // empty extension
		}
		return name.substring(lastIndexOf + 1);
	}

	public static void log(String text) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println("[" + dateFormat.format(date) + "] [ChipDrive]: " + text);
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