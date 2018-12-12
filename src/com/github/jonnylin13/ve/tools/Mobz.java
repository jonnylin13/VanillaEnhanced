package com.github.jonnylin13.ve.tools;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.constants.RPG;
import com.github.jonnylin13.ve.objects.User;

import net.md_5.bungee.api.ChatColor;

public class Mobz {
	
	public static String formatHealth(double health, int level) {
		ChatColor color = ChatColor.GREEN;
		double ratio = health / (RPG.MIN_HEALTH + level);
		int filled = (int) Math.round(ratio * 10);
		int empty = 10 - filled;
		color = filled <= 5 ? ChatColor.YELLOW : color;
		color = filled <= 3 ? ChatColor.RED : color;
		String filledStr = "[" + color + StringUtils.repeat("|", filled) + ChatColor.DARK_GRAY + StringUtils.repeat("|", empty) + ChatColor.WHITE +  "]";
		return filledStr;
	}
	
	
	public static int getRandomLevel(Location loc) {
		int min = 1;
		int max = 1;
		for (Entity e : loc.getChunk().getEntities()) {
			
			// Add player level to sum
			if (e instanceof Player) {
				User user = VEPlugin.getInstance().getUser((Player) e);
				if (min > user.getLevel()) min = user.getLevel();
				if (max < user.getLevel()) max = user.getLevel();
			}
			
		}
		if (min == max) return Math.min(ThreadLocalRandom.current().nextInt(1, min + 5), RPG.MAX_LEVEL);
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	public static boolean isCustomZombie(Entity entity) {
		return entity.isCustomNameVisible() && entity.getCustomName().contains("Level: ");
	}
	
	public static int getLevel(Entity entity) {
		if (isCustomZombie(entity)) {
			String name = entity.getCustomName();
			return Integer.parseInt(name.split(" ")[1]);
		}
		return 0;
	}


}
