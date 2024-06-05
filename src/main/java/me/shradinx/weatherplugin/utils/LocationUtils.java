package me.shradinx.weatherplugin.utils;

import org.bukkit.Location;

import java.util.Random;

public class LocationUtils {
    public static Location getRandomLocation(Location origin, double radius, boolean is3D) {
        Random random = new Random();
        double randomRadius = random.nextDouble() * radius;
        double theta = Math.toRadians(random.nextDouble() * 360);
        double phi = Math.toRadians(random.nextDouble() * 180 - 90);
        
        double x = randomRadius * Math.cos(theta) * Math.sin(phi);
        double y = randomRadius * Math.sin(theta) * Math.cos(phi);
        double z = randomRadius * Math.cos(phi);
        Location loc = origin.clone().add(x, origin.getY(), z);
        if (is3D) {
            loc.add(0, y, 0);
        }
        return loc;
    }
    
}
