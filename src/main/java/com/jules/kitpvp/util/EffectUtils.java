package com.jules.kitpvp.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EffectUtils {

    public static void createSphere(Player p, Location loc, int radius, int duration) {
    }

    public static void createHelix(Location loc, int radius, int duration) {
    }

    public static void createCircle(Location loc, int duration, boolean b) {
        for (double i = 0; i < 360; i += 5) {
            double x = Math.cos(i) * 1;
            double z = Math.sin(i) * 1;
            loc.getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY, loc.add(x, 0, z), 0, 0, 0, 0, 1);
            loc.subtract(x, 0, z);
        }
    }

    public static void playFirework(org.bukkit.World world, org.bukkit.Location location, org.bukkit.FireworkEffect fireworkEffect) {
        org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) world.spawnEntity(location, org.bukkit.entity.EntityType.FIREWORK);
        org.bukkit.inventory.meta.FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(fireworkEffect);
        fwm.setPower(1);
        fw.setFireworkMeta(fwm);
    }
}
