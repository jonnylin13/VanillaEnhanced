package com.github.jonnylin13.ve.commands.member;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;
import com.github.jonnylin13.ve.tools.Toolz;

import net.md_5.bungee.api.ChatColor;

public class StatsCommand implements CommandExecutor {

	public StatsCommand() {
		VEPlugin.getInstance().getCommand("stats").setExecutor(this);
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
			player.sendMessage(ChatColor.GRAY + "Level: " + ChatColor.WHITE + String.valueOf(user.getLevel()));
			player.sendMessage(ChatColor.BOLD + "XP: " + ChatColor.DARK_GRAY + String.valueOf(user.getXp())
					+ ChatColor.WHITE + "/" + ChatColor.GRAY + Toolz.getXpRequired(user.getLevel() + 1));
			return true;

		} else {
			// TODO: Handle
			return true;
		}
	}

}
