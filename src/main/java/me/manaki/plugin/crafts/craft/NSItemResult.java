package me.manaki.plugin.crafts.craft;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mk.plugin.niceshops.storage.ItemStorage;

public class NSItemResult extends CraftResult {

	private String itemID;
	
	public NSItemResult(String itemID) {
		this.itemID = itemID;
	}
	
	public String getItemID() {
		return this.itemID;
	}
	
	@Override
	public ItemStack getIcon() {
		return ItemStorage.get(this.itemID);
	}

	@Override
	public void giveResult(Player player) {
		player.getInventory().addItem(ItemStorage.get(this.getItemID()));
	}

}
