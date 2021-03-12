package me.manaki.plugin.crafts.craft;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.manaki.plugin.crafts.main.Crafts;
import me.manaki.plugin.crafts.main.ItemStackUtils;
import me.manaki.plugin.crafts.main.Utils;

public class CraftMenuGUI {
	
	
	public static Map<String, CraftMenu> menus = Maps.newHashMap();
	
	public static void open(String id, Player player) {
		CraftMenu menu = menus.get(id);
		Inventory inv = Bukkit.createInventory(new CraftMenuHolder(id), menu.size, "§2§lCÔNG THỨC CHẾ TÁC");
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		
		Bukkit.getScheduler().runTaskAsynchronously(Crafts.get(), () -> {
			for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, Utils.getBlackSlot());
			menu.icons.forEach(icon -> {
				inv.setItem(icon.getSlot(), getIcon(icon.getRecipeID()));
			});
		});
	}
	
	public static void eventClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof CraftMenuHolder == false) return;
		e.setCancelled(true);
		
		String id = ((CraftMenuHolder) e.getInventory().getHolder()).id;
		CraftMenu menu = menus.get(id);
		
		// Check slot
		int slot = e.getSlot();
		Player player = (Player) e.getWhoClicked();
		for (CraftMenuIcon icon : menu.icons) {
			if (icon.getSlot() == slot) {
				String rid = icon.getRecipeID();
				CraftRecipeGUI.open(player, rid);
			}
		}
	}
	
	private static ItemStack getIcon(String id) {
		ItemStack result = CraftRecipes.get(id).getResult().getIcon();
		ItemStackUtils.setLore(result, Lists.newArrayList());
		ItemStackUtils.addLoreLine(result, "§a§oClick để xem công thức");
		ItemStackUtils.addFlag(result, ItemFlag.HIDE_ATTRIBUTES);
		ItemStackUtils.addFlag(result, ItemFlag.HIDE_ENCHANTS);
		ItemStackUtils.addFlag(result, ItemFlag.HIDE_UNBREAKABLE);
		return result;
	}
	
}

class CraftMenu {
	
	public int size;
	public List<CraftMenuIcon> icons;
	
	public CraftMenu(int size, List<CraftMenuIcon> icons) {
		this.size = size;
		this.icons = icons;
	}
	
}

class CraftMenuHolder implements InventoryHolder {

	public String id;
	
	public CraftMenuHolder(String id) {
		this.id = id;
	}
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
}

class CraftMenuIcon {
	
	private String id;
	private int slot;
	
	public CraftMenuIcon(String id, int slot) {
		this.id = id;
		this.slot = slot;
	}
	
	public String getRecipeID() {
		return this.id;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
}
