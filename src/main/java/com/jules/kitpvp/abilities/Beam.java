package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import com.jules.kitpvp.team.TeamManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Beam {

    // Helper method to get nearby entities, but we'll use a more direct method for the beam.
    public static List<Entity> getNearbyEntites(Location l, int size) {
        List<Entity> entities = new ArrayList<Entity>();
        for(Entity ent : l.getWorld().getEntities()) {
            if(!(ent instanceof LivingEntity)) continue;
            if(!(ent instanceof Player)) continue;
            if(ent.isDead()) continue;
            if(ent == null) continue;
            if(!ent.getWorld().equals(l.getWorld())) continue;
            if(l.distance(ent.getLocation()) <= size) {
                entities.add(ent);
            }
        }
        return entities;
    }

    public static void shoot(Player p, double damage) {
        // Play the distinct arcanist beam sound
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 2.0f);

        Location startLoc = p.getEyeLocation().clone();
        Vector direction = startLoc.getDirection().normalize();

        // Use a BukkitRunnable to create a fast, but temporary, beam effect.
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if(ticks > 25) { // Beam lasts for a fraction of a second
                    cancel();
                    return;
                }

                Location beamLoc = startLoc.clone().add(direction.clone().multiply(ticks));

                // Spawn a single, bright particle to create the beam
                beamLoc.getWorld().spawnParticle(Particle.FIREWORK, beamLoc, 0, 0, 0, 0, 1);
                beamLoc.getWorld().spawnParticle(Particle.END_ROD, beamLoc, 0, 0, 0, 0, 1);

                // Check for entities to damage along the beam's path
                for (Entity ent : beamLoc.getWorld().getNearbyEntities(beamLoc, 1.5, 1.5, 1.5)) {
                    if (ent instanceof Player) {
                        Player nearby = (Player) ent;
                        // Don't damage the caster or teammates
                        if (nearby.equals(p) || (TeamManager.getTeamByPlayer(p) != null && TeamManager.getTeamByPlayer(p).getPlayers().contains(nearby))) {
                            continue;
                        }
                        // Damage the enemy player
                        nearby.damage(damage, p);
                    }
                }
                ticks++;
            }
        }.runTaskTimer(KitPVP.getInstance(), 0L, 1L);
    }
}
