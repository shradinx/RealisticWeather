package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;

public class ThrowableListener implements Listener {
    
    private final RealisticWeather plugin;
    
    public ThrowableListener(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.isCancelled()) return;
        setVelocity(event.getEntity());
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        setVelocity(event.getItemDrop());
    }
    
    private void setVelocity(Entity throwable) {
        if (!plugin.getConfig().getBoolean("wind-enabled")) return;
        if (!plugin.getConfig().getBoolean("wind-affect-projectiles")) return;
        
        Vector direction = throwable.getVelocity();
        Vector windDirection = plugin.getWindDirection();
        if (windDirection.isZero()) return;
        
        double dot = direction.dot(windDirection);
        Vector midpoint = direction.getMidpoint(windDirection);
        
        double multiplier = 0.75;
        if (dot <= 0) {
            throwable.setVelocity(direction.midpoint(midpoint).multiply(multiplier));
        } else {
            throwable.setVelocity(midpoint);
        }
    }
}
