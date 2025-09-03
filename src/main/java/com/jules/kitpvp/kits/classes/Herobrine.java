package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.Wrath;
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

public class Herobrine extends KitClass {

    public Herobrine() {
        super("Herobrine", "A powerful being that gains strength from kills.", new ItemStack(Material.PLAYER_HEAD),
                new ArrayList<>(Collections.singletonList(new Wrath())), new ArrayList<>());
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
    }

    @Override
    public void onKill(Player killer, Player victim) {
        killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0)); // Strength for 10 seconds
    }

    @Override
    public void onDeath(Player player) {
        // No action on death
    }
}
