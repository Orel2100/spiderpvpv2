package com.jules.kitpvp.abilities;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Heal implements Ability {

    @Override
    public String getName() {
        return "Heal";
    }

    @Override
    public String getDescription() {
        return "Heals you for a small amount.";
    }

    @Override
    public void execute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
    }
}
