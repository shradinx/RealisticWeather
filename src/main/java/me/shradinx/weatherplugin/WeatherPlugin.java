package me.shradinx.weatherplugin;

import lombok.Getter;
import lombok.Setter;
import me.shradinx.weatherplugin.listener.PlayerJoinListener;
import me.shradinx.weatherplugin.listener.PlayerQuitListener;
import me.shradinx.weatherplugin.timer.WindDirectionTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;

public final class WeatherPlugin extends JavaPlugin {
    
    @Getter
    private static WeatherPlugin plugin;
    
    @Getter
    @Setter
    private Vector windDirection = new Vector(0, 0, 0);
    
    @Getter
    private HashMap<Player, Integer> windTimers = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        
        // Register wind direction timer
        new WindDirectionTimer(plugin.getServer().getWorld("world"))
            .runTaskTimer(this, 5, 10);
        
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        
        getLogger().info("WeatherPlugin Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("WeatherPlugin Disabled!");
    }
}
