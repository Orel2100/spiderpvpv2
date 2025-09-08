package com.jules.kitpvp.abilities;

import java.util.HashMap;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Particle;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class ExplosiveArrow {

    public static HashMap<Arrow, Double> exArrow = new HashMap<>();

    public static void use(final Player p, final int upgrade, org.bukkit.event.entity.EntityShootBowEvent event) {
        final Arrow a = (Arrow) event.getProjectile();
        a.setShooter(p);
        double damage = 4.0 + (upgrade - 1) * 0.5;
        exArrow.put(a, damage);
        new BukkitRunnable() {
            public void run() {
                if(a.isDead() || a == null){ cancel(); return;}
                if(a.isOnGround() || a.isInsideVehicle() || a.isCritical() || a.isDead()) {
                    cancel();
                    return;
                }
                a.getWorld().spawnParticle(Particle.CRIT_MAGIC, a.getLocation(), 50, 0, 0, 0, 15);
            }
        }.runTaskTimer(KitPVP.getInstance(), 0, 1);
    }

}
