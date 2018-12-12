package com.github.jonnylin13.ve.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.RPG;
import com.github.jonnylin13.ve.tools.Mobz;

public class MobListener implements Listener {

	public MobListener() {
		Bukkit.getPluginManager().registerEvents(this, VEPlugin.getInstance());
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		EntityType type = event.getRightClicked().getType();
		ItemStack item = event.getHand() == EquipmentSlot.HAND ? event.getPlayer().getInventory().getItemInMainHand()
				: event.getPlayer().getInventory().getItemInOffHand();
		if (item.getType() != Material.NAME_TAG)
			return;
		if (type == EntityType.ZOMBIE)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity damaged = event.getEntity();
		if (event.getEntityType() == EntityType.ZOMBIE) {
			Zombie zombie = (Zombie) damaged;
			String oldName = damaged.getCustomName();
			String oldHealth = oldName.split(" ")[2];
			Bukkit.getScheduler().runTaskLater(VEPlugin.getInstance(), () -> {
				damaged.setCustomName(oldName.replace(oldHealth, Mobz.formatHealth(zombie.getHealth(), Mobz.getLevel(damaged))));
				damaged.setCustomNameVisible(true); // Just in case, redundant
			}, 0L);
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		EntityType type = event.getEntityType();
		if (event.getSpawnReason() == SpawnReason.SPAWNER) return;
		if (type == EntityType.ZOMBIE)
			this.onZombieSpawn(event);
	}
	
	private void onZombieSpawn(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		int level = Mobz.getRandomLevel(entity.getLocation());
		Zombie zombie = (Zombie) entity;
		AttributeInstance maxHealth = zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		maxHealth.setBaseValue(RPG.MIN_HEALTH + level);
		zombie.setHealth(RPG.MIN_HEALTH + level);
		String levelStr = String.valueOf(level);
		entity.setCustomName("Level: " + levelStr + " " + Mobz.formatHealth(zombie.getHealth(), level));
		entity.setCustomNameVisible(true);
	}
}
