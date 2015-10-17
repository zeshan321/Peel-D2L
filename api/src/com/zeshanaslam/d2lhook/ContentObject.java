package com.zeshanaslam.d2lhook;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentObject {

	public String name;
	public List<String> subContent;
	
	public ContentObject(String name, List<String> subContent) {
		this.name = name;
		this.subContent = subContent;
	}
	
	@Override
	public String toString() {
		String objectString = null;
		try {
			
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("subContent", subContent);
		
		objectString = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return objectString;
	}
}
