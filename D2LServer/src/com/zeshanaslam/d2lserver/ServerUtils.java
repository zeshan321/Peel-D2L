package com.zeshanaslam.d2lserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;

public class ServerUtils {

	public void writeResponse(HttpExchange t, String response) {
		try {
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
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
		Login, Invaild
	}

	public String getError(ErrorType type) {
		String error = null;
		JSONObject jsonObject = new JSONObject();
		try {
			switch (type) {
			case Login:
				jsonObject.put("status", "failed");
				jsonObject.put("data", "Invaild login excpetion");
			case Invaild:
				jsonObject.put("status", "failed");
				jsonObject.put("data", "Invaild type or missing parameters");
			}

			error = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return error;
	}

	public String returnData(List<String> list) {
		String data = null;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "success");
			jsonObject.put("data", list);

			data = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return data;
	}
}
