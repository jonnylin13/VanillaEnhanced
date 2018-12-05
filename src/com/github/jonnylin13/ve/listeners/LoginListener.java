package com.github.jonnylin13.ve.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.jonnylin13.ve.VEPlugin;

public class LoginListener implements Listener {
	
	private VEPlugin vep;
	
	public LoginListener(VEPlugin instance) {
		this.vep = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (this.vep.isRegistered(player.getUniqueId())) {
			// Init player
			this.vep.setPermissions(player);
		} else {
			// Save & init player
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
	}

}
