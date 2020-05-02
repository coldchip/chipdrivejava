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
		ChipDrive.log("ColdChip Drive V2.3.2 Java");
		ChipDrive.log("--------------------------");
		ChipDrive.log("Starting ChipDrive HTTP On Port " + PORT);
		HTTPServer server = new HTTPServer(PORT);
		ChipDrive.log("HTTP Server Started");

		//ChipDrive drive = new ChipDrive();
		
		

		

		server.on("/{*}", new HTTPRoute() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				response.serve("html");
			}
		});

		server.run();
	}

}
