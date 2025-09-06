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

public class Pigman extends MegaWallsClass {

    public Pigman() {
        super(
                "Pigman",
                new String[]{"A strong and tanky kit that gets", "stronger as it takes damage."},
                7500,
                new ItemStackCreator(Material.PORKCHOP, "§dPigman").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 4, 2, 3, 3);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.DIAMOND_SWORD));
        items.add(new ItemStack(Material.GOLDEN_APPLE, 3));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.PIGMAN;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
