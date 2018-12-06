package com.github.jonnylin13.ve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jonnylin13.ve.db.FileDatabase;
import com.github.jonnylin13.ve.db.SQLDatabase;
import com.github.jonnylin13.ve.listeners.LoginListener;
import com.github.jonnylin13.ve.objects.Config;
import com.github.jonnylin13.ve.objects.Group;
import com.github.jonnylin13.ve.objects.User;

public class VEPlugin extends JavaPlugin {

	public static Logger log;
	private static VEPlugin instance;

	static {
		log = Logger.getLogger("Minecraft:VanillaEnhanced");
	}

	// Custom stuff
	private FileDatabase fileDb;
	private SQLDatabase sqlDb;
	private Map<UUID, User> users;
	private Map<String, Group> groups;
	private Config config;
	
	// Listeners
	public LoginListener loginListener;

	public VEPlugin() {
		instance = this;
	}

	// ================
	// Override Methods
	// ================

	@Override
	public void onEnable() {
		
		this.users = new HashMap<UUID, User>();
		this.groups = new HashMap<String, Group>();
		this.fileDb = new FileDatabase(this);
		
		try {
			this.groups = this.fileDb.loadGroups();
			this.config = this.fileDb.loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.sqlDb = new SQLDatabase(this);
		this.loginListener = new LoginListener(this);
		
		VEPlugin.log.info("<VE> " + this.getName() + " has been enabled!");

	}

	@Override
	public void onDisable() {
		try {
			
			this.fileDb.save(this.fileDb.getGroupsFile(), this::getGroups);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.sqlDb.closeConnection();
		VEPlugin.log.info("<VE> " + this.getName() + " has been disabled!");
	}

	// ==========
	// Public API
	// ==========

	public Map<String, Group> getGroups() {
		return this.groups;
	}

	public static VEPlugin getInstance() {
		return instance;
	}

	public Map<UUID, User> getUsers() {
		return this.users;
	}
	
	public void addUser(User user) {
		this.users.put(user.getUUID(), user);
	}
	
	public void removeUser(User user) {
		this.users.remove(user.getUUID());
	}
	
	public User getUser(Player player) {
		UUID uuid = player.getUniqueId();
		if (!this.users.containsKey(uuid)) return null;
		return this.users.get(uuid);
	}

	public FileDatabase getFileDb() {
		return this.fileDb;
	}
	
	public SQLDatabase getDb() {
		return this.sqlDb;
	}
	
	public Config getCfg() {
		return this.config;
	}
	
	public boolean setPermissions(Player player, User user) {
		for (String groupName : user.getGroups()) {
			Group group = this.groups.get(groupName);
			
			if (group == null) {
				// TODO: Handle null
				continue;
			}
			
			if (group.getName() == null) group.setName(groupName);
			log.info("<VE> Checking group name: " + group.getName());
			for (String permission : group.getPermissions()) {
				log.info("<VE> Attaching permission: " + permission + " to " + player.getName());
				player.addAttachment(this, permission, true);
			}
		}
		return true;
	}

	public String[] getDefaultGroups() {
		List<Group> defaults = new ArrayList<Group>(this.groups.values());
		defaults.removeIf(p -> (!p.isDefault()));
		String[] defaultsArr = new String[defaults.size()];
		for (int i = 0; i < defaults.size(); i++) {
			defaultsArr[i] = defaults.get(i).getName();
		}
		return defaultsArr;
	}

}
