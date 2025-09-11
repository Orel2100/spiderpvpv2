package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.abilities.Ability;
import com.jules.kitpvp.abilities.HomingTask;
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
import java.util.Random;

public class Hunter extends KitClass {

    private final Random random = new Random();

    public Hunter() {
        super("Hunter", "A master archer that can track and eliminate its prey with deadly accuracy. Its Eagle's Eye ability makes its arrows home in on enemies for a short period.", new ItemStack(Material.BOW),
                new ArrayList<>(Collections.singletonList(new HomingTask())), new ArrayList<>());
    }

    @Override
    public void applyKit(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
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
        if (random.nextInt(100) < 10) { // 10% chance
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
        }
    }
}
