package com.zeshanaslam.d2lserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.zeshanaslam.d2lhook.D2LHook;
import com.zeshanaslam.d2lserver.ServerUtils.ErrorType;

import Objects.ContentObject;
import Objects.CourseObject;
import Objects.LockerObject;
import Objects.NotificationObject;

public class Main {

	public static  HashMap<String, DataObject> apiData = new HashMap<>();

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/data", new LoingHandler());
		server.setExecutor(null); // creates a default executor
		server.start();

		// Remove D2Lhook in case of session timeout
		ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
		s.scheduleAtFixedRate(new SessionTask(), 0, 5, TimeUnit.MINUTES);

		// Make sure to add encryption
	}

	// http://localhost:8001/data?user=test&pass=testing&type=
	// type: content, course, locker
	// Options: courseid, linkpreview
	static class LoingHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			ServerUtils serverUtils = new ServerUtils();
			Map<String, String> params = serverUtils.queryToMap(httpExchange.getRequestURI().getQuery()); 

			// Params check
			if (!params.containsKey("user") || !params.containsKey("pass") || !params.containsKey("type")) {
				serverUtils.writeResponse(httpExchange, serverUtils.getError(ErrorType.Invalid));
				return;
			}

			String username = params.get("user"), password = params.get("pass");

			if (apiData.containsKey(username) && apiData.get(username).password.equals(password)) {
				getData(httpExchange, username, params);
			} else {
				D2LHook d2lHook = new D2LHook(username, password);
				d2lHook.loadPage();

				if (d2lHook.loginStatus()) {
					apiData.put(username, new DataObject(password, System.currentTimeMillis(), d2lHook));
					getData(httpExchange, username, params);
				} else {
					serverUtils.writeResponse(httpExchange, serverUtils.getError(ErrorType.Login));
				}
			}

			httpExchange.close();
		}

		public void getData(HttpExchange httpExchange, String user, Map<String, String> params) {
			try {
				ServerUtils serverUtils = new ServerUtils();
				JSONArray jsonArray = new JSONArray();
				D2LHook d2lHook = apiData.get(user).d2lHook;

				switch (params.get("type")) {
				case "course":
					// Sterilize objects
					for (CourseObject courseObject: d2lHook.getCourses()) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", courseObject.name);
						jsonObject.put("ID", courseObject.ID);

						jsonArray.put(jsonObject);
					}

					serverUtils.writeResponse(httpExchange, serverUtils.returnData(jsonArray));
					break;
				case "content":
					try {
						// Params check
						if (!params.containsKey("courseid")) {
							serverUtils.writeResponse(httpExchange, serverUtils.getError(ErrorType.Invalid));
							return;
						}

						// Sterilize objects
						for (ContentObject contentObject: d2lHook.getCourseContent(params.get("courseid"))) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("name", contentObject.name);
							jsonObject.put("subContent", contentObject.subContent);

							jsonArray.put(jsonObject);
						}

						serverUtils.writeResponse(httpExchange, serverUtils.returnData(jsonArray));
					} catch(Exception e) {
						serverUtils.writeResponse(httpExchange, serverUtils.getError(ErrorType.Invalid));
					}
					break;
				case "locker":
					// Params check
					if (!params.containsKey("linkpreview")) {
						serverUtils.writeResponse(httpExchange, serverUtils.getError(ErrorType.Invalid));
						return;
					}

					// Sterilize objects
					for (LockerObject lockerObject: d2lHook.getLocker(Boolean.valueOf(params.get("linkpreview")))) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("name", lockerObject.name);
						jsonObject.put("link", lockerObject.link);

						jsonArray.put(jsonObject);
					}

					serverUtils.writeResponse(httpExchange, serverUtils.returnData(jsonArray));
					break;
				case "notifications":
					// Sterilize objects
					for (NotificationObject notificationObject: d2lHook.getNotifications()) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("title", notificationObject.title);
						jsonObject.put("desc", notificationObject.desc);
						jsonObject.put("date", notificationObject.date);

						jsonArray.put(jsonObject);
					}

					serverUtils.writeResponse(httpExchange, serverUtils.returnData(jsonArray));
					break;
				default:
					serverUtils.writeResponse(httpExchange, serverUtils.getError(ErrorType.Invalid));
				}
			} catch (JSONException e ) {
				e.printStackTrace();
			}
		}
	}
}