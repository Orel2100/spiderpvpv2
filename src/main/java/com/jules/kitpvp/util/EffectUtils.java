package com.jules.kitpvp.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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

    public static void createHexagon(Location center, double radius) {
        for (int i = 0; i < 6; i++) {
            double angle1 = 2 * Math.PI * i / 6;
            double angle2 = 2 * Math.PI * (i + 1) / 6;
            Location p1 = center.clone().add(radius * Math.cos(angle1), 0, radius * Math.sin(angle1));
            Location p2 = center.clone().add(radius * Math.cos(angle2), 0, radius * Math.sin(angle2));
            drawParticleLine(p1, p2, 0.5);
        }
    }

    private static void drawParticleLine(Location from, Location to, double space) {
        double distance = from.distance(to);
        Vector vector = to.toVector().subtract(from.toVector()).normalize().multiply(space);
        for (double i = 0; i < distance; i += space) {
            from.add(vector);
            from.getWorld().spawnParticle(org.bukkit.Particle.FLAME, from, 0, 0, 0, 0, 1);
        }
    }

    public static void playFirework(org.bukkit.World world, org.bukkit.Location location, org.bukkit.FireworkEffect fireworkEffect) {
        org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) world.spawn(location, org.bukkit.entity.Firework.class, firework -> {
            org.bukkit.inventory.meta.FireworkMeta fwm = firework.getFireworkMeta();
            fwm.addEffect(fireworkEffect);
            fwm.setPower(0);
            firework.setFireworkMeta(fwm);
            firework.detonate();
        });
    }
}
