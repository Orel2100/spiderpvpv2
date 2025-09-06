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

public class Skeleton extends MegaWallsClass {

    public Skeleton() {
        super(
                "Skeleton",
                new String[]{"A ranged kit that shoots", "arrows at its enemies."},
                1000,
                new ItemStackCreator(Material.BONE, "§fSkeleton").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 3, 2, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.BOW));
        items.add(new ItemStack(Material.ARROW, 16));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.SKELETON;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
