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
		System.out.println("ColdChip Drive V2.3.2 Java");
		System.out.println("--------------------------");

		ChipDrive drive = new ChipDrive();

		HTTPServer server = new HTTPServer(PORT);

		server.on("/api/v1/login/state", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				response.write("{\"state\":\"NOT_LOGGED_IN\"}");
			}
		});

		server.on("/api/v1/login", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				response.write("{\"message\":\"Backend Error\"}");
			}
		});
		
		server.on("/api/v1/version", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				DriveQueue queue = drive.enqueue(ChipDrive.VERSION, request, response);
			}
		});

		server.on("/api/v1/getDriveConfig", new HTTPRoute() {
			@Override public void handle(Request request, Response response) throws IOException {
				DriveQueue queue = drive.enqueue(ChipDrive.CONFIG, request, response);
			}
		});

		server.on("/api/v1/drive/{method}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				String mode = request.getArgs("method");
				switch(mode) {
					case "files.list":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.LIST, request, response);
						}
					break;
					case "file.link":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.LINK, request, response);
						}
					break;
					case "new.upload":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.UPLOAD, request, response);
		        		}
					break;
					case "file.delete":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.DELETE, request, response);
						}
					break;
					case "new.folder":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.FOLDER, request, response);
						}
					break;
					case "item.rename":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.RENAME, request, response);
						}
					break;
					case "item.info":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.INFO, request, response);
						}
					break;
					case "drive.quota":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.QUOTA, request, response);
						}
					break;
					case "item.stream":
						{
							DriveQueue queue = drive.enqueue(ChipDrive.STREAM, request, response);
						}
					break;
					default:
						{
							DriveQueue queue = drive.enqueue(ChipDrive.UNKNOWN, request, response);
						}
					break;
				}
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
