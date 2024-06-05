package me.shradinx.weatherplugin.listener;

import me.shradinx.weatherplugin.WeatherPlugin;
import me.shradinx.weatherplugin.timer.WindParticleEffectTimer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

public class PlayerJoinListener implements Listener {
    
    private final WeatherPlugin plugin;
    
    public PlayerJoinListener(WeatherPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase("world")) return;
        BukkitTask task = new WindParticleEffectTimer(player, Particle.ENTITY_EFFECT)
            .runTaskTimer(plugin, 0, 200);
        plugin.getWindTimers().put(player, task.getTaskId());
    }
}
