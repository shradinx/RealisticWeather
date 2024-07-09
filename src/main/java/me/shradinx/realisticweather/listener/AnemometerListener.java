package me.shradinx.realisticweather.listener;

import me.shradinx.realisticweather.RealisticWeather;
import me.shradinx.realisticweather.commands.subcommands.GetAnemometerCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class AnemometerListener implements Listener {
    private final RealisticWeather plugin;
    
    public AnemometerListener(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onItemHold(PlayerItemHeldEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack newItem = inventory.getItem(event.getNewSlot());
        ItemStack oldItem = inventory.getItem(event.getPreviousSlot());
        if (checkForAnemometer(newItem)) {
            AnemometerFunctionality function = new AnemometerFunctionality(player);
            plugin.getAnemometers().put(player, function);
            function.runTaskTimer(plugin, 0, 20);
        } else if (checkForAnemometer(oldItem)) {
            removeAnemometer(player);
        }
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (checkForAnemometer(item)) {
            removeAnemometer(player);
        }
    }
    
    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        if (checkForAnemometer(item)) {
            removeAnemometer(player);
        }
    }
    
    private void removeAnemometer(Player player) {
        if (!plugin.getAnemometers().containsKey(player)) return;
        AnemometerFunctionality function = plugin.getAnemometers().get(player);
        function.cancel();
        plugin.getAnemometers().remove(player);
    }
    
    private boolean checkForAnemometer(ItemStack item) {
        if (item == null) return false;
        if (!item.getType().equals(Material.CLOCK)) return false;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(GetAnemometerCommand.key);
    }
    
    public class AnemometerFunctionality extends BukkitRunnable {
        final Player player;
        
        public AnemometerFunctionality(Player player) {
            this.player = player;
        }
        
        @Override
        public void run() {
            Vector direction = plugin.getWindDirection();
            String symbol = getSymbol(direction);
            player.sendActionBar(Component.text("--- ", NamedTextColor.GRAY)
                .append(Component.text(String.format("Direction: %s", symbol), NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD))
                .append(Component.text(" ---", NamedTextColor.GRAY)));
        }
        
        private static @NotNull String getSymbol(Vector direction) {
            String directionString = String.format("%1$s %2$s", direction.getX(), direction.getZ());
            return switch (directionString) {
                case "0.0 -1.0" -> "\u2191N";
                case "0.0 1.0" -> "\u2193S";
                case "-1.0 0.0" -> "\u2190W";
                case "1.0 0.0" -> "\u2192E";
                case "1.0 1.0" -> "\u2198SE";
                case "-1.0 1.0" -> "\u2199SW";
                case "1.0 -1.0" -> "\u2197NE";
                case "-1.0 -1.0" -> "\u2196NW";
                default -> "No wind!";
            };
        }
    }
}
