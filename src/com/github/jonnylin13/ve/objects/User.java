package com.github.jonnylin13.ve.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.permissions.PermissionAttachment;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.Queries;
import com.github.jonnylin13.ve.objects.generic.SQLObject;
import com.github.jonnylin13.ve.tools.QueryParser;
import com.google.gson.Gson;

public class User extends SQLObject {

	private String name;
	private UUID uuid;
	private String[] groups;
	private transient PermissionAttachment permissions;
	private transient boolean woodcutting;

	public User(String name, UUID uuid, String[] groups) {
		this.name = name;
		this.uuid = uuid;
		this.groups = groups;
		this.permissions = null;
		this.woodcutting = false;
	}
	
	public String matchCondition() {
		return "uuid = '" + this.uuid + "'";
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public String[] getGroups() {
		return this.groups;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public User load() {
		try {
			
			Statement statement = VEPlugin.getInstance().getDb().getConnection().createStatement();
			ResultSet rs = statement.executeQuery(QueryParser.parseSelect(Queries.USERS, matchCondition()));
			if (!rs.isBeforeFirst()) {
				this.insert();
			} else {
				rs.first();
				this.uuid = UUID.fromString(rs.getString("uuid"));
				this.name = rs.getString("name");
				Gson gson = new Gson();
				this.groups = gson.fromJson(rs.getString("groups"), String[].class);
				rs.close();
			}
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void update() {
		try {
			Statement statement = VEPlugin.getInstance().getDb().getConnection().createStatement();
			Gson gson = new Gson();
			statement.execute(QueryParser.parseUpdate(Queries.USERS, "groups = '" + gson.toJson(this.groups) + "'",
					matchCondition()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateAsync() {
		VEPlugin.getInstance().getServer().getScheduler().runTaskLater(VEPlugin.getInstance(), new UpdateAsync(this), 0L);
	}
	
	public class UpdateAsync implements Runnable {
		private User user;
		
		public UpdateAsync(User user) {
			this.user = user;
		}
		
		public void run() {
			this.user.update();
			VEPlugin.log.info("<VE> " + this.user.getUUID().toString() + " update task completed!");
		}
	}
	
	public PermissionAttachment getPermissions() {
		return this.permissions;
	}
	
	public void setPermissions(PermissionAttachment permissions) {
		this.permissions = permissions;
	}
	
	public boolean toggleWoodcutting() {
		this.woodcutting = !this.woodcutting;
		return this.woodcutting;
	}
	
	public boolean getWoodcutting() {
		return this.woodcutting;
	}

}
