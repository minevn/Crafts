package me.manaki.plugin.crafts.craft;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

import me.manaki.plugin.crafts.main.Crafts;
import me.manaki.plugin.crafts.main.ItemStackUtils;
import me.manaki.plugin.crafts.main.PointAPI;
import me.manaki.plugin.crafts.main.Utils;

public class CraftStorageGUI {

	public static void open(Player player) {
		// Get slots
		Map<Integer, CraftContent> slots = Maps.newHashMap();
		CraftStorage cs = CraftStorages.get(player);

		int slot = -1;
		for (CraftContent cc : cs.getItems()) {
			slot++;
			slots.put(slot, cc);
		}

		// Open
		Inventory inv = Bukkit.createInventory(new CSGHolder(slots), 9, "§2§lKHO CHẾ TÁC");
		player.openInventory(inv);

		// Load
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (player.getOpenInventory() == null || player.getOpenInventory().getTopInventory().getHolder() instanceof CSGHolder == false) {
					this.cancel();
					return;
				}
				slots.forEach((i, cc) -> {
					inv.setItem(i, getIcon(cc.getRecipeID(), cc.getTimeExpired()));
				});
			}
		}.runTaskTimerAsynchronously(Crafts.get(), 0, 10);

	}

	public static void eventClick(InventoryClickEvent e) {
		// Check inv
		Inventory inv = e.getInventory();
		if (inv.getHolder() instanceof CSGHolder == false)
			return;
		e.setCancelled(true);

		// Check slot
		Player player = (Player) e.getWhoClicked();
		Map<Integer, CraftContent> slots = ((CSGHolder) inv.getHolder()).getSlots();
		int slot = e.getSlot();
		if (!slots.containsKey(slot))
			return;

		// Check item
		CraftStorage cs = CraftStorages.get(player);
		CraftContent cc = slots.get(slot);

		// Not expired yet
		if (cc.getTimeExpired() > System.currentTimeMillis()) {

			// Waiting bypass
			if (e.getClick() != ClickType.RIGHT) return;
			long timeExpired = cc.getTimeExpired();
			long remain = timeExpired - System.currentTimeMillis();
			int point = getPointRequỉred(remain);
			if (!PointAPI.pointCost(player, point)) {
				player.sendMessage("§cBạn không đủ point để chi trả");
				return;
			}

		}

		// Give
		CraftRecipes.get(cc.getRecipeID()).getResult().giveResult(player);
		cs.remove(cc);
		CraftStorages.save(player, cs);
		player.sendMessage("§aNhận vật phẩm chế tác thành công");
		player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

		// Close and reopen
		open(player);
	}

	public static ItemStack getIcon(String rID, long timeExpired) {
		boolean expired = System.currentTimeMillis() > timeExpired;
		long remain = timeExpired - System.currentTimeMillis();
		String status = expired ? "§a§oCó thể lấy" : "§c§oĐợi " + Utils.formatMinute(remain);

		ItemStack result = CraftRecipes.get(rID).getResult().getItem();
		ItemStackUtils.addFlag(result, ItemFlag.HIDE_ATTRIBUTES);
		ItemStackUtils.addLoreLine(result, "");
		ItemStackUtils.addLoreLine(result, "§2§oTrạng thái: " + status);

		if (!expired) {
			ItemStackUtils.addLoreLine(result, "§a§oClick phải để lấy nhanh với " + getPointRequỉred(remain) + "P");
		}

		return result;
	}

	public static int getPointRequỉred(long remain) {
		return new Long(remain / 10000).intValue();
	}
}

class CSGHolder implements InventoryHolder { 

	private Map<Integer, CraftContent> slots;

	public CSGHolder(Map<Integer, CraftContent> slots) {
		this.slots = slots;
	}

	public Map<Integer, CraftContent> getSlots() {
		return this.slots;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

}
