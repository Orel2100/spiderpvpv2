package com.jules.kitpvp.abilities;

import com.jules.kitpvp.team.TeamManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Beam {

    public static void shoot(Player p, double damage) {
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 2.0f);

        Location startLoc = p.getEyeLocation().clone();
        Vector direction = startLoc.getDirection().normalize();

        HashSet<LivingEntity> hitEntities = new HashSet<>();

        for (double i = 1; i < 30; i += 0.5) { // Beam length of 30 blocks, incrementing every 0.5 blocks
            Location beamLoc = startLoc.clone().add(direction.clone().multiply(i));

            if (!beamLoc.getBlock().isPassable() && !beamLoc.getBlock().isLiquid()) {
                break; // Stop beam if it hits a solid block
            }

            beamLoc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, beamLoc, 0, 0, 0, 0, 0);
            beamLoc.getWorld().spawnParticle(Particle.END_ROD, beamLoc, 0, 0, 0, 0, 0);

            for (Entity ent : beamLoc.getWorld().getNearbyEntities(beamLoc, 1.5, 1.5, 1.5)) {
                if (ent instanceof LivingEntity && !ent.equals(p) && !hitEntities.contains(ent)) {

                    if (ent instanceof Player) {
                        Player nearby = (Player) ent;
                        if (TeamManager.getTeamByPlayer(p) != null && TeamManager.getTeamByPlayer(p).getPlayers().contains(nearby)) {
                            continue;
                        }
                    }

                    ((LivingEntity) ent).damage(damage, p);
                    hitEntities.add((LivingEntity) ent);
                }
            }
        }
    }
}
