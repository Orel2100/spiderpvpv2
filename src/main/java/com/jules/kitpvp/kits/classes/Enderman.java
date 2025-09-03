package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.Teleport;
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

public class Enderman extends KitClass {

    public Enderman() {
        super("Enderman", "A mobile class that teleports to enemies.", new ItemStack(Material.ENDER_PEARL),
                new ArrayList<>(Collections.singletonList(new Teleport())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public void triggerAbility(Player player) {
        getAbilities().get(0).execute(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1)); // Speed for 5 seconds
    }

    @Override
    public void onKill(Player killer, Player victim) {
        // No action on kill
    }

    @Override
    public void onDeath(Player player) {
        // No action on death
    }
}
