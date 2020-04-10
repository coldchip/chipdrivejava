package ru.ColdChip.ChipDrive;

import ru.ColdChip.WebServer.*;
import ru.ColdChip.WebServer.Exceptions.TokenNotFoundException;
import ru.ColdChip.ChipDrive.Routes.*;
import org.JSON.*;

public class Server {

	private static final int PORT = 9010;

	private static final double VERSION = 1.1;
	
	public static void main(String[] args) {

		try {

			System.out.println("ColdChip Drive V2.2.1 Java");
			System.out.println("--------------------------");
			HTTPServer server = new HTTPServer(PORT);
			ChipDriveRoute route = new ChipDriveRoute(server);
			route.serve();
			server.run();

		} catch(Exception e) {
			
		}
	}

}
