package me.shradinx.realisticweather.commands;

import lombok.Getter;
import lombok.Setter;
import me.shradinx.realisticweather.RealisticWeather;
import me.shradinx.realisticweather.commands.subcommands.ReloadCommand;
import me.shradinx.realisticweather.commands.subcommands.SubCommand;
import me.shradinx.realisticweather.commands.subcommands.TornadoCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {
    
    @Getter
    private ArrayList<SubCommand> subCommands = new ArrayList<>();
    private final RealisticWeather plugin;
    @Getter
    private final String name;
    @Getter
    @Setter
    private static boolean isDisabled = false;
    
    public MainCommand(RealisticWeather plugin) {
        this.plugin = plugin;
        subCommands.add(new ReloadCommand(plugin));
        subCommands.add(new TornadoCommand(plugin));
        this.name = "realisticweather";
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.getLogger().info("You must be a player to run this command.");
            return false;
        }
        if (isDisabled) {
            player.sendMessage(Component.text("This command is disabled.", NamedTextColor.RED));
            return false;
        }
        if (args.length > 0) {
            for (SubCommand sub : getSubCommands()) {
                if (args[0].equalsIgnoreCase(sub.getName()) && player.hasPermission(sub.getPermission())) {
                    sub.run(sender, args);
                    return true;
                } else if (!player.hasPermission(sub.getPermission())) {
                    player.sendMessage(Component.text("No Permission.", NamedTextColor.RED));
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> commandArgs = new ArrayList<>();
            
            for (SubCommand sub : getSubCommands()) {
                if (sender.hasPermission(sub.getPermission())) {
                    commandArgs.add(sub.getName());
                }
            }
            return commandArgs;
        } else if (args.length >= 2) {
            for (SubCommand sub : getSubCommands()) {
                if (args[0].equalsIgnoreCase(sub.getName())) {
                    return sub.getCommandArgs((Player) sender, args);
                }
            }
        }
        return null;
    }
}
