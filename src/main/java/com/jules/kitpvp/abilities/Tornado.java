package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Tornado {

    public static void use(Player player, int level) {
        new BukkitRunnable() {
            final Location loc = player.getLocation();
            double radius = 1;
            double y = 0;
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 100) { // 5 seconds
                    this.cancel();
                }

                loc.add(0, y, 0);
                for (int i = 0; i < 360; i += 20) {
                    double angle = i * Math.PI / 180;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    loc.add(x, 0, z);
                    player.getWorld().spawnParticle(Particle.CLOUD, loc, 0, 0, 0, 0, 1);
                    loc.subtract(x, 0, z);
                }
                loc.subtract(0, y, 0);

                for (Entity entity : player.getNearbyEntities(radius, 4, radius)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        entity.setVelocity(new Vector(0, 0.5, 0));
                    }
                }

                y += 0.1;
                if (y > 4) {
                    y = 0;
                }
                ticks++;
            }
        }.runTaskTimer(KitPVP.getInstance(), 0L, 1L);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                double damage = 1.0 + (level * 0.25);
                for(Entity ent : player.getNearbyEntities(4, 4, 4)) {
                    if(ent instanceof LivingEntity) {
                        if(ent == player) continue;
                        ((LivingEntity)ent).damage(damage, player);
                    }
                }
                i++;
                if(i >= 5) {
                    cancel();
                }
            }
        }.runTaskTimer(KitPVP.getInstance(), 0, 20);
    }
}
