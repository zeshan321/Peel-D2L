package com.zeshanaslam.d2lserver;

import com.zeshanaslam.d2lhook.D2LHook;

public class DataObject {

	public String password;
	public D2LHook d2lHook;
	public Long timestamp;
	
	public DataObject(String password, Long timestamp, D2LHook d2lHook) {
		this.password = password;
		this.d2lHook = d2lHook;
		this.timestamp = timestamp;
	}
}
