package com.github.jonnylin13.ve;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jonnylin13.ve.db.Database;
import com.github.jonnylin13.ve.listeners.LoginListener;
import com.github.jonnylin13.ve.objects.VEGroup;
import com.github.jonnylin13.ve.objects.VEUser;
import com.github.jonnylin13.ve.permissions.Permissions;

public class VEPlugin extends JavaPlugin {
	
	public static Logger log;
	private static VEPlugin instance;
	
	static {
		log = Logger.getLogger("Minecraft:VanillaEnhanced");
	}
	
	// Custom stuff
	private Database db;
	public Permissions perms;
	private Map<UUID, VEUser> users;
	private Map<String, VEGroup> groups;
	
	// Listeners
	public LoginListener loginListener;
	
	public VEPlugin() {
		// Instantiate non-Bukkit services
		this.users = new HashMap<UUID, VEUser>();
		this.groups = new HashMap<String, VEGroup>();
		this.perms = new Permissions();
		this.db = new Database(this);
		instance = this;
	}
	
	// ================
	// Override Methods
	// ================
	
	@Override
	public void onEnable() {
		// Instantiate Bukkit related services
		this.loginListener = new LoginListener(this);
		
		String[] perms = {"fake.permission", "another.fake.permission"};
		VEGroup testGroup = new VEGroup("Guest", perms);
		String[] testGroups = {testGroup.getName()};
		VEUser testUser = new VEUser(UUID.randomUUID(), testGroups);
		this.users.put(testUser.getUUID(), testUser);
		this.groups.put(testGroup.getName(), testGroup);
		try {
			Database.save(this.db.getUsersFile(), this::getUsers);
			Database.save(this.db.getGroupsFile(), this::getGroups);
			this.users = this.db.loadUsers();
			Database.save(this.db.getUsersFile(), this::getUsers);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		VEPlugin.log.info("<VE> " + this.getName() + " has been enabled!");
		
	}

	@Override
	public void onDisable() {
		
		VEPlugin.log.info("<VE> " + this.getName() + " has been disabled!");
	}
	
	// ==========
	// Public API
	// ==========
	
	public static VEPlugin getInstance() {
		return instance;
	}
	
	public Map<UUID, VEUser> getUsers() {
		return this.users;
	}
	
	public Map<String, VEGroup> getGroups() {
		return this.groups;
	}
	
	public boolean isRegistered(UUID uuid) {
		return this.users.containsKey(uuid);
	}
	
	public boolean setPermissions(Player player) {
		UUID uuid = player.getUniqueId();
		if (!isRegistered(uuid)) {
			// TODO: Handle could find player 
			return false;
		}
		for (String groupName : this.users.get(uuid).getGroups()) {
			VEGroup group = this.groups.get(groupName);
			if (group == null) {
				// TODO: Handle exception
				continue;
			}
			for (String permission : group.getPermissions()) {
				player.addAttachment(this, permission, true);
			}
		}
		return true;
	}
}
