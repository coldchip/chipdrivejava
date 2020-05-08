package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;
import ru.ColdChip.ChipDrive.api.ChipFS;
import ru.ColdChip.ChipDrive.Exceptions.*;
import ru.ColdChip.ChipDrive.Object.*;
import ru.ColdChip.ChipDrive.Constants.MimeTypes;
import java.security.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.net.URLEncoder;
import org.JSON.*;
import java.util.concurrent.TimeUnit;

public class ChipDrive extends ChipFS implements IChipDrive {

	private final String SECRET = "G6DDKCsSYT59AxR3J9WrbgmwXy2c64kA6zQU8J7GYbE2JA9HGt58Sw5vtkJ5Zr5BgqZMDVCdtuZranwn2MBF5ULDeqZWNs4LpYqypgmFxFYxhaxVTk7s5zhpcS34fMpx";

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
	public static final int LOGIN   = 13;

	private static volatile int threads = 0;

	private DriveSession sessions = new DriveSession();

	public ChipDrive() {
		log("-----DONE-----");
	}

	public void enqueue(int method, Request request, Response response) throws IOException {
		try {
			threads++;
			if(threads < 255) {
				DriveRequest driveRequest = new DriveRequest(request, sessions);
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
		DriveUser user = null;
		String token = "";
		if(request.hasParam(DriveRequest.AUTH_TOKEN)) {
			token = request.getParam(DriveRequest.AUTH_TOKEN);
		}
		try {
			switch(method) {
				case ChipDrive.LOGIN: {
					this.login(request, response);
				}
				break;
				case ChipDrive.VERSION: {
					this.version(request, response);
				}
				break;
				case ChipDrive.CONFIG: {
					this.config(request, response);
				}
				break;
				case ChipDrive.LIST: {
					if(sessions.hasUser(token) == true) {
						this.list(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.LINK: {
					if(sessions.hasUser(token) == true) {
						this.link(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.UPLOAD: {
					if(sessions.hasUser(token) == true) {
						this.upload(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.DELETE: {
					if(sessions.hasUser(token) == true) {
						this.delete(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.FOLDER: {
					if(sessions.hasUser(token) == true) {
						this.folder(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.RENAME: {
					if(sessions.hasUser(token) == true) {
						this.rename(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.INFO: {
					if(sessions.hasUser(token) == true) {
						this.info(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.QUOTA: {
					if(sessions.hasUser(token) == true) {
						this.quota(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.STREAM: {
					if(sessions.hasUser(token) == true) {
						this.stream(sessions.getUser(token), request, response);
					} else {
						throw new ChipDriveLoginException();
					}
				}
				break;
				case ChipDrive.UNKNOWN: {
					throw new ChipDriveException("Unknown Mode");
				}
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("error", true);
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendMessage(response, error);
		} catch(ChipDriveLoginException e) {
			JSONObject error = new JSONObject();
			error.put("error", true);
			error.put("errorMsg", e.toString());
			error.put("login", true);
			error.put("url", "/login/");
			sendMessage(response, error);
		}
	}

	private void login(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.USERNAME) && request.hasParam(DriveRequest.PASSWORD)) {
			String username = request.getParam(DriveRequest.USERNAME);
			String password = request.getParam(DriveRequest.PASSWORD);
			if((username.equals("coldchip") || username.equals("admin")) && hash(password + SECRET).equals("dda7d1aa4380d0589df76fe5929508dfadbfdc78c613fd79a652089205950773")) {
				DriveUser user = new DriveUser(username);
				String token = randomString(128);
				this.sessions.addUser(token, user);
				response.setParam(DriveResponse.TOKEN, token);

				JSONObject result = new JSONObject();
				result.put("message", "Logged In");
				result.put("url", "/drive");
				response.setParam(DriveResponse.CONTENT_TYPE, "application/json");
				response.write(result.toString(4));
			} else {
				JSONObject result = new JSONObject();
				result.put("message", "Invalid Credentials");
				response.setParam(DriveResponse.CONTENT_TYPE, "application/json");
				response.write(result.toString(4));
			}
		} else {
			JSONObject result = new JSONObject();
			result.put("message", "Username or Password is empty");
			response.setParam(DriveResponse.CONTENT_TYPE, "application/json");
			response.write(result.toString(4));
		}
	}

	private void version(DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		JSONObject config = new JSONObject();
		config.put("version", "V2.3.2");
		response.setParam(DriveResponse.CONTENT_TYPE, "application/json");
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
		response.setParam(DriveResponse.CONTENT_TYPE, "application/json");
		response.write(config.toString(4));
	}

	private void list(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.FOLDER_ID) && user != null) {
			String folderid = request.getParam(DriveRequest.FOLDER_ID);

			NodeRoot root = new NodeRoot(user.getUsername());
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

	private void link(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.FILE_ID) && user != null) {
			String fileid = request.getParam(DriveRequest.FILE_ID);

			NodeRoot root = new NodeRoot(user.getUsername());
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

	private void upload(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.FOLDER_ID) && request.hasParam(DriveRequest.NAME) && user != null) {
			String folderid = request.getParam(DriveRequest.FOLDER_ID);
			String name = request.getParam(DriveRequest.NAME);
			try {
				if(request.hasParam(DriveRequest.CONTENT_LENGTH)) {
					String randomID = randomString(32);

					NodeRoot root = new NodeRoot(user.getUsername());

					FileObject file = new FileObject();
					file.setName(name);
					file.setID(randomID);
					file.setParentID(folderid);
					root.put(file);

					FolderObject rootDir = (FolderObject)root.get(folderid);
					rootDir.addChild(file);
					root.put(rootDir);

					long contentSize = Long.parseLong(request.getParam(DriveRequest.CONTENT_LENGTH));
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

	private void deleteRecursive(DriveUser user, Node node) throws ChipDriveException, ChipDriveException {
		NodeRoot root = new NodeRoot(user.getUsername());
		
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
				deleteRecursive(user, currentNode);
			}
			root.remove(node.getID());
		}
	}

	private void delete(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.ITEM_ID) && user != null) {
			String itemid = request.getParam(DriveRequest.ITEM_ID);

			NodeRoot root = new NodeRoot(user.getUsername());
			Node node = root.get(itemid);
			deleteRecursive(user, node);

			sendMessage(response, new JSONObject());
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void folder(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.FOLDER_ID) && request.hasParam(DriveRequest.NAME) && user != null) {
			String folderid = request.getParam(DriveRequest.FOLDER_ID);
			String name = request.getParam(DriveRequest.NAME);

			String randomID = randomString(32);

			NodeRoot root = new NodeRoot(user.getUsername());

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

	private void rename(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.ITEM_ID) && request.hasParam(DriveRequest.NAME) && user != null) {
			String itemid = request.getParam(DriveRequest.ITEM_ID);
			String name = request.getParam(DriveRequest.NAME);

			NodeRoot root = new NodeRoot(user.getUsername());
			Node node = root.get(itemid);
			node.setName(name);
			root.put(node);

			sendMessage(response, new JSONObject());
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void info(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.FILE_ID) && user != null) {
			String fileid = request.getParam(DriveRequest.FILE_ID);

			NodeRoot root = new NodeRoot(user.getUsername());

			Node node = root.get(fileid);

			JSONObject data = new JSONObject();
			data.put("size", 0);

			sendMessage(response, data);
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void quota(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(user != null) {
			NodeRoot root = new NodeRoot(user.getUsername());

			Node node = root.get("");
			long size = 0;

			JSONObject data = new JSONObject();
			data.put("used", size);
			data.put("available", "1073741824");

			sendMessage(response, data);
		} else {
			throw new ChipDriveException("Params not Satisfiable");
		}
	}

	private void stream(DriveUser user, DriveRequest request, DriveResponse response) throws IOException, ChipDriveException {
		if(request.hasParam(DriveRequest.FILE_ID) && user != null) {
			String fileid = request.getParam(DriveRequest.FILE_ID);
			
			NodeRoot root = new NodeRoot(user.getUsername());
			
			if(root.has(fileid)) {
				Node node = root.get(fileid);
				if(node instanceof FileObject) {
					String name = node.getName();
					long size = size(fileid);
					long start = 0;
					long end = size - 1;
					if(request.hasParam(DriveRequest.RANGE_START) || request.hasParam(DriveRequest.RANGE_END)) {
						if(request.hasParam(DriveRequest.RANGE_START)) {
							start = Long.parseLong(request.getParam(DriveRequest.RANGE_START));
						}
						if(request.hasParam(DriveRequest.RANGE_END)) {
							end = Long.parseLong(request.getParam(DriveRequest.RANGE_END));
						}
						if((start >= 0 && start < size) && (end > 0 && end <= size)) {
							response.setParam(DriveResponse.STATUS, "206");
							response.setParam(DriveResponse.ACCEPT_RANGES, "bytes");
							response.setParam(DriveResponse.CONTENT_RANGE, "bytes " + (start) + "-" + (end) + "/" + size);
						} else {
							response.setParam(DriveResponse.STATUS, "416");
							response.setParam(DriveResponse.ACCEPT_RANGES, "bytes");
							return;
						}
					} else {
						response.setParam(DriveResponse.STATUS, "200");
						response.setParam(DriveResponse.CONTENT_NAME, "inline; filename=\"" + URLEncoder.encode(name, "UTF-8") + "\"");
					}
					response.setParam(DriveResponse.CONTENT_TYPE, MimeTypes.get(getExtension(name).toLowerCase()));
					response.setParam(DriveResponse.CONTENT_LENGTH, Long.toString(((end - start) + 1)));
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

	private void sendMessage(DriveResponse response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		if(data.has("error")) {
			stub.put("error", data.getBoolean("error"));
			stub.put("data", new JSONObject());
		} else {
			stub.put("error", false);
			stub.put("data", data);
		}
		if(data.has("errorMsg")) {
			stub.put("errorMsg", data.getString("errorMsg"));
		} else {
			stub.put("errorMsg", "");
		}
		if(data.has("login")) {
			stub.put("login", data.getBoolean("login"));
		} else {
			stub.put("login", false);
		}
		if(data.has("url")) {
			stub.put("url", data.getString("url"));
		} else {
			stub.put("url", "null");
		}
		response.setParam(DriveResponse.CONTENT_TYPE, "application/json");
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

	public static String randomString(int length) {
		final String list = "XvJ1kQWM7p9KCmGPfaT6YRledInBxEorLVj3NFU0usZ8gy_2iStwchbqz45AOHD";
		StringBuilder results = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int character = (int)(Math.random() * list.length());
			results.append(list.charAt(character));
		}
		return results.toString();
	}

	private static String hash(String input) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
			byte[] result = mDigest.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch(NoSuchAlgorithmException e) {
			return input;
		}
	}
} 