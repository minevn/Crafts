package me.manaki.plugin.crafts.craft;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;

public class CraftStorages {
	
	private static final String KEY = "craft";
	private static final String HOOK = "crafts";
	
	public static final int MAX_SLOT = 9;
	
	public static CraftStorage get(Player player) {
		PlayerData pd = PlayerDataAPI.get(player, HOOK);
		if (pd.hasData(KEY)) return CraftStorage.fromString(pd.getValue(KEY));
		return new CraftStorage(Lists.newArrayList());
	}
	
	public static void save(Player player, CraftStorage cs) {
		PlayerData pd = PlayerDataAPI.get(player, HOOK);
		pd.set(KEY, cs.toString());
		pd.save();
	}
	
	public static boolean isFull(Player player) {
		CraftStorage cs = get(player);
		return cs.getItems().size() >= 9;
	}
	
	public static void add(Player player, String rID, long timeExpired) {
		CraftStorage cs = get(player);
		cs.add(new CraftContent(rID, timeExpired));
		save(player, cs);
	}
	
}
