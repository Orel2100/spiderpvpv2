package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.Random;

public class Tornado {

    private static final double PULL_STRENGTH = 0.2; // Weaker pull
    private static final int RADIUS = 7;
    private static final double MOVE_SPEED = 0.12;
    private static final double DAMAGE_TICK_RATE = 0.25;
    private static final int TICKS_PER_SECOND = 20;
    private static final Random random = new Random();

    public static void use(LivingEntity caster, int level) {
        // Find the ground location below the caster
        Location groundLoc = caster.getLocation().clone();
        for (int y = groundLoc.getBlockY(); y > 0; y--) {
            Location checkLoc = new Location(groundLoc.getWorld(), groundLoc.getX(), y, groundLoc.getZ());
            if (checkLoc.getBlock().getType().isSolid()) {
                groundLoc.setY(y + 1); // Set tornado base to be 1 block above the ground
                break;
            }
        }
        final Location tornadoCenter = groundLoc;

        double durationSeconds = 3.0 + (level * 0.5);
        final int totalTicks = (int) (durationSeconds * TICKS_PER_SECOND);

        double damagePerSecond = 1.0 + (level * 0.5);
        final double damagePerTick = damagePerSecond / (1.0 / DAMAGE_TICK_RATE);

        Random random = new Random();
        double xDir = random.nextDouble() - 0.5;
        double zDir = random.nextDouble() - 0.5;
        final Vector moveDirection = new Vector(xDir, 0, zDir).normalize().multiply(MOVE_SPEED);

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= totalTicks) {
                    cancel();
                    return;
                }

                tornadoCenter.add(moveDirection);
                tornadoCenter.getWorld().playSound(tornadoCenter, Sound.ENTITY_ENDER_DRAGON_FLAP, 0.2f, 1.9f);

                for (Entity e : tornadoCenter.getWorld().getNearbyEntities(tornadoCenter, RADIUS, 10, RADIUS)) {
                    if (e instanceof LivingEntity && !e.equals(caster)) {
                        LivingEntity target = (LivingEntity) e;
                        if (ticks % (int)(TICKS_PER_SECOND * DAMAGE_TICK_RATE) == 0) {
                            target.damage(damagePerTick, caster);
                        }
                        Vector pullVector = tornadoCenter.toVector().subtract(target.getLocation().toVector()).normalize().multiply(PULL_STRENGTH);
                        pullVector.setY(pullVector.getY() + 0.15);
                        target.setVelocity(pullVector);
                    }
                }

                // --- Visuals ---
                double tornadoHeight = ticks * 0.3;
                if (tornadoHeight > 9) tornadoHeight = 9;

                for (double y = 0; y < tornadoHeight; y += 0.75) {
                    double currentRadius = (y / tornadoHeight) * RADIUS;
                    if (currentRadius < 1.5) currentRadius = 1.5;

                    int particleCount = (int)(currentRadius * 1.5);

                    for (int i = 0; i < particleCount; i++) {
                        double angle = (ticks * 0.5) + (y * 0.5) + (i * (2 * Math.PI / particleCount));
                        double x = tornadoCenter.getX() + currentRadius * Math.cos(angle);
                        double z = tornadoCenter.getZ() + currentRadius * Math.sin(angle);
                        Location particleLoc = new Location(tornadoCenter.getWorld(), x, tornadoCenter.getY() + y, z);

                        tornadoCenter.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 0, 0, 0, 0, 0.1);
                    }
                }
                ticks++;
            }
        }.runTaskTimer(KitPVP.getInstance(), 0L, 1L);
    }
}
