package me.shradinx.realisticweather;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    
    private final RealisticWeather plugin;
    
    public ReloadCommand(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.reloadConfig();
        if (sender instanceof Player player) {
            player.sendMessage(Component.text("RealisticWeather Config Reloaded!", NamedTextColor.GREEN));
        }
        plugin.getLogger().info("Config.yml Reloaded!");
        return true;
    }
}
