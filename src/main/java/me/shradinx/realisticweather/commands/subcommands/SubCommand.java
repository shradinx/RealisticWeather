package me.shradinx.realisticweather.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {
    public abstract String getName();
    
    public abstract String getSyntax();
    
    public abstract String getPermission();
    
    public abstract void run(CommandSender sender, String[] args);
    
    public abstract List<String> getCommandArgs(Player player, String[] args);
}
