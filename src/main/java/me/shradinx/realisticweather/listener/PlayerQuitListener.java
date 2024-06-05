package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    private final RealisticWeather plugin;
    
    public PlayerQuitListener(RealisticWeather plugin) {
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
