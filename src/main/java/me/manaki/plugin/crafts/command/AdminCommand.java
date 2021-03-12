package me.manaki.plugin.crafts.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.manaki.plugin.crafts.craft.CraftRecipeGUI;
import me.manaki.plugin.crafts.craft.CraftStorageGUI;

public class AdminCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if (!sender.hasPermission("crafts.admin")) {
			sender.sendMessage("§cNo permission you dickhead");
			return false;
		}
		
		try {
			if (args[0].equalsIgnoreCase("gui")) {
				if (args[1].equalsIgnoreCase("craft")) {
					Player player = Bukkit.getPlayer(args[2]);
					String rID = args[3];
					CraftRecipeGUI.open(player, rID);
				}
				else if (args[1].equalsIgnoreCase("storage")) {
					Player player = Bukkit.getPlayer(args[2]);
					CraftStorageGUI.open(player);
				}
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			sendHelp(sender);
		}
		
		
		
		return false;
	}
	
	public void sendHelp(CommandSender sender) {
		sender.sendMessage("§a/crafts gui craft <*player> <*id>");
		sender.sendMessage("§a/crafts gui storage <*player>");
	}

}
