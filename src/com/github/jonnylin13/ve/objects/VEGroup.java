package com.github.jonnylin13.ve.objects;

import com.github.jonnylin13.ve.objects.generic.VEObject;

public class VEGroup extends VEObject {
	
	private String name;
	private String[] permissions;
	private boolean isDefault;
	
	public VEGroup(String name, String[] permissions, boolean isDefault) {
		this.name = name;
		this.permissions = permissions;
		this.isDefault = isDefault;
	}
	
	public VEGroup(String name, String[] permissions) {
		this.name = name;
		this.permissions = permissions;
		this.isDefault = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isDefault() {
		return this.isDefault;
	}	
	
	public String[] getPermissions() {
		return this.permissions;
	}

}
