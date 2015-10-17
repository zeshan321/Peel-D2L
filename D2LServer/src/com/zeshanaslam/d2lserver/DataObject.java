package com.zeshanaslam.d2lserver;

import com.zeshanaslam.d2lhook.D2LHook;

public class DataObject {

	public String password;
	public D2LHook d2lHook;
	
	public DataObject(String password, D2LHook d2lHook) {
		this.password = password;
		this.d2lHook = d2lHook;
	}
}
