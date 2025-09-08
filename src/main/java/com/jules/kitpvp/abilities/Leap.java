package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Leap {

    public static List<Player> leaping = new ArrayList<>();

    public static void use(Player p, int distance) {
        p.setVelocity(p.getLocation().getDirection().multiply(distance / 5.0));
        p.setVelocity(new Vector(p.getVelocity().getX(), 1.1D, p.getVelocity().getZ()));
        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20*5,0));
        p.setLevel(0);
        p.setExp(0);
        leaping.add(p);
    }

}
