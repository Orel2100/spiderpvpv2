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

public class Dreadlord extends MegaWallsClass {

    public Dreadlord() {
        super(
                "Dreadlord",
                new String[]{"A dark and powerful kit that", "can drain life from its enemies."},
                9000,
                new ItemStackCreator(Material.WITHER_SKELETON_SKULL, "§8Dreadlord").build()
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
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.DREADLORD;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
