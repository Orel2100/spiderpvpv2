package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Leap {

    public static void use(Player p, int distance) {
        p.setVelocity(p.getLocation().getDirection().multiply(distance / 5.0));
        p.setVelocity(new Vector(p.getVelocity().getX(), 1.1D, p.getVelocity().getZ()));
        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20*5,0));
        p.setLevel(0);
        p.setExp(0);
    }

}
