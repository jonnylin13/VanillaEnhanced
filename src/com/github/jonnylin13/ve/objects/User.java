package com.github.jonnylin13.ve.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.Queries;
import com.github.jonnylin13.ve.objects.generic.CustomEntity;
import com.github.jonnylin13.ve.tools.QueryParser;
import com.github.jonnylin13.ve.tools.Toolz;
import com.google.gson.Gson;

public class User extends CustomEntity {

	private String name;
	private String[] groups;
	private transient PermissionAttachment permissions;
	private transient boolean woodcutting;
	private int xp;

	public User(String name, UUID uuid, String[] groups) {
		super(uuid, 1);
		this.name = name;
		this.groups = groups;
		this.permissions = null;
		this.woodcutting = false;
		this.xp = 0;
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
				this.setUUID(UUID.fromString(rs.getString("uuid")));
				this.name = rs.getString("name");
				Gson gson = new Gson();
				this.groups = gson.fromJson(rs.getString("groups"), String[].class);
				this.setLevel(rs.getInt("level"));
				this.xp = rs.getInt("xp");
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
			statement.execute(QueryParser.parseUpdate(Queries.USERS, "groups = " + wrap(gson.toJson(this.groups))
					+ ", level = " + String.valueOf(this.getLevel())
					+ ", xp = " + String.valueOf(this.xp),
					matchCondition()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean setPermissions(Player player) {
		PermissionAttachment permissions = player.addAttachment(VEPlugin.getInstance());
		for (String groupName : this.getGroups()) {
			Group group = VEPlugin.getInstance().getGroup(groupName);
			
			if (group == null) {
				// TODO: Handle null
				return false;
			}
			
			if (group.getName() == null) group.setName(groupName);
			for (String permission : group.getPermissions()) {
				permissions.setPermission(permission, true);
			}
		}
		this.permissions = permissions;
		return true;
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
	
	public boolean toggleWoodcutting() {
		this.woodcutting = !this.woodcutting;
		return this.woodcutting;
	}
	
	public boolean getWoodcutting() {
		return this.woodcutting;
	}
	
	public int getXp() {
		return this.xp;
	}
	
	public boolean addXp(int add) {
		this.xp += add;
		if (this.xp >= Toolz.getXpRequired(this.getLevel() + 1)) {
			this.setLevel(this.getLevel() + 1);
			return true;
		}
		return false;
	}

}
