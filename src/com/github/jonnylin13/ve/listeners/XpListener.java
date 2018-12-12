package com.github.jonnylin13.ve.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;
import com.github.jonnylin13.ve.tools.Mobz;

import net.md_5.bungee.api.ChatColor;

public class XpListener implements Listener {
	
	public XpListener() {
		Bukkit.getPluginManager().registerEvents(this, VEPlugin.getInstance());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
		LivingEntity ent = event.getEntity();
		EntityDamageEvent dmgEvent = ent.getLastDamageCause();
		Player attacker = ent.getKiller();
		
		if (!Mobz.isCustomZombie(ent)) return;
		if (attacker == null) return;
		
		if (dmgEvent.getCause() == DamageCause.ENTITY_ATTACK) {
			
			Player player = (Player) attacker;
			User user = VEPlugin.getInstance().getUser(player);
			if (user == null) return;
			
			int xp = Mobz.getLevel(ent);
			player.sendMessage(ChatColor.GRAY + String.format("+%s XP", String.valueOf(xp)));
			if (user.addXp(xp)) {
				player.sendMessage(ChatColor.GREEN + "You have gained a level!");
			}
		}
	}

}
