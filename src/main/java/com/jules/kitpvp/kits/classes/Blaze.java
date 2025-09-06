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

public class Blaze extends MegaWallsClass {

    public Blaze() {
        super(
                "Blaze",
                new String[]{"A fiery kit that can shoot", "fireballs and is immune to fire."},
                7000,
                new ItemStackCreator(Material.BLAZE_ROD, "§6Blaze").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 2, 3, 4, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.STONE_SWORD));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.BLAZE;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
