package me.manaki.plugin.crafts;

import me.manaki.plugin.crafts.command.AdminCommand;
import me.manaki.plugin.crafts.command.PlayerCommand;
import me.manaki.plugin.crafts.craft.CraftRecipes;
import me.manaki.plugin.crafts.listener.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Crafts extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        this.reloadConfig();

        this.getCommand("crafts").setExecutor(new AdminCommand());
        this.getCommand("khochetac").setExecutor(new PlayerCommand());

        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
    }

    public void reloadConfig() {
        this.saveDefaultConfig();
        this.config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
        CraftRecipes.reload(this.config);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public static Crafts get() {
        return Crafts.getPlugin(Crafts.class);
    }
}
