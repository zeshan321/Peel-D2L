package com.zeshanaslam.d2lhook;

import org.json.JSONException;
import org.json.JSONObject;

public class LockerObject {
	
	public String name;
	public String link;
	
	public LockerObject(String name, String link) {
		this.name = name;
		this.link = link;
	}
	
	@Override
	public String toString() {
		String objectString = null;
		try {
			
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("link", link);
		
		objectString = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return objectString;
	}
}
