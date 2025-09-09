package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Tornado {

    private static final double PULL_STRENGTH = 0.4;
    private static final int RADIUS = 6;
    private static final double DAMAGE_TICK_RATE = 0.25; // 0.25 seconds per tick
    private static final int TICKS_PER_SECOND = 20;

    public static void use(LivingEntity caster, int level) {
        // The tornado is static at the caster's location when used.
        final Location tornadoCenter = caster.getLocation().clone();

        // Duration in ticks, based on the Canvas tier table.
        double durationSeconds = 3.0 + (level * 0.5);
        final int totalTicks = (int) (durationSeconds * TICKS_PER_SECOND);

        // Damage per tick
        double damagePerSecond = 1.0 + (level * 0.5);
        final double damagePerTick = damagePerSecond / (1.0 / DAMAGE_TICK_RATE);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                // Cancel after the specified duration
                if (ticks >= totalTicks) {
                    cancel();
                    return;
                }

                // Play sound effect
                tornadoCenter.getWorld().playSound(tornadoCenter, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.5f, 1.5f);

                // Apply pull and damage to nearby entities
                for (Entity e : tornadoCenter.getWorld().getNearbyEntities(tornadoCenter, RADIUS, RADIUS, RADIUS)) {
                    if (e instanceof LivingEntity && !e.equals(caster)) {
                        LivingEntity target = (LivingEntity) e;

                        // Apply damage every 5 ticks (0.25s) to match the Canvas.
                        if (ticks % (TICKS_PER_SECOND * DAMAGE_TICK_RATE) == 0) {
                            target.damage(damagePerTick, caster);
                        }

                        // Pull entity towards the center of the tornado
                        Vector pullVector = tornadoCenter.toVector().subtract(target.getLocation().toVector()).normalize().multiply(PULL_STRENGTH);
                        // Make the pull slightly upward to create a swirling effect
                        pullVector.setY(pullVector.getY() + 0.1);
                        target.setVelocity(pullVector);
                    }
                }

                // --- Visuals: Enhanced Tornado Shape ---
                // Increase the size of the tornado as it progresses
                double currentRadius = RADIUS * (1 - (double) ticks / totalTicks);
                if (currentRadius < 1.0) currentRadius = 1.0;

                for (double y = 0; y < (totalTicks - ticks) * 0.1; y += 0.5) {
                    double angle = ticks * 0.2 + (y * 2.5);
                    double x = tornadoCenter.getX() + currentRadius * Math.cos(angle);
                    double z = tornadoCenter.getZ() + currentRadius * Math.sin(angle);

                    tornadoCenter.getWorld().spawnParticle(Particle.CLOUD, new Location(tornadoCenter.getWorld(), x, tornadoCenter.getY() + y, z), 0, 0, 0, 0, 1);
                    tornadoCenter.getWorld().spawnParticle(Particle.SWEEP_ATTACK, new Location(tornadoCenter.getWorld(), x, tornadoCenter.getY() + y, z), 0, 0, 0, 0, 1);
                }

                ticks++;
            }
        }.runTaskTimer(KitPVP.getInstance(), 0L, 1L);
    }
}
