package me.manaki.plugin.crafts.craft;

import java.util.List;

import com.google.common.collect.Lists;

import me.manaki.plugin.crafts.main.Utils;

public class CraftStorage {

	//         recipe   timemilis
	private List<CraftContent> items;
	
	public CraftStorage(List<CraftContent> items) {
		this.items = items;
	}
	
	public List<CraftContent> getItems() {
		return this.items;
	}
	
	public void add(CraftContent cc) {
		items.add(cc);
	}
	
	public void remove(CraftContent cc) {
		this.items.remove(cc);
	}
	
	public String toString() {
		String s = "";
		for (CraftContent cc : items) {
			s += cc.toString() + ";";
		}
		if (s.length() > 0) s = s.substring(0, s.length() - 1);
		return s;
	}
	
	public static CraftStorage fromString(String s) {
		List<CraftContent> items = Lists.newArrayList();
		Utils.from(s, ";").forEach(ss -> {
			items.add(CraftContent.fromString(ss));
		});
		return new CraftStorage(items);
	}
	
	
}
