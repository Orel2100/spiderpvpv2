package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.util.EffectUtils;
import com.jules.kitpvp.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class IronPunch {

    public static void use(final Player p, final int upgrade) {
        // XP is handled in Golem.java
        EffectUtils.createCircle(p.getLocation(), 5, true);
        EffectUtils.createHelix(p.getLocation(), 5, 20*2);

        p.getWorld().playSound(p.getEyeLocation(), Sound.BLOCK_ANVIL_LAND, 1, 2);

        Location loc = p.getLocation().add(0, 3, 0);
        int theta = 30;

        for (int i = 0; i < 12; i++) {
            int deg = theta * i;
            double rad = Math.toRadians(deg);

            double x = 3 * Math.cos(rad);
            double z = 3 * Math.sin(rad);

            Location pt = loc.clone().add(x, 0, z);
            final FallingBlock fb = p.getWorld().spawnFallingBlock(pt, Material.IRON_BLOCK, (byte)0);
            fb.setDropItem(false);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(fb.isDead() || fb.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                        fb.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, fb.getLocation(), 1);
                        fb.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(KitPVP.getInstance(), 0, 1);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(KitPVP.getInstance(), new Runnable() {

            double damage = 0.5;
            @Override
            public void run() {
                for(int i = 0; i < upgrade; i++) {
                    damage += 0.5;
                }
                for(Entity ent : Utils.getNearbyEntities(p.getLocation(), 5)){
                    if(ent instanceof LivingEntity){
                        // Team check removed
                        if(ent == p) continue;
                        Utils.realDamage(ent, p, damage);
                        if(!ent.isDead()) {
                            Vector v = p.getLocation().toVector().subtract(ent.getLocation().toVector()).multiply(0.5).setY(0);
                            ent.setVelocity(v);
                        }
                    }
                }
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.7f);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.7f);
            }
        }, 10);
    }
}
