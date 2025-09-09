package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Leap {

    public static List<Player> leaping = new ArrayList<>();
    private static final double BASE_LEAP_POWER = 1.1D;

    public static void use(Player p, int distance) {
        // 'distance' is a value from 10-30 passed from Spider.java, based on level.
        // We use it to scale the horizontal power of the leap.
        double horizontal_multiplier = 1.5 + (distance / 10.0); // scales from 2.5 to 4.5, a reasonable range

        Vector direction = p.getLocation().getDirection().normalize();
        Vector leapVelocity = new Vector(direction.getX() * horizontal_multiplier, BASE_LEAP_POWER, direction.getZ() * horizontal_multiplier);

        p.setVelocity(leapVelocity);
        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 5, 0));
        p.setLevel(0);
        p.setExp(0);
        leaping.add(p);
    }
}
