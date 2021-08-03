package me.manaki.plugin.crafts.craft;

import java.util.List;
import java.util.Map;

import me.manaki.plugin.crafts.Crafts;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.manaki.plugin.crafts.event.ItemCraftEvent;
import me.manaki.plugin.crafts.main.ItemStackUtils;
import me.manaki.plugin.crafts.main.MoneyAPI;
import me.manaki.plugin.crafts.main.Utils;

public class CraftRecipeGUI {
	
	private final static List<Integer> FROM_SLOTS = Lists.newArrayList(10, 11, 12, 19, 20, 21, 28, 29, 30);
	private final static int TO_SLOT = 23;
	private final static int BUTTON_SLOT = 25;
	private final static int KHO_SLOT = 44;
	
	private static Map<Player, Long> cooldown = Maps.newHashMap();

	public static void open(String preGUI, Player player, String recipeID) {
		Inventory inv = Bukkit.createInventory(new CRGHolder(preGUI, recipeID), 45, "§2§lCHẾ TÁC");
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		
		// Load icons
		Bukkit.getScheduler().runTaskAsynchronously(Crafts.get(), () -> {
			// Load background
			for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, Utils.getBlackSlot());
			reload(inv, recipeID, player);
		});
	}

	private static void reload(Inventory inv, String recipeID, Player player) {
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
		inv.setItem(BUTTON_SLOT, getButton(CraftRecipes.canCraft(player, recipeID), player, cr.getFee(), false));
	}
	
	public static void eventClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof CRGHolder == false) return;
		e.setCancelled(true);
		
		// Check button
		Player player = (Player) e.getWhoClicked();
		int slot = e.getSlot();
		var holder = (CRGHolder) inv.getHolder();
		String id = holder.getRecipeID();
		String preGUI = holder.getPreGUI();

		player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);

		// Kho button
		if (slot == KHO_SLOT) {
			holder.setKhoOpen(true);
			CraftStorageGUI.open(player);
			return;
		}

		if (slot != BUTTON_SLOT) return;
		if (!checkCooldown(player)) return;
		
		// Do
		if (!CraftRecipes.canCraft(player, id)) return;
		
		// Fee
		if (!MoneyAPI.moneyCost(player, CraftRecipes.get(id).getFee())) {
			player.sendMessage("§cBạn không đủ khả năng chi trả để chế tác");
			return;
		}
		
		// Take and add to storage
		CraftRecipes.take(player, id);
		
		// Check permission
		if (CraftRecipes.canBypass(player, id)) {
			CraftStorages.add(player, id, System.currentTimeMillis() + 1000);
		}
		else CraftStorages.add(player, id, System.currentTimeMillis() + CraftRecipes.get(id).getWait() * 1000L);

		// Button kho
		inv.setItem(KHO_SLOT, getKhoButton());

		// Call event
		Bukkit.getPluginManager().callEvent(new ItemCraftEvent(player, id));
		
		// Noti
		player.sendMessage("§f§l>> §aGhi §f§l/khochetac §ađể xem vật phẩm của bạn");
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
		
		// Reopen
		reload(inv, id, player);
	}
	
	public static void eventDrag(InventoryDragEvent e) {
		Inventory inv = e.getInventory();
		if (!(inv.getHolder() instanceof CRGHolder)) return;
		e.setCancelled(true);
	}

	public static void eventClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (!(inv.getHolder() instanceof CRGHolder)) return;
		var holder = (CRGHolder) inv.getHolder();
		if (holder.isKhoOpen()) return;

		String preGUI = holder.getPreGUI();
		if (preGUI == null) return;
		Bukkit.getScheduler().runTask(Crafts.get(), () -> {
			CraftMenuGUI.open(preGUI, (Player) e.getPlayer());
		});
	}

	private static ItemStack getButton(boolean canCraft, Player player, int fee, boolean canBypass) {
		ItemStack is = canCraft ? new ItemStack(Material.GREEN_CONCRETE) : new ItemStack(Material.RED_CONCRETE);
		String name = canCraft ? "§a§lClick để chế tác" : "§c§lKhông thể chế tác";
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
		ItemStackUtils.setDisplayName(is, ItemStackUtils.getName(is) + " §7§l§o(Sản phẩm)");
		return is;
	}
	
	private static ItemStack getFromIcon(ItemStack is, Player player) {
		int total = is.getAmount();
		int has = CraftRecipes.count(player, is);
		int sub = total - has;

		ItemStackUtils.addLoreLine(is, "");
		ItemStackUtils.addLoreLine(is, "§aSố lượng: §f§l" + CraftRecipes.count(player, is) + "§f/§f§l" + is.getAmount());
		if (sub > 0) {
			ItemStackUtils.addLoreLine(is, "§cThiếu §f§l" + sub + "§c để chế tác");
		}

		return is;
	}

	private static ItemStack getKhoButton() {
		var is = new ItemStack(Material.CHEST);
		ItemStackUtils.setDisplayName(is, "§6§lMở kho chế tác");
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
	private String preGUI;

	private boolean isKhoOpen;

	public CRGHolder(String preGUI,String recipeID) {
		this.preGUI = preGUI;
		this.recipeID = recipeID;
	}

	public String getPreGUI() {
		return preGUI;
	}

	public boolean isKhoOpen() {
		return isKhoOpen;
	}

	public void setKhoOpen(boolean khoOpen) {
		isKhoOpen = khoOpen;
	}

	public String getRecipeID() {
		return this.recipeID;
	}
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
}