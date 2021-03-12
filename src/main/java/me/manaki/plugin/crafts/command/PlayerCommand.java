package me.manaki.plugin.crafts.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.manaki.plugin.crafts.craft.CraftStorageGUI;

public class PlayerCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("khochetac")) {
			Player player = (Player) sender;
			CraftStorageGUI.open(player);
		}
		
		return false;
	}

}
