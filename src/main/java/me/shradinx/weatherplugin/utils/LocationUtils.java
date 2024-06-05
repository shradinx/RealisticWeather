package me.shradinx.weatherplugin.utils;

import org.bukkit.Location;

import java.util.Random;

public class LocationUtils {
    public static Location getRandomLocation(Location origin, int radius) {
       Random random = new Random();
       int randomX = (random.nextBoolean() ? 1 : -1) * random.nextInt(0, radius + 1);
       int randomY = random.nextInt(3, 7);
       int randomZ = (random.nextBoolean() ? 1 : -1) * random.nextInt(0, (radius + 1));
       
       return origin.clone().add(randomX, randomY, randomZ);
    }
    
}
