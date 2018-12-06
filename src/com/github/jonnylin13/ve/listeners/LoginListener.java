package com.github.jonnylin13.ve.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.VEUser;

public class LoginListener implements Listener {
	
	private VEPlugin vep;
	
	public LoginListener(VEPlugin instance) {
		this.vep = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (this.vep.userExists(player.getUniqueId())) {
			this.vep.setPermissions(player);
		} else {
			VEUser newUser = new VEUser(player.getUniqueId(), this.vep.getDefaultGroups());
			VEPlugin.log.info(newUser.toString());
			this.vep.addUser(newUser);
			this.vep.setPermissions(player);
			// TODO: Save player async
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// TODO: Save player sync
	}

}
