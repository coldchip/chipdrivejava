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
		System.out.println("ColdChip Drive V2.3.1 Java");
		System.out.println("--------------------------");

		ChipDrive drive = new ChipDrive();

		HTTPServer server = new HTTPServer(PORT);
		
		server.on("/api/v1/version", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				drive.enqueue(ChipDrive.VERSION, request, response);
			}
		});

		server.on("/api/v1/getDriveConfig", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				drive.enqueue(ChipDrive.CONFIG, request, response);
			}
		});

		server.on("/api/v1/drive/{method}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				String mode = request.getArgs("method");
				switch(mode) {
					case "files.list":
						{
							drive.enqueue(ChipDrive.LIST, request, response);
						}
					break;
					case "file.link":
						{
							drive.enqueue(ChipDrive.LINK, request, response);
						}
					break;
					case "new.upload":
						{
							drive.enqueue(ChipDrive.UPLOAD, request, response);
		        		}
					break;
					case "file.delete":
						{
							drive.enqueue(ChipDrive.DELETE, request, response);
						}
					break;
					case "new.folder":
						{
							drive.enqueue(ChipDrive.FOLDER, request, response);
						}
					break;
					case "item.rename":
						{
							drive.enqueue(ChipDrive.RENAME, request, response);
						}
					break;
					case "item.info":
						{
							drive.enqueue(ChipDrive.INFO, request, response);
						}
					break;
					case "drive.quota":
						{
							drive.enqueue(ChipDrive.QUOTA, request, response);
						}
					break;
					default:
						{
							drive.enqueue(ChipDrive.UNKNOWN, request, response);
						}
					break;
				}
			}
		});

		server.on("/api/v1/drive/item.stream/{object}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				drive.enqueue(ChipDrive.STREAM, request, response);
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
