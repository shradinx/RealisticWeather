package me.shradinx.realisticweather.timer;

import com.destroystokyo.paper.ParticleBuilder;
import me.shradinx.realisticweather.RealisticWeather;
import me.shradinx.realisticweather.utils.LocationUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
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
        Vector direction = RealisticWeather.getPlugin().getWindDirection().clone();
        if (direction.isZero()) return;
        direction = direction.clone().multiply(0.25);
        Location origin = player.getLocation().clone();
        for (int i = 0; i < 10; i++) {
            Location randomLoc = LocationUtils.getRandomLocation(origin, 15);
            Vector finalDirection = direction;
            new BukkitRunnable() {
                double counter = 0;
                @Override
                public void run() {
                    if (counter > 200) {
                        this.cancel();
                        return;
                    }
                    counter++;
                    Location newLoc = randomLoc.add(finalDirection);
                    Block block = newLoc.getBlock();
                    if (block.isCollidable()) {
                        this.cancel();
                        return;
                    }
                    new ParticleBuilder(particle)
                        .location(newLoc)
                        .count(10)
                        .receivers(player)
                        .data(Color.fromRGB(245, 245, 245))
                        .extra(0)
                        .spawn();
                }
            }.runTaskTimer(RealisticWeather.getPlugin(), 0, 5);
        }
    }
}
