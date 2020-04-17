/*

	@copyright: ColdChip

*/
package ru.ColdChip.WebServer;

import java.io.*;
import java.lang.NumberFormatException;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.nio.file.*;
import java.util.regex.*;
import ru.ColdChip.WebServer.Exceptions.*;

public class HTTPServerThread implements Runnable { 

	private Socket connect;

	private LinkedHashMap<String, HTTPRoute> map = new LinkedHashMap <String, HTTPRoute>();

	public HTTPServerThread(LinkedHashMap<String, HTTPRoute> map) {
		this.map = map;
	}

	public void attachClient(Socket c) {
		connect = c;
	}

	@Override
	public void run() {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = connect.getInputStream();
			output = connect.getOutputStream();
			while(true) {
				Header header = readHeader(input);
				if(header.getMethod().equals("POST")) {
					if(header.containsHeader("content-length") && header.containsHeader("content-type")) {
						String contentLength = header.getHeader("content-length");
						String contentType = header.getHeader("content-type");
						if(contentType.equals("application/x-www-form-urlencoded")) {
							try {
								int contentSize = Integer.parseInt(contentLength.replaceAll("[^0-9]", ""));
								if(contentSize > 0 && contentSize < 16000) {
									byte[] data = new byte[contentSize];
									int read = input.read(data, 0, contentSize);
									String queryData = new String(data);
									header.postQuery = splitQuery(queryData);
								}
							} catch(NumberFormatException e) {
								Response responseError = new Response(output);
								responseError.writeText("Invalid Content-Length");
							}
						}
					}
				}
				
				Request request = new Request(input, header);

				Response response = new Response(output);

				request.res = response;
				response.req = request;

		
				boolean isHandled = false;
				for (String key : this.map.keySet()) {
					LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
					String[] pathSegments = cleanPath(key).split("/");
					String[] calledPathSegments = cleanPath(header.getPath()).split("/");
					int score = 0;
					int requiredScore = Math.max(calledPathSegments.length, pathSegments.length);
					for(int i = 0; i < Math.min(calledPathSegments.length, pathSegments.length); i++) {
						if(pathSegments[i].equals(calledPathSegments[i])) {
							score += 1;
						} else if(isDynamic(pathSegments[i]) && !pathSegments[i].equals("{*}")) {
							score += 1;
							parameters.put(stripDynamic(pathSegments[i]), calledPathSegments[i]);
						} else if(pathSegments[i].equals("{*}")) {
							score = requiredScore;
							break;
						} else {
							break;
						}
				
						
					}
					if(score == requiredScore) {
						isHandled = true;
						HTTPRoute route = new HTTPRoute();
						route = this.map.get(key);
						request.args = parameters;
						route.handle(request, response);
						break;
					}
				}
				if(isHandled == false) {
					response.writeText("Unable to process request");
				}
			}
		} catch (IOException e) {

		} catch(RequestException e) {
			
		} finally {
			try {
				input.close();
				output.close();
				connect.close();
			} catch (IOException e) {
				
			}
		}
	}

	private boolean isDynamic(String path) {
        return path.matches("\\{([A-Za-z0-9]{1,}|\\*)\\}");
    }

    public static String stripDynamic(String dynamicPath) {
        return dynamicPath.substring(1, dynamicPath.length() - 1);
    }

	private String readLine(InputStream in, int max) throws IOException, RequestException {
		String result = new String();
		while(true) {
			int readed = in.read();
			if(readed > 0) {
				result += (char)readed;
				if(result.contains("\r\n")) {
					result = result.substring(0, result.length() - 2);
					break;
				}
				if(result.contains("\n")) {
					result = result.substring(0, result.length() - 1);
					break;
				}
				if(result.length() > max) {
					throw new RequestException("Header Too Long");
				}
			} else {
				throw new IOException("Socket closed before reaching to line break");
			}
		}
		return result;
	}

	private Header readHeader(InputStream in) throws IOException, RequestException {
		Header head = new Header();


		String stub = readLine(in, 65535);

		StringTokenizer parse = new StringTokenizer(stub);
		head.setMethod(parse.nextToken().toUpperCase());
		StringTokenizer pathData = new StringTokenizer(parse.nextToken(), "?");
		head.setPath(urlDecode(pathData.nextToken()));
		if(pathData.countTokens() == 1) {
			head.query = splitQuery(pathData.nextToken());
		}
		head.version = parse.nextToken().toLowerCase();

		String headerLine;

		while(true) {
			headerLine = readLine(in, 65535);
			if(headerLine.length() > 0) {
				StringTokenizer headerValues = new StringTokenizer(headerLine);
				String key = headerValues.nextToken(":").toLowerCase();
				String value = ltrim(headerValues.nextToken(":"));
				if(key.length() > 0 && value.length() > 0) {
					head.headers.put(key, value);
				}
			} else {
				break;
			}
		}
		
		return head;
	}

	public static String ltrim(String s) {
	    int i = 0;
	    while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
	        i++;
	    }
	    return s.substring(i);
	}

	private static String urlDecode(String url) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, "UTF-8");
	}

	public static HashMap<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		HashMap<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
		    int idx = pair.indexOf("=");
		    if(idx != -1) {
		    	query_pairs.put(urlDecode(pair.substring(0, idx)), urlDecode(pair.substring(idx + 1)));
			}
		}
		return query_pairs;
	}

	public static String cleanPath(String path) {
        path = path.trim();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }
	
}