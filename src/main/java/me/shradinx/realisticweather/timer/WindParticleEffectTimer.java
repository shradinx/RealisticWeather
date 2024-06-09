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
    private final Particle particle;
    private Color color = null;
    private ItemStack stack = null;
    
    public WindParticleEffectTimer(Player player, Particle particle) {
        this.player = player;
        this.particle = particle;
        switch (particle) {
            case ENTITY_EFFECT -> color = Color.fromRGB(245, 245, 245);
            case ITEM -> stack = new ItemStack(Material.SAND);
        }
    }
    
    @Override
    public void run() {
        Vector direction = RealisticWeather.getPlugin().getWindDirection().clone();
        if (direction.isZero()) return;
        direction = direction.clone().multiply(0.25);
        Location origin = player.getLocation().clone();
        
        for (int i = 0; i < 10; i++) {
            Location randomLoc = LocationUtils.getRandomLocation(origin, 30);
            Vector finalDirection = direction;
            
            new BukkitRunnable() {
                double counter = 0;
                @Override
                public void run() {
                    if (counter > 400) {
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
                    
                    ParticleBuilder builder = new ParticleBuilder(particle)
                        .location(newLoc)
                        .count(10)
                        .receivers(player)
                        .extra(0);
                    
                    if (particle.equals(Particle.ENTITY_EFFECT)) {
                        builder.data(color);
                    } else if (particle.equals(Particle.ITEM)) {
                        builder.data(stack);
                    }
                    
                    builder.spawn();
                }
            }.runTaskTimer(RealisticWeather.getPlugin(), 0, 2);
        }
    }
}
