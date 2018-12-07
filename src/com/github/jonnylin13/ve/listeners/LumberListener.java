package com.github.jonnylin13.ve.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.jonnylin13.ve.VEPlugin;

public class LumberListener implements Listener {

	public LumberListener() {
		Bukkit.getPluginManager().registerEvents(this, VEPlugin.getInstance());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("ve.woodcutter")) return;
		if (!VEPlugin.getInstance().getUser(player).getWoodcutting()) return;
		
		this.breakTree(event.getBlock());
	}

	private void breakTree(Block tree) {
		if (this.isTree(tree)) {
			tree.breakNaturally();
			for (Block b : this.getBlocks(tree, 1)) {
				this.breakTree(b);
			}
		}
	}

	private boolean isTree(Block tree) {
		return (tree.getType().toString().contains("LOG"));
	}

	private List<Block> getBlocks(Block start, int radius) {
		if (radius < 0) {
			return new ArrayList<Block>(0);
		}
		int iterations = (radius * 2) + 1;
		List<Block> blocks = new ArrayList<Block>(iterations * iterations * iterations);
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					blocks.add(start.getRelative(x, y, z));
				}
			}
		}
		return blocks;
	}

}
