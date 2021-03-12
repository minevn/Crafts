package me.manaki.plugin.crafts.main;

import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

public class Utils {
	
	public static String formatMinute(long miliTime) {
		return (miliTime / 60000) + "m " + ((miliTime % 60000) / 1000) + "s";
	}
	
	@SuppressWarnings("deprecation")
	public static short getColor(DyeColor color) {
		return color.getWoolData();
	}
	
	public static ItemStack getBlackSlot() {
		ItemStack other = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		other.setDurability((short) 15);
		ItemMeta meta = other.getItemMeta();
		meta.setDisplayName(" ");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		other.setItemMeta(meta);
		return other;
	}
	
	public static List<String> from(String s, String split) {
		if (s.equals(""))
			return Lists.newArrayList();
		return Lists.newArrayList(s.split(split));
	}

	
}
