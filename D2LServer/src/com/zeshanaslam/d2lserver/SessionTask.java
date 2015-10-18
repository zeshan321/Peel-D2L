package com.zeshanaslam.d2lserver;

import java.util.ArrayList;
import java.util.List;

public class SessionTask implements Runnable {

	@Override
	public void run() {
		List<String> list = new ArrayList<>();
		
		for (String username: Main.apiData.keySet()) {
			DataObject dataObject = Main.apiData.get(username);
			long secondsLeft = ((dataObject.timestamp / 1000) + 600) - (System.currentTimeMillis() / 1000);
			if (secondsLeft <= 0) {
				list.add(username);
			}
		}
		
		for (String username: list) {
			Main.apiData.remove(username);
		}
	}
}
