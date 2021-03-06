package com.github.jonnylin13.ve.listeners;


import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.RPG;
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
		AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(RPG.MIN_HEALTH + newUser.getLevel());
		VEPlugin.getInstance().addUser(newUser);
		if (!newUser.setPermissions(player)) {
			VEPlugin.log.info("<VE> Could not set permissions for player: " + player.getName());
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// TODO: Save player async
		Player player = event.getPlayer();
		User user = VEPlugin.getInstance().getUser(player);
		if (user != null) {
			// player.removeAttachment(user.getPermissions()); TODO: Investigate if needed and error
			user.updateAsync();
			VEPlugin.getInstance().removeUser(user);
		}
		
	}

}
