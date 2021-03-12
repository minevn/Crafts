package me.manaki.plugin.crafts.craft;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class CraftRecipe {
	
	private int fee;
	private int wait;
	private CraftResult result;
	private List<ItemStack> from;
	private List<Integer> amounts;
	
	public CraftRecipe(int fee, int wait, List<ItemStack> from, CraftResult result, List<Integer> amounts) {
		this.fee = fee;
		this.from = from;
		this.result = result;
		this.wait = wait;
		this.amounts = amounts;
	}
	
	public int getFee() {
		return this.fee;
	}
	
	public int getWait() {
		return this.wait;
	}
	
	public List<ItemStack> getFrom() {
		return this.from;
	}
	
	public CraftResult getResult() {
		return this.result;
	}
	
	public List<Integer> getAmounts() {
		return this.amounts;
	}
	
	public int getAmount(ItemStack is) {
		int amount = 0;
		for (int i = 0 ; i < from.size() ; i++) {
			ItemStack item = from.get(i);
			if (is.isSimilar(item)) {
				amount += amounts.get(i);
			}
		}
		return amount;
	}
	
	
}
