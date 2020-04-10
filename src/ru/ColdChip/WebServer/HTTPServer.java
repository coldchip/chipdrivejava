/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.util.HashMap;

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
				myServer.attachClient(client);
				thread.start();
			}
		} catch (IOException e) {
			System.err.println("Server Connection error: " + e.getMessage());
		}
	}
	
}