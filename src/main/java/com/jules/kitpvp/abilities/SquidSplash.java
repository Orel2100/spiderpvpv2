package com.jules.kitpvp.abilities;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SquidSplash {

    public static void use(Player player, int level) {
        World world = player.getWorld();
        Location loc = player.getLocation();

        world.playSound(loc, Sound.ENTITY_PLAYER_SPLASH, 1, 1);
        world.spawnParticle(Particle.WATER_SPLASH, loc, 500, 0.5, 0.5, 0.5, 1);

        double healedAmount = 0;

        for (Player victim : inRange(player, 5)) {
            Vector vel = loc.toVector().subtract(victim.getLocation().toVector());
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

            double damage = 3.5;
            healedAmount += damage * 0.7;

            victim.damage(damage, player);
        }

        if (healedAmount > 7) {
            healedAmount = 7;
        }

        double newHealth = player.getHealth() + healedAmount;
        if (newHealth > player.getMaxHealth()) {
            newHealth = player.getMaxHealth();
        }
        player.setHealth(newHealth);
    }

    private static List<Player> inRange(Player center, double radius) {
        List<Player> players = new ArrayList<>();
        for (Entity entity : center.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player && entity != center) {
                players.add((Player) entity);
            }
        }
        return players;
    }
}
