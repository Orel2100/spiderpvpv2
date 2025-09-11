package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.BurningSoul;
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

public class Pigman extends KitClass {

    public Pigman() {
        super("Pigman", "A resilient and powerful class that gets stronger the more damage it takes. Its Burning Soul ability damages nearby enemies and grants the Pigman a strength boost.", new ItemStack(Material.PORKCHOP),
                new ArrayList<>(Collections.singletonList(new BurningSoul())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.GOLDEN_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_SWORD));
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
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
    }
}
