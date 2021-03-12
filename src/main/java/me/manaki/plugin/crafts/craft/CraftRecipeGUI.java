package me.manaki.plugin.crafts.craft;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.manaki.plugin.crafts.event.ItemCraftEvent;
import me.manaki.plugin.crafts.main.Crafts;
import me.manaki.plugin.crafts.main.ItemStackUtils;
import me.manaki.plugin.crafts.main.MoneyAPI;
import me.manaki.plugin.crafts.main.Utils;

public class CraftRecipeGUI {
	
	private final static List<Integer> FROM_SLOTS = Lists.newArrayList(10, 11, 12, 19, 20, 21, 28, 29, 30);
	private final static int TO_SLOT = 23;
	private final static int BUTTON_SLOT = 25;
	
	private static Map<Player, Long> cooldown = Maps.newHashMap();
	
	public static void open(Player player, String recipeID) {
		Inventory inv = Bukkit.createInventory(new CRGHolder(recipeID), 45, "§2§lCHẾ TÁC");
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		
		// Load icons
		Bukkit.getScheduler().runTaskAsynchronously(Crafts.get(), () -> {
			// Load background
			for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, Utils.getBlackSlot());
			for (int i = 0 ; i < FROM_SLOTS.size() ; i++) inv.setItem(FROM_SLOTS.get(i), null);
			inv.setItem(TO_SLOT, null);
			
			// Load recipe
			CraftRecipe cr = CraftRecipes.get(recipeID);
			for (int i = 0 ; i < cr.getFrom().size() ; i++) {
				ItemStack icon = getFromIcon(cr.getFrom().get(i).clone(), player);
				
				inv.setItem(FROM_SLOTS.get(i), icon);
			}
			inv.setItem(TO_SLOT, getToIcon(cr.getResult().getItem()));
			
			// Load button
			inv.setItem(BUTTON_SLOT, getButton(CraftRecipes.canCraft(player, recipeID), player, cr.getFee(), CraftRecipes.canBypass(player, recipeID)));
		});
	}
	
	public static void eventClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof CRGHolder == false) return;
		e.setCancelled(true);
		
		// Check button
		int slot = e.getSlot();
		if (slot != BUTTON_SLOT) return;
			
		Player player = (Player) e.getWhoClicked();
		
		if (!checkCooldown(player)) return;
		
		// Do
		String id = ((CRGHolder) inv.getHolder()).getRecipeID();
		if (!CraftRecipes.canCraft(player, id)) return;
		
		// Fee
		if (!CraftRecipes.canBypass(player, id)) {
			if (!MoneyAPI.moneyCost(player, CraftRecipes.get(id).getFee())) {
				player.sendMessage("§cBạn không đủ khả năng chi trả để chế tác");
				return;
			}
		}
		
		// Take and add to storage
		CraftRecipes.take(player, id);
		
		// Check permission
		if (CraftRecipes.canBypass(player, id)) {
			CraftStorages.add(player, id, System.currentTimeMillis() + 1000);
		}
		else CraftStorages.add(player, id, System.currentTimeMillis() + CraftRecipes.get(id).getWait() * 1000);
		
		// Call event
		Bukkit.getPluginManager().callEvent(new ItemCraftEvent(player, id));
		
		// Noti
		player.sendMessage("§f§l>> §aGhi §7/khochetac §ađể xem vật phẩm của bạn");
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
		
		// Reopen
		open(player, id);	
	}
	
	public static void eventDrag(InventoryDragEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof CRGHolder == false) return;
		e.setCancelled(true);
	}
	
	private static ItemStack getButton(boolean canCraft, Player player, int fee, boolean canBypass) {
		ItemStack is = canCraft ? new ItemStack(Material.GREEN_CONCRETE) : new ItemStack(Material.RED_CONCRETE);
		String name = canCraft ? "§a§lCó thể chế tác" : "§c§lKhông thể chế tác";
		ItemStackUtils.setDisplayName(is, name);
		
		if (!canCraft) {
			if (CraftStorages.isFull(player)) ItemStackUtils.addLoreLine(is, "§7§oKho đồ chế tác đã đầy!");
			else ItemStackUtils.addLoreLine(is, "§7§oThiếu vật phẩm");
		}
		
		else {
			 if (!canBypass) ItemStackUtils.addLoreLine(is, "§aPhí: §f" + fee + "$");
		}
	
		return is;
	}
	
	private static ItemStack getToIcon(ItemStack is) {
		ItemStackUtils.setDisplayName(is, ItemStackUtils.getName(is) + " §7§o(Sản phẩm)");
		return is;
	}
	
	private static ItemStack getFromIcon(ItemStack is, Player player) {
		ItemStackUtils.setDisplayName(is, ItemStackUtils.getName(is) + "§o (" + CraftRecipes.count(player, is) + "/" + is.getAmount() + ")");
		return is;
	}
	
	private static boolean checkCooldown(Player player) {
		if (cooldown.containsKey(player)) {
			if (cooldown.get(player) > System.currentTimeMillis()) {
				player.sendMessage("§cMỗi lần chế tác cách nhau 2 giây");
				return false;
			}
		}
		cooldown.put(player, System.currentTimeMillis() + 2000);
		return true;
	}
}

class CRGHolder implements InventoryHolder {

	private String recipeID;
	
	public CRGHolder(String recipeID) {
		this.recipeID = recipeID;
	}
	
	public String getRecipeID() {
		return this.recipeID;
	}
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
}