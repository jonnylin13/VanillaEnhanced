package com.github.jonnylin13.ve.tools;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;

import com.github.jonnylin13.ve.constants.RPG;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.IBlockData;

public class Toolz {
	
	public static String generateShortId() {
		return RandomStringUtils.randomAlphanumeric(8);
	}
	
	public static String prettyEnum(String enumString) {
		String[] tokens = enumString.split("_");
		if (tokens.length > 0) {
			String result = "";
			for (String s : tokens) {
				if (result == "") {
					result += prettyWord(s);
				} else {
					result += " " + prettyWord(s);
				}
			}
			return result;
		} else {
			return Toolz.prettyWord(enumString);
		}
	}
	
	private static String prettyWord(String word) {
		String first = word.substring(0, 1).toUpperCase();
		return first + word.substring(1).toLowerCase();
	}
	
	public static net.minecraft.server.v1_13_R2.Block getNMSBlock(Block block) {
		World world = block.getWorld();
		IBlockData blockData = ((CraftWorld) world).getHandle()
				.getType(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		net.minecraft.server.v1_13_R2.Block nmsBlock = blockData.getBlock();
		return nmsBlock;
	}
	
	public static int getXpRequired(int level) {
		return (int) Math.round(Math.pow(level * RPG.XP_MODIFIER, 2));
	}

}
