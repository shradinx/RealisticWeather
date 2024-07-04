package me.shradinx.realisticweather.commands;

import me.shradinx.realisticweather.RealisticWeather;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand implements CommandExecutor {
    
    private final RealisticWeather plugin;
    
    public ToggleCommand(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        String name = args[0];
        String state;
        if (name.equalsIgnoreCase("realisticweather")) {
            MainCommand.setDisabled(true);
            state = (MainCommand.isDisabled()) ? "Disabled" : "Enabled";
            String message = state + " /" + name + " command!";
            if (sender instanceof Player player) {
                player.sendMessage(Component.text(message, NamedTextColor.GREEN));
            } else {
                plugin.getLogger().info(message);
            }
            return true;
        } else {
            String message = "Command cannot be disabled or does not exist!";
            if (sender instanceof Player player) {
                player.sendMessage(Component.text(message, NamedTextColor.RED));
            } else {
                plugin.getLogger().info(message);
            }
            return false;
        }
    }
}
