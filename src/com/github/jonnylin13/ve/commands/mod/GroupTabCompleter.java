package com.github.jonnylin13.ve.commands.mod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;
import com.google.common.collect.Lists;

public class GroupTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> options = new ArrayList<String>();
		if (!label.equalsIgnoreCase("group")) return options;
		if (!sender.hasPermission("ve.mod") && !sender.isOp()) {
			// TODO: Handle permission message in CommandResponse
			return options;
		}
		if (args.length == 1) {
			// TODO: Implement level check somehow... think it through
			String target = args[0];
			Bukkit.getOnlinePlayers().forEach(p -> {
				options.add(p.getName());
			});
			List<String> suggestions = new ArrayList<String>();
			StringUtil.copyPartialMatches(target, options, suggestions);
			return options;
			
		} else if (args.length == 2) {
			String target = args[0];
			String groupName = args[1];
			Player p = VEPlugin.getInstance().getServer().getPlayerExact(target);
			if (p == null) {
				// TODO: Handle
				return options;
			}
			User user = VEPlugin.getInstance().getUser(p);
			List<String> groupList = Lists.newArrayList(user.getGroups());
			List<String> suggestions = new ArrayList<String>();
			StringUtil.copyPartialMatches(groupName, groupList, suggestions);
			
			return suggestions;
		}
		return options;
	}

}
