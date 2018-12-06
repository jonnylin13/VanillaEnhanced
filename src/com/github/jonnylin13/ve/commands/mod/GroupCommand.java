package com.github.jonnylin13.ve.commands.mod;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.jonnylin13.ve.VEPlugin;
import com.github.jonnylin13.ve.objects.User;
import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

public class GroupCommand implements CommandExecutor {
	
	public GroupCommand() {
		VEPlugin.getInstance().getCommand("group").setExecutor(this);
		VEPlugin.getInstance().getCommand("group").setTabCompleter(new GroupTabCompleter());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!label.equalsIgnoreCase("group")) return true;
		if (!sender.hasPermission("ve.mod") && !sender.isOp()) {
			// TODO: Handle permission message in CommandResponse
			return true;
		}
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
			if (!VEPlugin.getInstance().getGroups().containsKey(groupName)) {
				// TODO: Handle
				return true;
			}
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
