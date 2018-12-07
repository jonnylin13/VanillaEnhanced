package com.github.jonnylin13.ve.commands.member;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;

import net.md_5.bungee.api.ChatColor;

public class WoodCommand implements CommandExecutor {
	
	public WoodCommand() {
		VEPlugin.getInstance().getCommand("wood").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				// TODO: Handle
				return true;
			}
			Player player = (Player) sender;
			User user = VEPlugin.getInstance().getUser(player);
			boolean result = user.toggleWoodcutting();
			if (result) {
				player.sendMessage("Woodcutting" + ChatColor.GRAY + " has been toggled on!");
			} else {
				player.sendMessage("Woodcutting" + ChatColor.GRAY + " has been toggled off!");
			}
			
			return true;
		}
		return false;
	}

}
