package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import me.shradinx.realisticweather.timer.WindParticleEffectTimer;
import org.bukkit.Particle;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class PlayerJoinListener implements Listener {
    
    private final RealisticWeather plugin;
    
    public PlayerJoinListener(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase("world")) return;
        
        Particle particle = switch (player.getLocation().getBlock().getBiome()) {
            case TAIGA, SNOWY_PLAINS, SNOWY_TAIGA, SNOWY_SLOPES,
                SNOWY_BEACH, ICE_SPIKES, FROZEN_OCEAN, FROZEN_PEAKS,
                FROZEN_RIVER, DEEP_FROZEN_OCEAN -> Particle.SNOWFLAKE;
            case DESERT -> Particle.ITEM;
            default -> Particle.ENTITY_EFFECT;
        };
        
        BukkitTask task = new WindParticleEffectTimer(player, particle)
            .runTaskTimer(plugin, 0, 200);
        plugin.getWindTimers().put(player, task.getTaskId());
    }
    
    @EventHandler
    public void onVehicleMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (!event.hasChangedPosition()) return;
        Player player = event.getPlayer();
        if (!player.isInsideVehicle()) return;
        Vehicle vehicle = (Vehicle) player.getVehicle();
        if (vehicle == null) {
            plugin.getLogger().info("Vehicle is null");
            return;
        }
        if (!(vehicle instanceof Boat)) {
            if (vehicle.getVehicle() != null) {
                plugin.getLogger().info(vehicle.getVehicle().getType().name());
            }
            return;
        }
        
        Vector direction = vehicle.getLocation().getDirection();
        if (direction.isZero()) return;
        Vector windDirection = plugin.getWindDirection();
        if (windDirection.isZero()) return;
        
        plugin.getLogger().info("Player Direction: " + direction);
        plugin.getLogger().info("Wind Direction: " + windDirection);
        
        double dot = direction.dot(windDirection);
        double angle = direction.angle(windDirection) * 180.0f / Math.PI;
        Vector midpoint = direction.getMidpoint(windDirection);
        
        double calcAngle = Math.sqrt(angle) / 30;
        double calcMultiplier = Math.pow(0.7, angle);
        plugin.getLogger().info("Angle: " + angle);
        plugin.getLogger().info("Calculated Angle: " + calcAngle);
        if (dot > 0) {
            vehicle.setVelocity(vehicle.getVelocity().add(midpoint.multiply(calcAngle * calcMultiplier).setY(0)));
        } else if (dot <= 0) {
            vehicle.setVelocity(vehicle.getVelocity().add(direction.midpoint(midpoint).multiply(calcAngle).setY(0)));
        }
    }
}
