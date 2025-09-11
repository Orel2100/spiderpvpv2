package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.Tornado;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.Upgrade;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Shaman extends KitClass {

    private final Random random = new Random();

    public Shaman() {
        super("Shaman", "A mystical class that can summon the forces of nature to aid it in battle. Its Tornado ability creates a vortex of wind that damages and throws around enemies.", new ItemStack(Material.TOTEM_OF_UNDYING),
                new ArrayList<>(Collections.singletonList(new Tornado())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.VINE));
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
        if (random.nextInt(100) < 5) { // 5% chance
            player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
        }
    }
}
