package me.shradinx.weatherplugin.timer;

import com.destroystokyo.paper.ParticleBuilder;
import me.shradinx.weatherplugin.WeatherPlugin;
import me.shradinx.weatherplugin.utils.LocationUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WindParticleEffectTimer extends BukkitRunnable {
    
    private final Player player;
    private final Particle particle;
    
    public WindParticleEffectTimer(Player player, Particle particle) {
        this.player = player;
        this.particle = particle;
    }
    
    @Override
    public void run() {
        Location origin = player.getLocation();
        for (int i = 0; i < 5; i++) {
            Location randomLoc = LocationUtils.getRandomLocation(origin, 5, true);
            Vector direction = WeatherPlugin.getPlugin().getWindDirection().clone().multiply(0.25);
            for (double k = 0; k <= 5; k += 0.25) {
                Location newLoc = randomLoc.add(direction);
                new ParticleBuilder(particle)
                    .location(newLoc)
                    .count(25)
                    .receivers(player)
                    .data(Color.fromRGB(245, 245, 245))
                    .extra(0)
                    .spawn();
            }
        }
    }
}
