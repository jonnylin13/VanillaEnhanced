package com.github.jonnylin13.ve.objects;

import com.github.jonnylin13.ve.objects.generic.SQLObject;

public class Group extends SQLObject {
	
	private transient String name;
	private String[] permissions;
	private boolean isDefault;
	
	public Group(String[] permissions, boolean isDefault) {
		this.name = null;
		this.permissions = permissions;
		this.isDefault = isDefault;
	}
	
	public Group(String[] permissions) {
		this.name = null;
		this.permissions = permissions;
		this.isDefault = false;
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

}
