package com.github.jonnylin13.ve.objects;

import com.github.jonnylin13.ve.objects.generic.JSONObject;

public class Config extends JSONObject {

	private String host, database, username, password;

	public Config(String host, String database, String username, String password) {
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
