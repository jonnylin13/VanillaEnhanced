package com.github.jonnylin13.ve.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.EntityType;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.Queries;
import com.github.jonnylin13.ve.objects.generic.SQLObject;
import com.github.jonnylin13.ve.tools.QueryParser;

public class Spawner extends SQLObject {

	private String shortId;
	private String spawnedType;

	public Spawner(String shortId, String spawnedType) {
		this.shortId = shortId;
		this.spawnedType = spawnedType;
	}
	
	public Spawner(String shortId) {
		this.shortId = shortId;
		this.spawnedType = "";
		this.load();
	}

	// ==========
	// Public API
	// ==========

	public String matchCondition() {
		return "id = " + wrap(shortId);
	}

	@Override
	public void insert() {
		try {
			VEPlugin.getInstance().getDb().execute(QueryParser.parseInsert(Queries.SPAWNERS, "id, spawnedType",
					wrap(this.shortId) + ", " + wrap(this.spawnedType)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertAsync() {
		// TODO: Method stub
	}

	public void update() {
		try {
			VEPlugin.getInstance().getDb().execute(QueryParser.parseUpdate(Queries.SPAWNERS,
					"spawnedType = '" + this.spawnedType + "'", "where " + matchCondition()));
			// TODO: Maybe report?
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateAsync() {
		// TODO: Method stub
	}

	public void delete() {
		try {
			VEPlugin.getInstance().getDb().execute(QueryParser.parseDelete(Queries.SPAWNERS, matchCondition()));
			// TODO: Reporting?
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Spawner load() {
		try {
			ResultSet rs = VEPlugin.getInstance().getDb()
					.query(QueryParser.parseSelect(Queries.SPAWNERS, matchCondition()));
			if (!rs.isBeforeFirst())
				return this; // TODO: Handle not found
			rs.first();
			this.shortId = rs.getString("id");
			this.spawnedType = rs.getString("spawnedType");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getSpawnedType() {
		return this.spawnedType;
	}

	public EntityType getSpawnedEntityType() {
		return EntityType.valueOf(this.spawnedType);
	}

	public String getShortId() {
		return this.shortId;
	}

	public void setId(String shortId) {
		this.shortId = shortId;
	}

}
