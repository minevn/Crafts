package me.manaki.plugin.crafts.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import me.manaki.plugin.crafts.craft.CraftMenuGUI;
import me.manaki.plugin.crafts.craft.CraftRecipeGUI;
import me.manaki.plugin.crafts.craft.CraftStorageGUI;

public class GUIListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		CraftRecipeGUI.eventClick(e);
		CraftStorageGUI.eventClick(e);
		CraftMenuGUI.eventClick(e);
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		CraftRecipeGUI.eventDrag(e);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		CraftRecipeGUI.eventClose(e);
	}
	
}
