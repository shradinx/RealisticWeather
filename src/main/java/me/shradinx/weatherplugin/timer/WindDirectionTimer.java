package me.shradinx.weatherplugin.timer;

import me.shradinx.weatherplugin.WeatherPlugin;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class WindDirectionTimer extends BukkitRunnable {
    private final World world;
    private final Random random = new Random();
    
    public WindDirectionTimer(World world) {
        this.world = world;
    }
    
    @Override
    public void run() {
        if (world.getTime() < 6000L || world.getTime() >= 6010L) return;
        int x = random.nextInt(-1, 2);
        int z = random.nextInt(-1, 2);
        Vector vector = new Vector(x, 0, z);
        WeatherPlugin.getPlugin().setWindDirection(vector);
    }
}
