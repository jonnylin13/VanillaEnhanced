package com.github.jonnylin13.ve.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;

public class LoginListener implements Listener {
	
	private VEPlugin vep;
	
	public LoginListener(VEPlugin instance) {
		this.vep = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		User newUser = new User(player.getUniqueId(), this.vep.getDefaultGroups());
		newUser = this.vep.getDb().loadUser(newUser);
		this.vep.addUser(newUser);
		if (!this.vep.setPermissions(player, newUser)) {
			VEPlugin.log.info("<VE> Could not set permissions for player: " + player.getName());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// TODO: Save player async
		Player player = event.getPlayer();
		User user = this.vep.getUser(player);
		if (user != null) {
			this.vep.removeUser(user);
		}
		
		
	}

}
