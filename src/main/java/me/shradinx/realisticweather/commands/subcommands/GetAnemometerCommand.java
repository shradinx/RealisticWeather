package me.shradinx.realisticweather.commands.subcommands;

import me.shradinx.realisticweather.RealisticWeather;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GetAnemometerCommand extends SubCommand {
    public static final NamespacedKey key = new NamespacedKey(RealisticWeather.getPlugin(), "anemometer");
    
    @Override
    public String getName() {
        return "getanemometer";
    }
    
    @Override
    public String getSyntax() {
        return "/realisticweather getanemometer";
    }
    
    @Override
    public String getPermission() {
        return "realisticweather.command.getanemometer";
    }
    
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command is player-only!", NamedTextColor.RED));
            return;
        }
        
        ItemStack tool = new ItemStack(Material.CLOCK);
        tool.lore(List.of(Component.text("Shows the current wind direction.", NamedTextColor.GRAY)));
        ItemMeta meta = tool.getItemMeta();
        meta.displayName(Component.text("Anemometer", NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.INTEGER, 1);
        tool.setItemMeta(meta);
        
        Inventory inventory = player.getInventory();
        int firstEmpty = inventory.firstEmpty();
        if (firstEmpty == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), tool);
        } else {
            inventory.setItem(firstEmpty, tool);
        }
        player.updateInventory();
        player.sendMessage(Component.text("You have been given 1x Anemometer!", NamedTextColor.GREEN));
    }
    
    @Override
    public List<String> getCommandArgs(Player player, String[] args) {
        return List.of();
    }
}
