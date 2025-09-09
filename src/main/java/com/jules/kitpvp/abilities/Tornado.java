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

import java.util.Random;

public class Tornado {

    private static final double PULL_STRENGTH = 0.4;
    private static final int RADIUS = 6;
    private static final double MOVE_SPEED = 0.05; // Slower movement

    public static void use(Player p, int level) {
        Location tornadoCenter = p.getLocation();

        // Generate a random horizontal direction for the tornado to move
        Random random = new Random();
        double xDir = random.nextDouble() - 0.5;
        double zDir = random.nextDouble() - 0.5;
        final Vector moveDirection = new Vector(xDir, 0, zDir).normalize().multiply(MOVE_SPEED);

        new BukkitRunnable() {
            int ticks = 0;
            double damage = 0.5 + (level * 0.1);

            @Override
            public void run() {
                // Duration of 5 seconds
                if (ticks > 100) {
                    cancel();
                }

                // Move the tornado's center
                tornadoCenter.add(moveDirection);

                // --- Mechanics ---
                tornadoCenter.getWorld().playSound(tornadoCenter, Sound.ITEM_ELYTRA_FLYING, 0.5f, 1.5f);

                for (Entity e : tornadoCenter.getWorld().getNearbyEntities(tornadoCenter, RADIUS, RADIUS, RADIUS)) {
                    if (e instanceof LivingEntity && e != p) {
                        ((LivingEntity) e).damage(damage, p);
                        Vector pullVector = tornadoCenter.toVector().subtract(e.getLocation().toVector()).normalize().multiply(PULL_STRENGTH);
                        pullVector.setY(pullVector.getY() + 0.1);
                        e.setVelocity(pullVector);
                    }
                }

                // --- Visuals ---
                for (int j = 0; j < 12; j++) {
                    double angle = ticks * 0.5 + (j * Math.PI / 6); // Faster swirl
                    double particleRadius = RADIUS * (1 - (ticks / 120.0));

                    double x = tornadoCenter.getX() + particleRadius * Math.cos(angle);
                    double z = tornadoCenter.getZ() + particleRadius * Math.sin(angle);
                    double y = tornadoCenter.getY() + (ticks * 0.1); // Faster rise
                    Location particleLoc = new Location(tornadoCenter.getWorld(), x, y, z);
                    tornadoCenter.getWorld().spawnParticle(Particle.COMPOSTER, particleLoc, 0, 0, 0, 0, 1);

                    if (j % 2 == 0) {
                        double airRadius = particleRadius * 0.5;
                        double airAngle = ticks * -0.6 + (j * Math.PI / 6); // Faster opposite swirl
                        double airX = tornadoCenter.getX() + airRadius * Math.cos(airAngle);
                        double airZ = tornadoCenter.getZ() + airRadius * Math.sin(airAngle);
                        double airY = tornadoCenter.getY() + (j * 0.4);
                        Location airParticleLoc = new Location(tornadoCenter.getWorld(), airX, airY, airZ);
                        tornadoCenter.getWorld().spawnParticle(Particle.CLOUD, airParticleLoc, 0, 0, 0, 0, 1);
                    }
                }

                ticks++;
            }
        }.runTaskTimer(KitPVP.getInstance(), 0L, 1L);
    }
}
