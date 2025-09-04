package com.jules.kitpvp.abilities;

//import me.main.yoni.Main;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jules.kitpvp.KitPVP;
import org.bukkit.Particle;

public class ImmolatingBurst {

    public static void shoot(final Player p) {
        new BukkitRunnable() {

            int i = 0;
            @Override
            public void run() {
                if(!KitPVP.getPlaying().contains(p.getName())) {
                    cancel();
                    return;
                }
                Fireball fb = p.launchProjectile(Fireball.class);
                fb.setShooter(p);
                fb.setDirection(p.getEyeLocation().getDirection());
                fb.setVelocity(p.getEyeLocation().getDirection().multiply(1.5));
                fb.setYield(0);
                fb.setFireTicks(0);
                i++;
                if(i == 3) {
                    cancel();
                }
            }
        }.runTaskTimer(KitPVP.getInstance(), 0, 10);
    }

    public static void use(final Player p, int upgrade) {
        new BukkitRunnable() {

            int i = 3;
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 5, 5);
                for(Entity ent : p.getNearbyEntities(15, 15, 15)) {
                    if(ent == p) continue;
                    if(ent instanceof Player) {
                        Player enp = (Player)ent;
                        enp.playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 5, 5);
                    }
                }
                i--;
                if(i == 0) {
                    shoot(p);
                    cancel();
                    return;
                }
                //Other particles
                p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 5, 0, 0, 0, 1);
                for(Entity ent : p.getNearbyEntities(30, 30, 30)) {
                    if(ent == p) continue;
                    if(ent instanceof Player) {
                        Player enp = (Player)ent;
                        enp.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 5, 0, 0, 0, 1);
                    }
                }
            }
        }.runTaskTimer(KitPVP.getInstance(), 0, 20);
    }

}
