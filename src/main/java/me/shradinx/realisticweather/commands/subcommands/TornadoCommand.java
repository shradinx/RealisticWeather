package me.shradinx.realisticweather.commands.subcommands;

import com.destroystokyo.paper.ParticleBuilder;
import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class TornadoCommand extends SubCommand {
    
    private final RealisticWeather plugin;
    
    public TornadoCommand(RealisticWeather plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "tornado";
    }
    
    @Override
    public String getSyntax() {
        return "/realisticweather tornado";
    }
    
    @Override
    public String getPermission() {
        return "realisticweather.admin.tornado";
    }
    
    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.getLogger().info("You must be a player to run this command.");
            return;
        }
        Location loc = player.getLocation().clone();
        
        /*
        World world = player.getWorld();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (!mainHand.getType().isBlock()) return;
        Block block = loc.getBlock();
        block.setType(mainHand.getType());
        BlockState state = block.getState();

        final double[] angle = {15};
        double theta = (angle[0] * Math.PI) / 180f;
          */
        new BukkitRunnable() {
            
            double counter = 0;
            
            @Override
            public void run() {
                if (counter > 20) {
                    this.cancel();
                    return;
                }
                for (int count = 0; count < 50; count++) {
                    double direction;
                    if (count % 2 == 0) {
                        direction = 1;
                    } else {
                        direction = -1;
                    }
                    
                    new BukkitRunnable() {
                        double time = 0;
                        double radius = 3;
                        double radIncrementor = 0.01;
                        final double heightMultiplier = 2.5;
                        double offset = 0.25;
                        double angle = 20;
                        final double angleIncrementor = 10;
                        public void run() {
                            time += (Math.PI / 20);
                            double x = (radius * (Math.cos(time)) * direction);
                            double y = time * heightMultiplier + 0.01;
                            double z = (radius * (Math.sin(time) * direction));
                            Vector vector = new Vector(x, y, z);
                            vector.rotateAroundY(angle);
                            loc.add(vector);
                            
                            new ParticleBuilder(Particle.ENTITY_EFFECT)
                                .location(loc)
                                .count(Math.round((float) offset * 100))
                                .receivers(200, true)
                                .data(Color.fromRGB(240, 242, 240))
                                .offset(offset, offset, offset)
                                .spawn();
                            
                            loc.subtract(vector);
                            
                            radius += radIncrementor;
                            radIncrementor += 0.01;
                            offset += 0.0025;
                            angle += angleIncrementor;
                            
                            if (time > Math.PI * 40 || loc.getY() >= 250) {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 0);
                }
                counter++;
            }
        }.runTaskTimer(plugin, 10, 10);
        
        // block.setType(Material.AIR);
    }
    
    @Override
    public List<String> getCommandArgs(Player player, String[] args) {
        return null;
    }
}
