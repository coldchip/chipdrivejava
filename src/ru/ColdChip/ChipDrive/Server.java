package ru.ColdChip.ChipDrive;

import ru.ColdChip.WebServer.*;
import ru.ColdChip.ChipDrive.Controllers.*;
import java.io.IOException;
import org.JSON.*;

public class Server {

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

		ChipDrive drive = new ChipDrive();

		HTTPServer server = new HTTPServer(PORT);
		
		server.on("/api/v1/version", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				drive.version(request, response);
			}
		});

		server.on("/api/v1/getDriveConfig", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				drive.config(request, response);
			}
		});

		server.on("/api/v1/drive/{method}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				String mode = request.getArgs("method");
				switch(mode) {
					case "files.list":
						{
							drive.list(request, response);
						}
					break;
					case "file.link":
						{
							drive.link(request, response);
						}
					break;
					case "new.upload":
						{
							drive.upload(request, response);
		        		}
					break;
					case "file.delete":
						{
							drive.delete(request, response);
						}
					break;
					case "new.folder":
						{
							drive.folder(request, response);
						}
					break;
					case "item.rename":
						{
							drive.rename(request, response);
						}
					break;
					case "item.info":
						{
							drive.info(request, response);
						}
					break;
					case "drive.quota":
						{
							drive.quota(request, response);
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
				drive.stream(request, response);
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
