package com.github.jonnylin13.ve.objects;

import com.github.jonnylin13.ve.objects.generic.SQLObject;

public class Group extends SQLObject {
	
	private transient String name;
	private String[] permissions;
	private boolean isDefault;
	private int level;
	
	public Group(String[] permissions, boolean isDefault, int level) {
		this.name = null;
		this.permissions = permissions;
		this.isDefault = isDefault;
		this.level = level;
	}
	
	public Group(String[] permissions, int level) {
		this.name = null;
		this.permissions = permissions;
		this.isDefault = false;
		this.level = level;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isDefault() {
		return this.isDefault;
	}
	
	public String[] getPermissions() {
		return this.permissions;
	}
	
	public int getLevel() {
		return this.level;
	}

}
