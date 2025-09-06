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

public class Hunter extends MegaWallsClass {

    public Hunter() {
        super(
                "Hunter",
                new String[]{"A ranged kit that excels at", "keeping enemies at a distance."},
                5000,
                new ItemStackCreator(Material.BOW, "§aHunter").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(3, 2, 4, 3, 2);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.STONE_SWORD));
        items.add(new ItemStack(Material.BOW));
        items.add(new ItemStack(Material.ARROW, 32));
        items.add(new ItemStack(Material.LEATHER_HELMET));
        items.add(new ItemStack(Material.LEATHER_CHESTPLATE));
        items.add(new ItemStack(Material.LEATHER_LEGGINGS));
        items.add(new ItemStack(Material.LEATHER_BOOTS));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.HUNTER;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
