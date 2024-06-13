package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class VehicleListener implements Listener {
    
    private final RealisticWeather plugin;
    
    public VehicleListener(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (!event.hasChangedPosition()) return;
        
        if (!plugin.getConfig().getBoolean("wind-enabled")) return;
        if (!plugin.getConfig().getBoolean("wind-affect-boats")) return;
        
        Player player = event.getPlayer();
        if (!player.isInsideVehicle()) return;
        
        Vehicle vehicle = (Vehicle) player.getVehicle();
        if (vehicle == null) {
            plugin.getLogger().info("Vehicle is null");
            return;
        }
        if (!(vehicle instanceof Boat)) {
            return;
        }
        
        
        
        Vector direction = vehicle.getLocation().getDirection();
        if (direction.isZero()) return;
        Vector windDirection = plugin.getWindDirection();
        if (windDirection.isZero()) return;
        
        double dot = direction.dot(windDirection);
        double angle = direction.angle(windDirection) * 180.0f / Math.PI;
        double multiplier;
        Vector vector;
        
        if (dot > 0) {
            multiplier = Math.clamp((dot / 2), 0.1, 0.3);
            vector = direction.clone().normalize().midpoint(windDirection).multiply(multiplier);
        } else {
            multiplier = Math.clamp((((angle / (180 - angle)) / (180 / angle)) / 20), 0.05, 0.1);
            if (angle > 170) {
                vector = direction.clone().normalize().multiply((multiplier / 2) * -1);
            } else {
                Vector midpoint = direction.clone().normalize().midpoint(windDirection);
                vector = direction.clone().normalize().midpoint(midpoint).multiply(multiplier);
            }
        }
        
        if (((Boat) vehicle).getStatus().equals(Boat.Status.IN_WATER)) {
            vector.setY(0);
        }
        vehicle.setVelocity(vector);
    }
}
