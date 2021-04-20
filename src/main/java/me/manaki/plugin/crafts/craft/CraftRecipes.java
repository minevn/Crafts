package me.manaki.plugin.crafts.craft;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CraftRecipes {

	private static Map<String, CraftRecipe> recipes = Maps.newHashMap();

	public static void reload(FileConfiguration config) {
		recipes.clear();
		config.getConfigurationSection("craft").getKeys(false).forEach(id -> {
			// Get elements

			List<ItemStack> from = Lists.newArrayList();
			List<Integer> amounts = Lists.newArrayList();
			config.getStringList("craft." + id + ".from").forEach(s -> {
				var split = ";";
				if (s.contains(" ")) split = " ";
				int amount = Integer.valueOf(s.split(split)[1]);
				
				do {
					int ta = amount % 64 == 0 ? 64 : amount % 64;
					amount -= ta;
					ItemStack is = ItemStorage.get(s.split(split)[0]);
					is.setAmount(ta);
					from.add(is);
					amounts.add(ta);
				}
				while (amount >= 64);


			});
			int fee = config.getInt("craft." + id + ".fee");
			int wait = config.getInt("craft." + id + ".wait");
			// Get result
			CraftResult cr = null;
			String rI = config.getString("craft." + id + ".result");
			
			// Check
			String itemID = rI;
			cr = new NSItemResult(itemID);
			
			// Add
			recipes.put(id, new CraftRecipe(fee, wait, from, cr, amounts));
			
			// GUI
			CraftMenuGUI.menus.clear();
			config.getConfigurationSection("gui").getKeys(false).forEach(gid -> {
				int size = config.getInt("gui." + gid + ".size");
				List<CraftMenuIcon> icons = Lists.newArrayList();
				for (String s : config.getStringList("gui." + gid + ".recipes")) {
					var split = ";";
					if (s.contains(" ")) split = " ";
					var cmi = new CraftMenuIcon(s.split(split)[0], Integer.parseInt(s.split(split)[1]));
					icons.add(cmi);
				}
				CraftMenu menu = new CraftMenu(size, icons);
				CraftMenuGUI.menus.put(gid, menu);
			});
		});

	}

	public static CraftRecipe get(String id) {
		return recipes.getOrDefault(id, null);
	}

	public static boolean canCraft(Player player, String id) {
		CraftRecipe cr = get(id);

		// Check storage
		if (CraftStorages.isFull(player))
			return false;

		// Check contains
		List<ItemStack> checked = Lists.newArrayList();
		for (ItemStack is : cr.getFrom()) {
			if (contains(checked, is)) continue;
			checked.add(is);
			if (!contains(player, is, cr.getAmount(is))) return false;
		}
		
		return true;
	}
	
	public static boolean contains(List<ItemStack> list, ItemStack is) {
		for (ItemStack item : list) {
			if (item.isSimilar(is)) return true;
		}
		return false;
	}

	public static int count(Player player, ItemStack is) {
		int count = 0;
		PlayerInventory inv = player.getInventory();

		for (ItemStack i : inv.getContents()) {
			if (i == null)
				continue;
			if (i.isSimilar(is))
				count += i.getAmount();
		}
		return count;
	}
	
	public static boolean contains(Player player, ItemStack is, int amount) {
		int count = count(player, is);
		return count >= amount;
	}

	public static boolean take(Player player, ItemStack is, int amount) {
		if (!contains(player, is, amount)) return false;
		PlayerInventory inv = player.getInventory();

		int count = amount;

		for (int slot = 0; slot < inv.getSize(); slot++) {
			ItemStack i = inv.getItem(slot);
			if (count <= 0)
				break;
			if (i == null)
				continue;
			if (i.isSimilar(is)) {
				if (i.getAmount() > count) {
					i.setAmount(i.getAmount() - count);
					count = 0;
				} else if (i.getAmount() <= count) {
					count -= i.getAmount();
					inv.setItem(slot, null);
				}
			}
		}
		
		return true;
	}

	public static void take(Player player, String recipeID) {
		CraftRecipe cr = get(recipeID);
		cr.getFrom().forEach(is -> take(player, is, is.getAmount()));
	}

	public static void give(Player player, String recipeID) {
		CraftRecipe cr = get(recipeID);
		player.getInventory().addItem(cr.getResult().getItem());
	}
	
	public static boolean canBypass(Player player, String id) {
		return player.hasPermission("crafts.bypass." + id) || player.hasPermission("crafts.bypass");
	}

}
