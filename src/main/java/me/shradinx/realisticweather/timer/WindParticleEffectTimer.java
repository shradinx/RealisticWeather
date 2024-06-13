package me.shradinx.realisticweather.timer;

import com.destroystokyo.paper.ParticleBuilder;
import me.shradinx.realisticweather.RealisticWeather;
import me.shradinx.realisticweather.utils.LocationUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WindParticleEffectTimer extends BukkitRunnable {
    
    private final Player player;
    private Color color = null;
    private Particle.DustOptions options = null;
    
    private final RealisticWeather plugin = RealisticWeather.getPlugin();
    
    public WindParticleEffectTimer(Player player) {
        this.player = player;
    }
    
    @Override
    public void run() {
        Vector direction = RealisticWeather.getPlugin().getWindDirection().clone();
        if (direction.isZero()) return;
        direction = direction.clone().multiply(0.25);
        Location origin = player.getLocation().clone();
        
        Particle particle = switch (player.getWorld().getBiome(player.getLocation())) {
            case TAIGA, SNOWY_PLAINS, SNOWY_TAIGA, SNOWY_SLOPES,
                SNOWY_BEACH, ICE_SPIKES, FROZEN_OCEAN, FROZEN_PEAKS,
                FROZEN_RIVER, DEEP_FROZEN_OCEAN -> Particle.SNOWFLAKE;
            case DESERT, BADLANDS, ERODED_BADLANDS, WOODED_BADLANDS -> Particle.DUST;
            default -> Particle.ENTITY_EFFECT;
        };
        
        switch (particle) {
            case ENTITY_EFFECT -> color = Color.fromRGB(245, 245, 245);
            case DUST -> options = new Particle.DustOptions(Color.fromRGB(237, 237, 164), 1.5f);
        }
        for (int i = 0; i < 15; i++) {
            Location randomLoc = LocationUtils.getRandomLocation(origin, 50);
            Vector finalDirection = direction;
            
            new BukkitRunnable() {
                double counter = 0;
                double deltaOffset = 0.4;
                @Override
                public void run() {
                    if (counter > 40) {
                        this.cancel();
                        return;
                    }
                    counter++;
                    
                    if (!plugin.getConfig().getBoolean("wind-enabled")) {
                        this.cancel();
                        return;
                    }
                    
                    Location newLoc = randomLoc.add(finalDirection);
                    Block block = newLoc.getBlock();
                    
                    if (block.isCollidable()) {
                        this.cancel();
                        return;
                    }
                    
                    ParticleBuilder builder = new ParticleBuilder(particle)
                        .location(newLoc)
                        .count(10)
                        .offset(deltaOffset, deltaOffset, deltaOffset)
                        .receivers(player)
                        .extra(0);
                    
                    if (particle.equals(Particle.ENTITY_EFFECT)) {
                        builder.data(color);
                    } else if (particle.equals(Particle.DUST)) {
                        builder.data(options);
                    }
                    
                    builder.spawn();
                    deltaOffset -= 0.01;
                }
            }.runTaskTimer(RealisticWeather.getPlugin(), 10, 1);
        }
    }
}
