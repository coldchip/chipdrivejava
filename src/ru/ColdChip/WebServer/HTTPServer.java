/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.net.*;
import java.io.IOException;
import java.util.*;

public class HTTPServer { 

	private int port = 80;

	private LinkedHashMap<String, HTTPRoute> map = new LinkedHashMap<String, HTTPRoute>();

	private Socket connect;
	
	public HTTPServer(int port) {
		this.port = port;
	}

	public void on(String path, HTTPRoute route) {
		map.put(path, route);
	}

	public void run() {
		int port = this.port;
		try {
			ServerSocket serverConnect = new ServerSocket(port);
			while (true) {
				HTTPServerThread myServer = new HTTPServerThread(map);
				Socket client = serverConnect.accept();
				Thread thread = new Thread(myServer);
				client.setSoTimeout(30000);
				myServer.attachClient(client);
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Server Connection error: " + e.getMessage());
		}
	}
	
}