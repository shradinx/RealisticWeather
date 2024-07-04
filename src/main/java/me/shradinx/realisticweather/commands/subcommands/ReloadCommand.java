package me.shradinx.realisticweather.commands.subcommands;

import me.shradinx.realisticweather.RealisticWeather;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {
    
    private final RealisticWeather plugin;
    
    public ReloadCommand(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "reload";
    }
    
    @Override
    public String getSyntax() {
        return "/realisticweather reload";
    }
    
    @Override
    public String getPermission() {
        return "realisticweather.admin.reload";
    }
    
    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 1) {
            plugin.reloadConfig();
            if (sender instanceof Player player) {
                player.sendMessage(Component.text("RealisticWeather Config Reloaded!", NamedTextColor.GREEN));
            }
            plugin.getLogger().info("Config.yml Reloaded!");
        } else {
            sender.sendMessage(Component.text(getSyntax()));
        }
    }
    
    @Override
    public List<String> getCommandArgs(Player player, String[] args) {
        return null;
    }
}
