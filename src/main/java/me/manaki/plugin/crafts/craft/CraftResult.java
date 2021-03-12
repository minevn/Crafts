package me.manaki.plugin.crafts.craft;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CraftResult {
	
	public abstract ItemStack getIcon();
	public abstract void giveResult(Player player);
	
	public ItemStack getItem() {
		return getIcon();
	}
	
	
}
