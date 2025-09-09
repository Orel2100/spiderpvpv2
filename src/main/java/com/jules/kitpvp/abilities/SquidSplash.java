package com.jules.kitpvp.abilities;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SquidSplash {

    public static void use(Player p, int level) {
        p.playSound(p.getLocation(), Sound.ENTITY_SQUID_SQUIRT, 5, 5);
        for (Entity entity : p.getNearbyEntities(5, 5, 5)) {
            if (entity instanceof LivingEntity && entity != p) {
                int duration = 20 * (2 + level);
                int amplifier = level / 2;
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, amplifier));
            }
        }
    }
}
