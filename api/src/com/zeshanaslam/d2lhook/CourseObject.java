package com.zeshanaslam.d2lhook;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseObject {

	public String ID;
	public String name;
	
	public CourseObject(String ID, String name) {
		this.ID = ID;
		this.name = name;
	}
	
	@Override
	public String toString() {
		String objectString = null;
		try {
			
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ID", ID);
		jsonObject.put("name", name);
		
		objectString = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return objectString;
	}
}
