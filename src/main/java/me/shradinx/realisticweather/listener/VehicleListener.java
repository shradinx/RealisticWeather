package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VehicleListener implements Listener {
    
    private final RealisticWeather plugin;
    
    public VehicleListener(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void onVehicleMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (!event.hasChangedPosition()) return;
        if (!plugin.getConfig().getBoolean("wind-enabled")) return;
        if (!plugin.getConfig().getBoolean("wind-affect-boats")) return;
        Player player = event.getPlayer();
        if (!player.isInsideVehicle()) return;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
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
        
        double dot = direction.dot(windDirection);
        double angle = direction.angle(windDirection) * 180.0f / Math.PI;
        
        if (dot > 0) {
            if (angle >= 45) {
                vehicle.setVelocity(direction.midpoint(windDirection).normalize().multiply(0.4));
            }
            return;
        }
        
        int divisor = 200;
        int pushDirection = 1;
        double multiplier;
        
        if (dot <= 0) {
            if (mainHand.getType().equals(Material.PAPER)) {
                return;
            }
            pushDirection = -1;
            divisor = Math.round((float) Math.clamp((180 - angle) * 20, 300, 700));
        }
        
        multiplier = Math.clamp(angle / divisor, 0.005, 0.75);
        
        if (multiplier < 0 && dot <= 0) {
            multiplier *= pushDirection;
        }
        
        Vector vector;
        if (multiplier >= 0.45) {
            vector = direction.clone().normalize().multiply((1 - multiplier) / 2);
        } else {
            vector = direction.midpoint(windDirection)
                .multiply(multiplier);
        }
        
        if (((Boat) vehicle).getStatus().equals(Boat.Status.IN_WATER)) {
            vector.setY(0);
        }
        
        vehicle.setVelocity(vector);
    }
}
