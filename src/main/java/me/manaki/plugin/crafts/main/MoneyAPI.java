package me.manaki.plugin.crafts.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class MoneyAPI {
	
	public static Economy getEco() {
	    RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
	    if (rsp == null) {
	    	return null;
	    }
	    return rsp.getProvider();
	}
	
	public static double getMoney(Player player) {
		Economy eco = getEco();
		
		return eco.getBalance(player);
	}

	public static boolean moneyCost(Player player, double money) {
		Economy eco = getEco();
		double moneyOfPlayer = eco.getBalance(player);
		if (moneyOfPlayer < money) {
			return false;
		}
		eco.withdrawPlayer(player, money);
		return true;
		
	}
	
	public static void giveMoney(Player player, double money) {
		Economy eco = getEco();
		eco.depositPlayer(player, money);
	}
	
}
