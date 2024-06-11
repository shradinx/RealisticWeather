package me.shradinx.realisticweather.timer;

import me.shradinx.realisticweather.RealisticWeather;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class WindDirectionTimer extends BukkitRunnable {
    private final World world;
    private final Random random = new Random();
    private final RealisticWeather plugin = RealisticWeather.getPlugin();
    
    public WindDirectionTimer(World world) {
        this.world = world;
    }
    
    @Override
    public void run() {
        if (world.getTime() < 6000L || world.getTime() >= 6010L) return;
        int x = random.nextInt(-1, 2);
        int z = random.nextInt(-1, 2);
        if (!plugin.getConfig().getBoolean("wind-enabled")) {
            this.cancel();
            return;
        }
        Vector vector = new Vector(x, 0, z);
        RealisticWeather.getPlugin().setWindDirection(vector);
    }
}
