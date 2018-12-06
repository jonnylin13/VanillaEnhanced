package com.github.jonnylin13.ve.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.github.jonnylin13.ve.objects.generic.SQLObject;
import com.google.gson.Gson;

public class User extends SQLObject {
	
	private UUID uuid;
	private String[] groups;
	
	public User(UUID uuid, String[] groups) {
		this.uuid = uuid;
		this.groups = groups;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String[] getGroups() {
		return this.groups;
	}

	@Override
	public User load(ResultSet rs) {
		try {
			rs.first();
			this.uuid = UUID.fromString(rs.getString("uuid"));
			Gson gson = new Gson();
			this.groups = gson.fromJson(rs.getString("groups"), String[].class);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
