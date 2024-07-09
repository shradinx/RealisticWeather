package me.shradinx.realisticweather.commands.subcommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ResetGravityCommand extends SubCommand {
    @Override
    public String getName() {
        return "resetgravity";
    }
    
    @Override
    public String getSyntax() {
        return "/rw resetgravity";
    }
    
    @Override
    public String getPermission() {
        return "realisticweather.admin.resetgravity";
    }
    
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command is player-only!", NamedTextColor.RED));
            return;
        }
        if (args.length > 1) {
            player.sendMessage(Component.text(getSyntax(), NamedTextColor.RED));
            return;
        }
        player.setGravity(true);
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_GRAVITY);
        if (attr == null) {
            player.sendMessage(Component.text("Player is missing gravity attribute!", NamedTextColor.RED));
            return;
        }
        attr.setBaseValue(attr.getDefaultValue());
    }
    
    @Override
    public List<String> getCommandArgs(Player player, String[] args) {
        return List.of();
    }
}
