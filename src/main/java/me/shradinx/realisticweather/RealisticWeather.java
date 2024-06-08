package me.shradinx.realisticweather;

import lombok.Getter;
import lombok.Setter;
import me.shradinx.realisticweather.listener.PlayerJoinListener;
import me.shradinx.realisticweather.listener.PlayerQuitListener;
import me.shradinx.realisticweather.listener.ProjectileListener;
import me.shradinx.realisticweather.timer.WindDirectionTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;

public final class RealisticWeather extends JavaPlugin {
    
    @Getter
    private static RealisticWeather plugin;
    
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
        getLogger().info("-- Player Join Listener Registered! -- ");
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getLogger().info("-- Player Quit Listener Registered! -- ");
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(this), this);
        getLogger().info("-- Projectile Listener Registered! -- ");
        
        getLogger().info("RealisticWeather Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("RealisticWeather Disabled!");
    }
}
