package com.zeshanaslam.d2lserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class ServerUtils {

	public void writeResponse(HttpExchange t, String response) {
		try {
			t.sendResponseHeaders(200, response.length());

			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.flush();
			os.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> queryToMap(String query){
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

	public enum ErrorType {
		Login, Invalid
	}

	public String getError(ErrorType type) {
		JSONObject jsonObject = new JSONObject();
		try {
			switch (type) {
			case Login:
				jsonObject.put("status", "failed");
				jsonObject.put("data", "Invalid login exception");
				return jsonObject.toString(2);
			case Invalid:
				jsonObject.put("status", "failed");
				jsonObject.put("data", "Invalid type or missing parameters");
				return jsonObject.toString(2);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String returnData(JSONArray array) {
		JSONObject jsonObject = null;
		String data = null;

		try {
			jsonObject = new JSONObject();
			jsonObject.put("status", "success");
			jsonObject.put("data", array);

			data = jsonObject.toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return data;
	}
}
