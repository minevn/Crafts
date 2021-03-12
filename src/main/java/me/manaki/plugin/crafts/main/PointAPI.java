package me.manaki.plugin.crafts.main;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PointAPI {
	
	public static PlayerPoints getPP() {
		Plugin pl = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
    	return PlayerPoints.class.cast(pl);
	}
	
	@SuppressWarnings("deprecation")
	public static int getPoint(Player player) {
		int points = getPP().getAPI().look(player.getName());
		return points;
	}
	
	public static boolean pointCost(Player player, int points) {
		if (points > getPoint(player)) {
			return false;
		} else {
			getPP().getAPI().take(player.getUniqueId(), points);
			return true;
		}
	}
	
	public static void givePoint(Player player, int point) {
		getPP().getAPI().give(player.getUniqueId(), point);
	}
	
	
}
