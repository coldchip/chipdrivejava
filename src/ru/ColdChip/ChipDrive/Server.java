package ru.ColdChip.ChipDrive;

import ru.ColdChip.WebServer.*;
import ru.ColdChip.ChipDrive.Routes.*;
import org.JSON.*;

public class Server {

	private static final int PORT = 9010;

	public static void main(String[] args) {

		try {

			System.out.println("ColdChip Drive V2.3 Java");
			System.out.println("--------------------------");
			HTTPServer server = new HTTPServer(PORT);
			ChipDriveRoute route = new ChipDriveRoute(server);
			route.serve();
			server.run();

		} catch(Exception e) {
			
		}
	}

}
