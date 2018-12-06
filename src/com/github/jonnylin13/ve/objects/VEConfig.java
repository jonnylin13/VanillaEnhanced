package com.github.jonnylin13.ve.objects;

import com.github.jonnylin13.ve.objects.generic.VEObject;

public class VEConfig extends VEObject {
	
	private String host, database, username, password;
	
	public VEConfig(String host, String database, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public String getDatabase() {
		return this.database;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

}
