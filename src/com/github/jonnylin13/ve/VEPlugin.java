package com.github.jonnylin13.ve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.jonnylin13.ve.commands.member.WoodCommand;
import com.github.jonnylin13.ve.commands.mod.GroupCommand;
import com.github.jonnylin13.ve.db.FileDatabase;
import com.github.jonnylin13.ve.db.SQLDatabase;
import com.github.jonnylin13.ve.listeners.LoginListener;
import com.github.jonnylin13.ve.listeners.LumberListener;
import com.github.jonnylin13.ve.listeners.SilkTouchListener;
import com.github.jonnylin13.ve.objects.Config;
import com.github.jonnylin13.ve.objects.Group;
import com.github.jonnylin13.ve.objects.User;

public class VEPlugin extends JavaPlugin {

	public static Logger log;
	private static VEPlugin instance;

	static {
		log = Logger.getLogger("VE");
	}

	// Custom stuff
	private FileDatabase fileDb;
	private SQLDatabase sqlDb;
	private Map<UUID, User> users;
	private Map<String, Group> groups;
	private Config config;
	

	private ProtocolManager protocolManager;

	public VEPlugin() {
		instance = this;
	}
	
	// ===========
	// Private API
	// ===========
	
	private void registerListeners() {
		new LoginListener();
		new SilkTouchListener();
		new LumberListener();
	}
	
	private void registerCommands() {
		new GroupCommand();
		new WoodCommand();
	}
	
	// ================
	// Override Methods
	// ================

	@Override
	public void onEnable() {
		
		this.protocolManager = ProtocolLibrary.getProtocolManager();
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
		this.registerListeners();
		this.registerCommands();
		
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
		// TODO: Detach attachments? + save all players sync?
		VEPlugin.log.info("<VE> " + this.getName() + " has been disabled!");
	}

	// ==========
	// Public API
	// ==========

	public Map<String, Group> getGroups() {
		return this.groups;
	}
	
	public Group getGroup(String name) {
		if (this.groups.containsKey(name)) return this.groups.get(name);
		return null;
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
	
	public User getUser(OfflinePlayer player) {
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
		PermissionAttachment permissions = player.addAttachment(VEPlugin.getInstance());
		for (String groupName : user.getGroups()) {
			Group group = this.groups.get(groupName);
			
			if (group == null) {
				// TODO: Handle null
				return false;
			}
			
			if (group.getName() == null) group.setName(groupName);
			log.info("<VE> Checking group name: " + group.getName());
			for (String permission : group.getPermissions()) {
				log.info("<VE> Attaching permission: " + permission + " to " + player.getName());
				permissions.setPermission(permission, true);
			}
		}
		user.setPermissions(permissions);
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
	
	public ProtocolManager getProtocolManager() {
		return this.protocolManager;
	}

}
