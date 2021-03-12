package me.manaki.plugin.crafts.main;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtils {
	
	public static boolean isNull(ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}
    
    public static boolean hasLore(ItemStack item) {
    	if (item == null) return false;
    	if (!item.hasItemMeta()) return false;
    	return item.getItemMeta().hasLore();
    }
    
    public static List<String> getLore(ItemStack item) {
    	if (!hasLore(item)) return new ArrayList<String>();
    	return item.getItemMeta().getLore();
    }
    
    public static void setLore(ItemStack item, List<String> lore) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setLore(lore);
    	item.setItemMeta(meta);
    }
    
    public static void addLoreLine(ItemStack item, String line) {
    	List<String> lore = getLore(item);
    	lore.add(line);
    	setLore(item, lore);
    }
    
    public static void addLore(ItemStack item, List<String> lore) {
    	for (int i = 0 ; i < lore.size() ; i++) {
    		addLoreLine(item, lore.get(i));
    	}
    }
    
    public static void setDisplayName(ItemStack item, String name) {
    	if (item == null || item.getType() == Material.AIR) return;
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(name);
    	item.setItemMeta(meta);
    }
    
    public static String getName(ItemStack item) {
    	if (item.getItemMeta().hasDisplayName()) {
    		return item.getItemMeta().getDisplayName();
    	}
    	return "§f" + item.getType().name().replace("_", " ");
    }
    
    public static void addFlag(ItemStack item, ItemFlag flag) {
    	ItemMeta meta = item.getItemMeta();
    	meta.addItemFlags(flag);
    	item.setItemMeta(meta);
    }
    
    public static void setUnbreakable(ItemStack item, boolean bool) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setUnbreakable(bool);
    	item.setItemMeta(meta);
    }
    
    public static void addEnchant(ItemStack item, Enchantment enchant, int lv) {
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(enchant, lv, false);
    	item.setItemMeta(meta);
    }
    
    public static void addEnchantEffect(ItemStack item) {
		ItemStackUtils.addEnchant(item, Enchantment.DURABILITY, 1);
		ItemStackUtils.addFlag(item, ItemFlag.HIDE_ENCHANTS);
    }
    
    public static ItemStack subtractItem(ItemStack item, int amount) {
    	if (item == null) return null;
    	if (item.getAmount() <= amount) return null;
    	item.setAmount(item.getAmount() - amount);
    	return item;
    }
    
}
