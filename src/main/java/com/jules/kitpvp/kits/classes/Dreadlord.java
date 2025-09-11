package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.ShadowBurst;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dreadlord extends KitClass {

    public Dreadlord() {
        super("Dreadlord", "A master of dark magic that can drain the life from its enemies. Its Shadow Burst ability launches a volley of wither skulls that explode on impact, damaging and weakening enemies.", new ItemStack(Material.WITHER_SKELETON_SKULL),
                new ArrayList<>(Collections.singletonList(new ShadowBurst())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
    }

    @Override
    public void triggerAbility(Player player) {
        getAbilities().get(0).execute(player);
    }

    @Override
    public void onKill(Player killer, Player victim) {
        killer.setHealth(Math.min(killer.getHealth() + 4.0, killer.getMaxHealth())); // Heal 2 hearts
    }

    @Override
    public void onDeath(Player player) {
        // No action on death
    }
}
