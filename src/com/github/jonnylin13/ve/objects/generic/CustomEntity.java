package com.github.jonnylin13.ve.objects.generic;

import java.util.UUID;

public abstract class CustomEntity extends SQLObject {
	
	private UUID uuid;
	private int level;
	
	public CustomEntity(UUID uuid,int level) {
		this.uuid = uuid;
		this.level = level;
	}


	@Override
	public String matchCondition() {
		return "uuid = '" + this.uuid.toString() + "'";
	}

	public UUID getUUID() {
		return this.uuid;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
}
