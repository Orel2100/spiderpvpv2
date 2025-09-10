package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.util.EffectUtils;
import com.jules.kitpvp.util.Utils;
import com.jules.kitpvp.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class IronPunch {

    public static void use(final Player p, final int upgrade) {
        p.setLevel(0);
        p.setExp(0);

        EffectUtils.createHexagon(p.getLocation(), 4.5);

        for (int x = -2; x < 4; x = x + 2) {
            for (int z = -2; z < 4; z = z + 2) {
                if (x == 0 && z == 0) continue;
                final FallingBlock fb = p.getWorld().spawnFallingBlock(p.getLocation().add(x, 3, z), Material.IRON_BLOCK.createBlockData());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (fb.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                            fb.remove();
                            cancel();
                        }
                    }
                }.runTaskTimer(KitPVP.getInstance(), 0, 1);
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(KitPVP.getInstance(), () -> {
            double damage = 1.0 + (upgrade - 1) * 0.5;

            for (Entity ent : Utils.getNearbyEntities(p.getLocation(), 4.5)) {
                if (ent instanceof LivingEntity) {
                    if (ent instanceof Player) {
                        ((Player) ent).playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                    }
                    if (TeamManager.getTeamByPlayer(p) != null) {
                        if (TeamManager.getTeamByPlayer(p).getPlayers().contains(ent)) {
                            continue;
                        }
                    }
                    if (ent == p) continue;

                    Utils.realDamage(ent, p, damage);

                    if (!ent.isDead()) {
                        Vector v = p.getLocation().toVector().subtract(ent.getLocation().toVector()).normalize().multiply(1.5).setY(0.5);
                        ent.setVelocity(v);
                        ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 1));
                        ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 0));
                    }
                }
            }
        }, 10);
    }
}
