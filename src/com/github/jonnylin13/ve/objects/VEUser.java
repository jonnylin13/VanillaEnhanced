package com.github.jonnylin13.ve.objects;

import java.util.UUID;

import com.github.jonnylin13.ve.objects.generic.VEObject;

public class VEUser extends VEObject {
	
	private UUID uuid;
	private String[] groups;
	
	public VEUser(UUID uuid, String[] groups) {
		this.uuid = uuid;
		this.groups = groups;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public String[] getGroups() {
		return this.groups;
	}
	
}
