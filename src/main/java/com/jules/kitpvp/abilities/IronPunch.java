package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.util.EffectUtils;
import com.jules.kitpvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class IronPunch {

    public static void use(final Player p, final int upgrade) {
        // XP is handled in Golem.java
        EffectUtils.createCircle(p.getLocation(), 5, 20*2);
        EffectUtils.createHelix(p.getLocation(), 5, 20*2);
        for(int x = -2; x<4; x=x+2) {
            for(int z = -2; z<4; z=z+2) {
                if(x == 0 && z == 0) continue;
                final FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation().add(x, 3, z), Material.IRON_BLOCK, (byte)0);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if(fb.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                            for(Entity ent : fb.getNearbyEntities(15, 15, 15)) {
                                if(ent instanceof Player) {
                                    Player enp = (Player)ent;
                                    enp.playEffect(fb.getLocation(), Effect.STEP_SOUND, Material.IRON_BLOCK);
                                }
                            }
                            fb.remove();
                            cancel();
                        }
                    }
                }.runTaskTimer(KitPVP.getInstance(), 0, 1);
            }
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
                        if(ent instanceof Player) {
                            ((Player)ent).playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                        }
                        // Team check removed
                        if(ent == p) continue;
                        Utils.realDamage(ent, p, damage);
                        if(!ent.isDead()) {
                            Vector v = p.getLocation().toVector().subtract(ent.getLocation().toVector()).multiply(0.5).setY(0);
                            ent.setVelocity(v);
                        }
                    }
                }
            }
        }, 10);
    }
}
