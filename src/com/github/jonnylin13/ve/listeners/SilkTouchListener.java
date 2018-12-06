package com.github.jonnylin13.ve.listeners;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SilkTouchListener implements Listener {
	
	public SilkTouchListener() {
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (event.getBlock().getType() == Material.SPAWNER) {
			if (player.getItemOnCursor().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
				CreatureSpawner cs = (CreatureSpawner) event.getBlock();
				
			}
				
			// MaxNearbyEntities
			// MaxSpawnDelay
			// MinSpawnDelay
			// RequiredPlayerRange
			// SpawnCount
			// SpawnedType
			// SpawnRange
			
		}
	}

}
