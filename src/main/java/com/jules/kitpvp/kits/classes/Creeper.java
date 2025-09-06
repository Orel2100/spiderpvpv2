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

public class Creeper extends MegaWallsClass {

    public Creeper() {
        super(
                "Creeper",
                new String[]{"An explosive kit that can", "detonate itself to deal damage."},
                3000,
                new ItemStackCreator(Material.TNT, "§2Creeper").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(1, 1, 3, 2, 2);
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
        return Kit.CREEPER;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
