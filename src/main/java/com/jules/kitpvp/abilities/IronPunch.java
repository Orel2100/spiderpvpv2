package com.jules.kitpvp.abilities;

import com.jules.kitpvp.KitPVP;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class IronPunch {

    public static void use(Player player, int upgrade) {
        // XP consumption is handled in Golem.java's onInteract method.

        World world = player.getWorld();
        Location locUp = player.getEyeLocation();

        for (Player victim : inRange(player, 4.5)) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1));

            Vector vel = locUp.toVector().subtract(victim.getEyeLocation().toVector());
            double y = vel.getY();

            if (Math.abs(y) > 0.4) {
                vel.setY(0.4 / y);
            }

            double len = vel.length();

            if (len > 0.9) {
                vel.multiply(0.9 / len);
            }

            vel.add(victim.getVelocity());
            victim.setVelocity(vel);
        }

        Vector vel = new Vector(0, -1, 0);
        Location loc = player.getLocation();

        loc.add(0, 6, 0);
        int theta = 30;

        for (int i = 0; i < 12; i++) {
            int deg = theta * i;
            double rad = Math.toRadians(deg);

            double x = 3 * Math.cos(rad);
            double z = 3 * Math.sin(rad);

            Location pt = loc.clone().add(x, 0, z);
            FallingBlock block = world.spawnFallingBlock(pt, Material.IRON_BLOCK.createBlockData());

            block.setVelocity(vel);
            block.setDropItem(false);

            Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), block::remove, 60);
        }

        world.spawnParticle(Particle.LAVA, locUp, 40, 3, 0.1, 3, 10);

        world.playSound(locUp, Sound.BLOCK_ANVIL_LAND, 1, 2);

        Bukkit.getScheduler().runTaskLater(KitPVP.getInstance(), () -> {
            for (Player victim : inRange(player, 4.5)) {
                victim.damage(6, player);
            }

            world.spawnParticle(Particle.EXPLOSION_LARGE, loc, 3, 0.1, 0.1, 0.1, 0);

            world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 0.7f);
        }, 7);
    }

    private static List<Player> inRange(Player center, double radius) {
        List<Player> players = new ArrayList<>();
        for (Entity entity : center.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player) {
                players.add((Player) entity);
            }
        }
        return players;
    }
}
