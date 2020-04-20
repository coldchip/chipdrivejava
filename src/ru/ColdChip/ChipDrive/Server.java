package ru.ColdChip.ChipDrive;

import ru.ColdChip.WebServer.*;
import ru.ColdChip.ChipDrive.Controllers.ChipDrive;
import ru.ColdChip.ChipDrive.Controllers.IChipDrive;
import java.io.IOException;
import org.JSON.*;

public class Server extends ChipDrive implements IChipDrive {

	private static final int PORT = 9010;

	public static void main(String[] args) {

		try {
			Server server = new Server();
			server.core();
		} catch(Exception e) {
			
		}
	}

	private void core() {
		System.out.println("ColdChip Drive V2.3 Java");
		System.out.println("--------------------------");
		HTTPServer server = new HTTPServer(PORT);
		
		server.on("/api/v1/version", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				version(request, response);
			}
		});

		server.on("/api/v1/getDriveConfig", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				driveConfig(request, response);
			}
		});

		server.on("/api/v1/drive/{method}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				String mode = request.getArgs("method");
				switch(mode) {
					case "files.list":
						{
							fileList(request, response);
						}
					break;
					case "file.link":
						{
							fileLink(request, response);
						}
					break;
					case "new.upload":
						{
							fileUpload(request, response);
		        		}
					break;
					case "file.delete":
						{
							fileDelete(request, response);
						}
					break;
					case "new.folder":
						{
							newFolder(request, response);
						}
					break;
					case "item.rename":
						{
							fileRename(request, response);
						}
					break;
					case "item.info":
						{
							fileInfo(request, response);
						}
					break;
					case "drive.quota":
						{
							driveQuota(request, response);
						}
					break;
					default:
						{
							JSONObject j = new JSONObject();
							j.put("errorMsg", "UnknownModeException");
							j.put("login", false);
							sendError(response, j);
						}
					break;
				}
			}
		});

		server.on("/api/v1/drive/item.stream/{object}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				fileStream(request, response);
			}
		});

		server.on("/{*}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				response.serve("html");
			}
		});

		server.run();
	}

}
