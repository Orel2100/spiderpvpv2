package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.ImmolatingBurst;
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

public class Blaze extends KitClass {

    public Blaze() {
        super("Blaze", "A fiery class with a fireball burst.", new ItemStack(Material.BLAZE_ROD),
                new ArrayList<>(Collections.singletonList(new ImmolatingBurst())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
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
}
