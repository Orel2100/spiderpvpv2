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

public class Herobrine extends MegaWallsClass {

    public Herobrine() {
        super(
                "Herobrine",
                new String[]{"A mysterious and powerful kit."},
                10000,
                new ItemStackCreator(Material.NETHER_STAR, "§dHerobrine").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 3, 3, 4, 5);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.DIAMOND_SWORD));
        items.add(new ItemStack(Material.GOLDEN_APPLE, 1));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.HEROBRINE;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
