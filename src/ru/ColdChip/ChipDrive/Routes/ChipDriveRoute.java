package ru.ColdChip.ChipDrive.Routes;

import ru.ColdChip.WebServer.*;
import java.io.*;

import ru.ColdChip.ChipDrive.Controllers.*;

import org.JSON.*;

public class ChipDriveRoute {

	private HTTPServer server;
	ChipDrive drive = new ChipDrive();

	public ChipDriveRoute(HTTPServer server) {
		this.server = server;
	}

	public void serve() {
		server.on("/api/v1/version", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				drive.version(request, response);
			}
		});

		server.on("/api/v1/getDriveConfig", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				drive.driveConfig(request, response);
			}
		});

		server.on("/api/v1/drive/{method}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				String session = request.getSession();
				String mode = request.getArgs("method");
				switch(mode) {
					case "files.list":
						{
							drive.fileList(request, response);
						}
					break;
					case "file.link":
						{
							drive.fileLink(request, response);
						}
					break;
					case "new.upload":
						{
							drive.fileUpload(request, response);
		        		}
					break;
					case "file.delete":
						{
							drive.fileDelete(request, response);
						}
					break;
					case "new.folder":
						{
							drive.newFolder(request, response);
						}
					break;
					case "item.rename":
						{
							drive.fileRename(request, response);
						}
					break;
					case "item.info":
						{
							drive.fileInfo(request, response);
						}
					break;
					case "drive.quota":
						{
							drive.driveQuota(request, response);
						}
					break;
					default:
						{
							JSONObject j = new JSONObject();
							j.put("errorMsg", "UnknownModeException");
							j.put("login", false);
							drive.sendError(response, j);
						}
					break;
				}
			}
		});

		server.on("/api/v1/drive/item.stream/{object}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				drive.fileStream(request, response);
			}
		});

		server.on("/api/v1/token", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				response.setContentType("application/json");
				JSONObject json = new JSONObject();
				json.put("token", "cdt" + randomString(61));
				response.writeText(json.toString(4));
			}
		});

		server.on("/{*}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				response.serve("html");
			}
		});
	}

	private static String randomString(int length) {
		final String list = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder results = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int character = (int)(Math.random() * list.length());
			results.append(list.charAt(character));
		}
		return results.toString();
	}
	
}