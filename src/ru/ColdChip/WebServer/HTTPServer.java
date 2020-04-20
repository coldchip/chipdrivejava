/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.net.*;
import java.io.IOException;
import java.util.*;

public class HTTPServer implements Runnable { 

	private LinkedHashMap<String, HTTPRoute> map = new LinkedHashMap<String, HTTPRoute>();

	private Socket connect;
	private int port = 80;
	
	public HTTPServer(int port) {
		this.port = port;
	}

	public void on(String path, HTTPRoute route) {
		map.put(path, route);
	}

	@Override
	public void run() {
		int port = this.port;
		try {
			ServerSocket serverConnect = new ServerSocket(port);
			long index = 0;
			while (true) {
				ServerThread myServer = new ServerThread(map);
				Socket client = serverConnect.accept();
				Thread thread = new Thread(myServer);
				client.setSoTimeout(30000);
				myServer.attachClient(client);
				thread.start();
				index++;
				System.out.println(index);
			}
		} catch (IOException e) {
			System.err.println("Server Connection error: " + e.getMessage());
		}
	}
	
}