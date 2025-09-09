package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

public class Fireball {

    public static void use(Player p, int level) {
        p.launchProjectile(SmallFireball.class);
    }
}
