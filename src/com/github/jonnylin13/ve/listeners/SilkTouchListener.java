package com.github.jonnylin13.ve.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.Spawner;
import com.github.jonnylin13.ve.tools.Toolz;

public class SilkTouchListener implements Listener {

	public SilkTouchListener() {
		VEPlugin.getInstance().getServer().getPluginManager().registerEvents(this, VEPlugin.getInstance());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (block.getType() == Material.SPAWNER) {
			if (player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {

				if (!player.hasPermission("ve.spawner.break") && !player.isOp()) {
					event.setCancelled(true);
					return;
				}
				event.setExpToDrop(0);
				CreatureSpawner cs = (CreatureSpawner) block.getState();
				ItemStack drop = new ItemStack(Material.SPAWNER);
				ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.SPAWNER);
				List<String> lore = new ArrayList<String>();
				Spawner spawner = new Spawner(Toolz.generateShortId(), cs.getSpawnedType().toString());
				spawner.insert();
				meta.setDisplayName(Toolz.prettyEnum(spawner.getSpawnedType()) + " Spawner");
				lore.add(spawner.getShortId());
				meta.setLore(lore);
				drop.setItemMeta(meta);
				block.getWorld().dropItemNaturally(block.getLocation(), drop);
				VEPlugin.log.info("<VE> Stored a " + spawner.getSpawnedType().toLowerCase() + " spawner!");
			}

		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		if (block.getType() == Material.SPAWNER) {
			if (!player.hasPermission("ve.spawner.place") && !player.isOp()) {
				event.setCancelled(true);
				return;
			}
			ItemStack item = event.getItemInHand();
			if (!item.hasItemMeta()) return;
			if (!this.isSpawner(item.getItemMeta())) return;
			
			Spawner spawner = new Spawner(this.getIdString(item.getItemMeta()));
			block.setType(Material.SPAWNER);
			BlockState state = block.getState();
			CreatureSpawner cs = (CreatureSpawner) state;
			cs.setSpawnedType(spawner.getSpawnedEntityType());
			
			Bukkit.getScheduler().runTaskLater(VEPlugin.getInstance(), () -> {
				
				state.update();
				spawner.delete();
				
			}, 0L);
			if (player.getGameMode() == GameMode.CREATIVE) {
				
				// TODO: Reimplement this to be more robust
				PlayerInventory inv = player.getInventory();
				ItemStack mainHand = inv.getItemInMainHand();
				mainHand.setAmount(0);
				
			}
			VEPlugin.log.info("<VE> Created a " + spawner.getSpawnedType().toLowerCase() + " spawner!");
		}
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		Item item = event.getEntity();
		ItemStack itemStack = item.getItemStack();
		if (!itemStack.hasItemMeta()) return;
		ItemMeta meta = itemStack.getItemMeta();
		if (this.isSpawner(meta)) {
			Spawner spawner = new Spawner(this.getIdString(meta));
			spawner.delete();
		}
	}
	
	private String getIdString(ItemMeta meta) {
		return meta.getLore().get(0);
	}
	
	private boolean isSpawner(ItemMeta meta) {
		return meta.hasDisplayName() && meta.getDisplayName().contains(" Spawner");
	}
	

}
