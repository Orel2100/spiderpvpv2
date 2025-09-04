package com.jules.kitpvp.abilities;

import com.jules.kitpvp.team.TeamManager;
import org.bukkit.Particle;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Heal {

    public static void use(Player p, int upgrade) {
        double damage = 1.5;
        for(int i = 0; i<upgrade; i++) {
            damage+=0.5;
        }
        p.setHealth(p.getHealth() + damage < p.getMaxHealth() ? p.getHealth() + damage : p.getMaxHealth());
        p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You were healed by your heal skill");
        for(Entity ent : p.getNearbyEntities(5, 5, 5)) {
            if(ent instanceof LivingEntity && ent instanceof Player) {
                Player enp = (Player)ent;
                if(enp == p) {
                    continue;
                }
                if(TeamManager.getTeamByPlayer(p) != null) {
                    if(TeamManager.getTeamByPlayer(p).getPlayers().contains(enp)) {
                        double h = damage/2;
                        enp.setHealth(enp.getHealth() + h < enp.getMaxHealth() ? enp.getHealth() + h : enp.getMaxHealth());
                        enp.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You were healed by " + p.getName() + "'s heal skill");
                    }
                }
            }
        }
        p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 4, 0, 1, 0, 1);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
        for(Entity ps : p.getNearbyEntities(10, 10, 10)) {
            if(ps == p) continue;
            if(ps instanceof Player) {
                ((Player) ps).playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
            }
        }
    }

}
