package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ProjectileListener implements Listener {
    
    private final RealisticWeather plugin;
    
    public ProjectileListener(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.isCancelled()) return;
        if (!plugin.getConfig().getBoolean("wind-enabled")) return;
        if (!plugin.getConfig().getBoolean("wind-affect-projectiles")) return;
        Projectile projectile = event.getEntity();
        
        Vector direction = projectile.getVelocity();
        Vector windDirection = plugin.getWindDirection();
        if (windDirection.isZero()) return;
        
        double dot = direction.dot(windDirection);
        Vector midpoint = direction.getMidpoint(windDirection);
        
        /*
        plugin.getLogger().info("Wind Direction: " + windDirection);
        plugin.getLogger().info("Dot Product: " + dot);
         */
        
        if (dot <= 0) {
            projectile.setVelocity(direction.midpoint(midpoint).multiply(0.75));
        } else {
            projectile.setVelocity(midpoint);
        }
    }
}
