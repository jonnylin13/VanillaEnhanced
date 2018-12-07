package com.github.jonnylin13.ve.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;

public class LoginListener implements Listener {
	
	public LoginListener() {
		VEPlugin.getInstance().getServer().getPluginManager().registerEvents(this, VEPlugin.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		User newUser = new User(player.getName(), player.getUniqueId(), VEPlugin.getInstance().getDefaultGroups());
		newUser = newUser.load();
		VEPlugin.getInstance().addUser(newUser);
		if (!VEPlugin.getInstance().setPermissions(player, newUser)) {
			VEPlugin.log.info("<VE> Could not set permissions for player: " + player.getName());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// TODO: Save player async
		Player player = event.getPlayer();
		User user = VEPlugin.getInstance().getUser(player);
		if (user != null) {
			user.updateAsync();
			VEPlugin.getInstance().removeUser(user);
		}
		player.removeAttachment(user.getPermissions());
		
	}

}
