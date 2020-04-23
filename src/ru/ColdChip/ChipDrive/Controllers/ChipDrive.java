package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;

import ru.ColdChip.ChipDrive.api.ChipFS;

import ru.ColdChip.ChipDrive.Exceptions.*;
import ru.ColdChip.ChipDrive.Constants.MimeTypes;
import java.io.*;
import java.util.*;
import java.net.URLEncoder;
import org.JSON.*;

public abstract class ChipDrive extends ChipFS implements IChipDrive {

	private NodeTable table = new NodeTable();

	public ChipDrive() {

	}

	public void version(Request request, Response response) throws IOException {
		JSONObject config = new JSONObject();
		config.put("version", "1.3.1");
		response.setContentType("application/json");
		response.writeText(config.toString(4));
	}

	public void driveConfig(Request request, Response response) throws IOException {
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

	public void fileList(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				String props = request.getValue("props");
				JSONObject propsJSON = new JSONObject(props);
				String folderid = propsJSON.getString("folderid");

				if(folderid.equals("") && !table.has(folderid)) {
					Node node = new Node(folderid);
					node.setDeletable(false);
					node.setType(Node.FOLDER);
					table.put(node);

				}

				Node node = table.get(folderid);

				ArrayList<String> files = node.list();

				JSONArray list = new JSONArray();

				for(String fileID : files) {
					JSONObject objectData = new JSONObject();
					Node currentNode = table.get(fileID);
					objectData.put("kind", currentNode.getType());
					objectData.put("displayName", currentNode.getName());
					objectData.put("id", fileID);
					list.put(objectData);
				}

				JSONObject meta = new JSONObject();
				meta.put("list", list);
				meta.put("displayName", node.getName());

				sendMessage(response, meta);
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);	
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		}
	}

	public void fileLink(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				try {
					String props = request.getValue("props");
					JSONObject propsJSON = new JSONObject(props);
					String fileid = propsJSON.getString("fileid");

					Node node = table.get(fileid);

					String filename = node.getName();
					JSONObject data = new JSONObject();
					data.put("type", MimeTypes.get(getExtension(filename).toLowerCase()));
					data.put("ext", getExtension(filename).toLowerCase());
					data.put("displayName", filename);
					data.put("url", "/api/v1/drive/item.stream/" + fileid);

					sendMessage(response, data);
				} catch(Exception e) {
					throw new ChipDriveException("Unable to generate link");
				}
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);	
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);	
			sendError(response, error);
		}
	}

	public void fileUpload(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				String props = request.getValue("props");
				JSONObject propsJSON = new JSONObject(props);
				String folderid = propsJSON.getString("folderid");
				String name = propsJSON.getString("name");

				try {
					Node node = table.get(folderid);
					node.setOwner("apps903923890.coldchip.ru.coldchip.0");
					String fileid = node.createFile(name);
					long contentSize = Long.parseLong(request.getHeader("content-length").replaceAll("[^0-9]", ""));
					if(contentSize > 0) {
						long i = 0;
						byte[] data = new byte[8192];
						while (i < contentSize) {
							int read = request.stream.read(data, 0, data.length);
							if(read > 0) {
								write(fileid, data, i, read);
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
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);	
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);		
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);			
			sendError(response, error);
		}
	}

	public void fileDelete(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				String props = request.getValue("props");
				JSONObject propsJSON = new JSONObject(props);
				String itemid = propsJSON.getString("itemid");

				Node node = table.get(itemid);
				node.delete();

				sendMessage(response, new JSONObject());
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);			
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);			
			sendError(response, error);
		}
	}

	public void newFolder(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				String props = request.getValue("props");
				JSONObject propsJSON = new JSONObject(props);
				String folderid = propsJSON.getString("folderid");
				String name = propsJSON.getString("name");

				Node node = table.get(folderid);
				node.setOwner("");
				node.createFolder(name);

				sendMessage(response, new JSONObject());
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);			
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);			
			sendError(response, error);
		}
	}

	public void fileRename(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				String props = request.getValue("props");
				JSONObject propsJSON = new JSONObject(props);
				String itemid = propsJSON.getString("itemid");
				String name = propsJSON.getString("name");

				Node node = table.get(itemid);
				node.rename(name);
				table.put(node);
				sendMessage(response, new JSONObject());
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);			
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);			
			sendError(response, error);
		}
	}

	public void fileInfo(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {
				String props = request.getValue("props");
				JSONObject propsJSON = new JSONObject(props);
				String fileid = propsJSON.getString("fileid");

				Node node = table.get(fileid);

				JSONObject data = new JSONObject();
				data.put("size", 0);

				sendMessage(response, data);
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);			
			sendError(response, error);	
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);			
			sendError(response, error);
		}
	}

	public void driveQuota(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {

				Node node = table.get("");
				long size = 0;

				JSONObject data = new JSONObject();
				data.put("used", size);
				data.put("available", "1073741824");

				sendMessage(response, data);
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);		
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);	
			sendError(response, error);
		}
	}

	public void fileStream(Request request, Response response) throws IOException {
		try {
			if(isAuthed() == true) {

				String object = request.getArgs("object");
				
				
				if(table.has(object)) {
					Node node = table.get(object);
					if(node.getType() == Node.FILE) {
						String objectName = node.getName();
						long objectSize = size(object);
						long start = 0;
						long end = objectSize - 1;
						if(request.containsHeader("range")) {
							try {
								start = request.getRangeStart();
								end = request.getRangeEnd();
							} catch(Exception e) {}
							if((start >= 0 && start < objectSize) && (end > 0 && end <= objectSize)) {
								response.writeHeader("HTTP/1.1 206 Partial Content");
								response.writeHeader("Accept-Ranges: bytes");
								response.writeHeader("Content-Range: bytes " + start + "-" + (end) + "/" + objectSize);
							} else {
								response.writeHeader("HTTP/1.1 416 Requested Range Not Satisfiable");
								response.writeHeader("Accept-Ranges: bytes");
								return;
							}
						} else {
							response.writeHeader("HTTP/1.1 200 OK");
							response.writeHeader("Content-Disposition: inline; filename=\"" + URLEncoder.encode(objectName, "UTF-8") + "\"");
						}
						response.writeHeader("Content-Type: " + MimeTypes.get(getExtension(objectName).toLowerCase()));
						response.writeHeader("Content-Length: " + ((end - start) + 1));
						response.writeHeader("Cache-Control: no-store");
						response.writeHeader("Connection: Keep-Alive");
						response.writeHeader("Keep-Alive: timeout=5, max=97");
						response.writeHeader("Server: ColdChip Web Servlet/CWS 1.2");
						response.writeHeader("");
						int buffer = 8192 * 8;
						byte[] b = new byte[buffer];
						System.out.println(object);
						for(long p = start; p < end; p += buffer) {
							int toRead = (int)Math.min(buffer, (end - p) + 1);
							read(object, b, p, toRead);
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
				
			} else {
				JSONObject error = new JSONObject();
				error.put("errorMsg", "Login");
				error.put("login", true);
				sendError(response, error);
			}
		} catch(ChipDriveException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", e.toString());
			error.put("login", false);
			sendError(response, error);
		} catch(JSONException e) {
			JSONObject error = new JSONObject();
			error.put("errorMsg", "Invalid request data");
			error.put("login", false);
			sendError(response, error);
		}
	}

	public void sendError(Response response, JSONObject data) throws IOException {
		JSONObject stub = new JSONObject();
		stub.put("error", true);
		stub.put("errorMsg", data.getString("errorMsg"));
		stub.put("login", data.getBoolean("login"));
		stub.put("data", new JSONObject());
		response.setContentType("application/json");
		response.writeText(stub.toString(4));
	}

	public void sendMessage(Response response, JSONObject data) throws IOException {
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

	private boolean isAuthed() {
		return true;
	}

} 