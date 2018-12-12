package com.github.jonnylin13.ve.commands.mod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

public class GroupsCommand implements TabExecutor {
	
	public GroupsCommand() {
		VEPlugin.getInstance().getCommand("groups").setExecutor(this);
		VEPlugin.getInstance().getCommand("groups").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			// TODO: Implement level check somehow... think it through
			String target = args[0];
			Player p = VEPlugin.getInstance().getServer().getPlayerExact(target);
			if (p == null) {
				// TODO: Get the player info from database
				return true;
			}
			// Player is online
			User user = VEPlugin.getInstance().getUser(p);
			sender.sendMessage(this.displayGroups(user));
			return true;
			
		} else if (args.length == 2) {
			String target = args[0];
			String groupName = args[1];
			Player p = VEPlugin.getInstance().getServer().getPlayerExact(target);
			if (p == null) {
				// TODO: Just send an update?
				return true;
			}
			User user = VEPlugin.getInstance().getUser(p);
			List<String> groupList = Lists.newArrayList(user.getGroups());

			if (groupList.contains(groupName)) {
				groupList.remove(groupName);
				for (String permission : VEPlugin.getInstance().getGroup(groupName).getPermissions()) {
					user.getPermissions().unsetPermission(permission);
					VEPlugin.log.info("<VE> Detaching permission: " + permission + " from " + user.getName());
				}
			} else { 
				groupList.add(groupName); 
				for (String permission : VEPlugin.getInstance().getGroup(groupName).getPermissions()) {
					user.getPermissions().setPermission(permission, true);
					VEPlugin.log.info("<VE> Attaching permission: " + permission + " to " + user.getName());
				}
			}
			String[] groups = groupList.toArray(new String[groupList.size()]);
			user.setGroups(groups);
			user.updateAsync();
			sender.sendMessage(this.displayGroups(user));
			return true;
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> options = new ArrayList<String>();
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

	
	private String displayGroups(User user) {
		String msg = ChatColor.GRAY + "%s: " + ChatColor.WHITE + "[ %s ]";
		String addition = "";
		for (String s : user.getGroups()) {
			if (addition == "") addition += s;
			else addition += ", " + s;
		}
		return String.format(msg, user.getName(), addition);
	}
	
	

}
