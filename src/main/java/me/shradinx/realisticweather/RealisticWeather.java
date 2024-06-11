package me.shradinx.realisticweather;

import lombok.Getter;
import lombok.Setter;
import me.shradinx.realisticweather.listener.PlayerJoinListener;
import me.shradinx.realisticweather.listener.PlayerQuitListener;
import me.shradinx.realisticweather.listener.ProjectileListener;
import me.shradinx.realisticweather.listener.VehicleListener;
import me.shradinx.realisticweather.timer.WindDirectionTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Objects;

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
        // Save default config
        saveDefaultConfig();
        
        // Init plugin instance variable
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
        Bukkit.getPluginManager().registerEvents(new VehicleListener(this), this);
        getLogger().info("-- Vehicle Listener Registered! -- ");
        
        // Register commands
        Objects.requireNonNull(getCommand("weatherreload")).setExecutor(new ReloadCommand(this));
        
        getLogger().info("RealisticWeather Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RealisticWeather Disabled!");
    }
}
