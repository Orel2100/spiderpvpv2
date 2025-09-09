package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Tornado {

    private static final double PULL_STRENGTH = 0.4;
    private static final int RADIUS = 8;

    public static void use(Player p, int level) {
        new BukkitRunnable() {
            int ticks = 0;
            double damage = 0.5 + (level * 0.1);

            @Override
            public void run() {
                // Duration of 5 seconds
                if (ticks > 100) {
                    cancel();
                }

                // --- Mechanics ---
                // Play sound
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_ELYTRA_FLYING, 0.5f, 1.5f);

                // Pull and damage enemies
                for (Entity e : p.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
                    if (e instanceof LivingEntity && e != p) {
                        // Damage
                        ((LivingEntity) e).damage(damage, p);

                        // Pull
                        Vector pullVector = p.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(PULL_STRENGTH);
                        // Add a slight upward lift to make it feel more like a vortex
                        pullVector.setY(pullVector.getY() + 0.1);
                        e.setVelocity(pullVector);
                    }
                }

                // --- Visuals ---
                // Spawn swirling particles
                for (int j = 0; j < 6; j++) { // spawn 6 particles per tick
                    double angle = ticks * 0.2 + (j * Math.PI / 3); // 0.2 rad/tick speed
                    double particleRadius = RADIUS * (1 - (ticks / 100.0)); // Tornado shrinks over time
                    double x = p.getLocation().getX() + particleRadius * Math.cos(angle);
                    double z = p.getLocation().getZ() + particleRadius * Math.sin(angle);
                    double y = p.getLocation().getY() + (ticks * 0.05); // particles rise slowly

                    Location particleLoc = new Location(p.getWorld(), x, y, z);
                    p.getWorld().spawnParticle(Particle.COMPOSTER, particleLoc, 0, 0, 0, 0, 1);
                }

                ticks++;
            }
        }.runTaskTimer(KitPVP.getInstance(), 0L, 1L);
    }
}
