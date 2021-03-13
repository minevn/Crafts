package me.manaki.plugin.crafts.craft;

import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


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
