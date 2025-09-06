package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zombie extends MegaWallsClass {

    public Zombie() {
        super(
                "Zombie",
                new String[]{"A tanky kit that gets stronger", "the more zombies are nearby."},
                500,
                new ItemStackCreator(Material.ROTTEN_FLESH, "§2Zombie").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 4, 1, 1, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.IRON_SWORD));
        items.add(new ItemStack(Material.IRON_HELMET));
        items.add(new ItemStack(Material.IRON_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_LEGGINGS));
        items.add(new ItemStack(Material.IRON_BOOTS));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.ZOMBIE;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
