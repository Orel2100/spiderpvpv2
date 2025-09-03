package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.SquidSplash;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Squid extends KitClass {

    public Squid() {
        super("Squid", "An aquatic class that can heal in water.", new ItemStack(Material.INK_SAC),
                new ArrayList<>(Collections.singletonList(new SquidSplash())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
    }

    @Override
    public void triggerAbility(Player player) {
        getAbilities().get(0).execute(player);
    }

    @Override
    public void onKill(Player killer, Player victim) {
        // No action on kill
    }

    @Override
    public void onDeath(Player player) {
        // No action on death
    }

    public void passive(Player player) {
        if (player.getHealth() / player.getMaxHealth() < 0.5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
        }
    }
}
