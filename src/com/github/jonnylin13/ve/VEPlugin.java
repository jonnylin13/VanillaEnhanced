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
import com.github.jonnylin13.ve.objects.VEConfig;
import com.github.jonnylin13.ve.objects.VEGroup;
import com.github.jonnylin13.ve.objects.VEUser;

public class VEPlugin extends JavaPlugin {

	public static Logger log;
	private static VEPlugin instance;

	static {
		log = Logger.getLogger("Minecraft:VanillaEnhanced");
	}

	// Custom stuff
	private FileDatabase fileDb;
	private SQLDatabase sqlDb;
	private Map<UUID, VEUser> users;
	private Map<String, VEGroup> groups;
	private VEConfig config;
	
	// Listeners
	public LoginListener loginListener;

	public VEPlugin() {
		
		instance = this;
		// Instantiate non-Bukkit services
		this.users = new HashMap<UUID, VEUser>();
		this.groups = new HashMap<String, VEGroup>();
		this.fileDb = new FileDatabase(this);
		this.sqlDb = new SQLDatabase(this);
	}

	// ================
	// Override Methods
	// ================

	@Override
	public void onEnable() {
		// First time load
		try {
			this.config = this.fileDb.loadConfig();
			this.groups = this.fileDb.loadGroups();
			// TODO: Load users from MySQL
			// this.users = this.db.loadUsers();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Instantiate Bukkit related services
		this.loginListener = new LoginListener(this);
		VEPlugin.log.info("<VE> " + this.getName() + " has been enabled!");

	}

	@Override
	public void onDisable() {
		try {
			
			this.fileDb.save(this.fileDb.getUsersFile(), this::getUsers);
			this.fileDb.save(this.fileDb.getGroupsFile(), this::getGroups);
		} catch (IOException e) {
			e.printStackTrace();
		}
		VEPlugin.log.info("<VE> " + this.getName() + " has been disabled!");
	}

	// ==========
	// Public API
	// ==========

	public Map<String, VEGroup> getGroups() {
		return this.groups;
	}

	public static VEPlugin getInstance() {
		return instance;
	}

	public Map<UUID, VEUser> getUsers() {
		return this.users;
	}

	public FileDatabase getFileDb() {
		return this.fileDb;
	}
	
	public SQLDatabase getDb() {
		return this.sqlDb;
	}

	public void addUser(VEUser user) {
		this.users.put(user.getUUID(), user);
	}

	public boolean userExists(UUID uuid) {
		return this.users.containsKey(uuid);
	}
	
	public VEConfig getCfg() {
		return this.config;
	}

	public boolean setPermissions(Player player) {
		UUID uuid = player.getUniqueId();
		if (!userExists(uuid)) {
			// TODO: Handle could not find player
			return false;
		}
		VEUser user = this.users.get(uuid);
		for (String groupName : user.getGroups()) {
			VEGroup group = this.groups.get(groupName);
			if (group == null) {
				// TODO: Handle null
				return false;
			}
			log.info("<VE> Checking group name: " + group.getName());
			if (!group.isDefault())
				continue;
			for (String permission : group.getPermissions()) {
				log.info("<VE> Attaching permission: " + permission + " to " + player.getName());
				player.addAttachment(this, permission, true);
			}
		}
		return true;
	}

	public String[] getDefaultGroups() {
		List<VEGroup> defaults = new ArrayList<VEGroup>(this.groups.values());
		defaults.removeIf(p -> (!p.isDefault()));
		String[] defaultsArr = new String[defaults.size()];
		for (int i = 0; i < defaults.size(); i++) {
			defaultsArr[i] = defaults.get(i).getName();
		}
		return defaultsArr;
	}

}
