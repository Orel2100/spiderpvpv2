package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.player.MPlayer;
//import com.jules.kitpvp.team.TeamManager;
import com.jules.kitpvp.util.Utils;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Detonate {

    public static void use(final Player p, final int upgrade) {
        new BukkitRunnable() {

            int i = 4;
            double damage = 1.25;
            @Override
            public void run() {
                p.playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 5, 5);
                for(Entity ps : p.getNearbyEntities(10, 10, 10)) {
                    if(ps == p) continue;
                    if(ps instanceof Player) {
                        ((Player) ps).playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 5, 5);
                    }
                }
                i--;
                if(i == 0) {
                    for(int i = 0; i < upgrade; i++) {
                        damage += 0.75;
                    }
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 5);
                    for(Entity ps : p.getNearbyEntities(10, 10, 10)) {
                        if(ps == p) continue;
                        if(ps instanceof Player) {
                            ((Player) ps).playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 5);
                        }
                    }
                    for(Entity ent : p.getNearbyEntities(3, 3, 3)) {
                        if(ent instanceof LivingEntity) {
                            if(ent == p) continue;
                            /*if(TeamManager.getTeamByPlayer(p) != null) {
                                if(TeamManager.getTeamByPlayer(p).getPlayers().contains(ent)) {
                                    continue;
                                }
                            }*/
                            if(ent instanceof Player) {
                                Player enp = (Player)ent;
                                MPlayer player = MPlayer.getMPlayer(enp.getUniqueId());
                                if(player != null && player.getKit() != null) {
                                    if(player.getKit().getName().equalsIgnoreCase("Creeper")) {
                                        Utils.realDamage(ent, p, damage/3);
                                        continue;
                                    }
                                    if(player.getKit().getName().equalsIgnoreCase("Arcanist")) {
                                        Utils.realDamage(ent, p, damage/2);
                                        continue;
                                    }
                                }
                            }
                            Utils.realDamage(ent, p, damage);
                        }
                    }
                    //Break some near blocks
                    p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "BOOM!");
                    cancel();
                    return;
                }
                p.getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_ANGRY, p.getLocation(), 100, 0, 0, 0, 3);
                p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You gonna explode in " + i + " seconds!");
            }
        }.runTaskTimer(KitPVP.getInstance(), 0, 20);
    }

}
