package me.shradinx.weatherplugin.listener;

import me.shradinx.weatherplugin.WeatherPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    private final WeatherPlugin plugin;
    
    public PlayerQuitListener(WeatherPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getWindTimers().containsKey(player)) {
            int taskID = plugin.getWindTimers().get(player);
            plugin.getServer().getScheduler().cancelTask(taskID);
            plugin.getWindTimers().remove(player);
        }
    }
}
