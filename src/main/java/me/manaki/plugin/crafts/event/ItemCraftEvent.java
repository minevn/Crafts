package me.manaki.plugin.crafts.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ItemCraftEvent extends PlayerEvent {
	
	private String id;
	
	public ItemCraftEvent(Player who, String id) {
		super(who);
		this.id = id;
	}

	public String getRecipeID() {
		return this.id;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
