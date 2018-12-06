package com.github.jonnylin13.ve.objects;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.EntityType;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.generic.SQLObject;

public class Spawner extends SQLObject {
	
	private int maxNearbyEntities;
	private int maxSpawnDelay;
	private int minSpawnDelay;
	private int spawnCount;
	private String spawnedType;
	private int spawnRange;
	
	public Spawner(int maxNearbyEntities, int maxSpawnDelay, int minSpawnDelay, int spawnCount, String spawnedType, int spawnRange) {
		this.maxNearbyEntities = maxNearbyEntities;
		this.maxSpawnDelay = maxSpawnDelay;
		this.minSpawnDelay = minSpawnDelay;
		this.spawnCount = spawnCount;
		this.spawnedType = spawnedType;
		this.spawnRange = spawnRange;
	}
	
	@Override
	public void insert() {
		try {
			Statement statement = VEPlugin.getInstance().getDb().getConnection().createStatement();
			// TODO: TIRED AF
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getMaxNearbyEntities() {
		return this.maxNearbyEntities;
	}
	
	public int getMaxSpawnDelay() {
		return this.maxSpawnDelay;
	}
	
	public int getMinSpawnDelay() {
		return this.minSpawnDelay;
	}
	
	public int getSpawnCount() {
		return this.spawnCount;
	}
	
	public String getSpawnedType() {
		return this.spawnedType;
	}
	
	public EntityType getSpawnedEntityType() {
		return EntityType.valueOf(this.spawnedType);
	}
	
	public int getSpawnRange() {
		return this.spawnRange;
	}
	

}
