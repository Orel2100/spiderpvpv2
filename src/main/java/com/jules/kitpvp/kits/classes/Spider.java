package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spider extends MegaWallsClass {

    public Spider() {
        super(
                "Spider",
                new String[]{"A fast and agile kit that can", "climb walls."},
                2000,
                new ItemStackCreator(Material.SPIDER_EYE, "§8Spider").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 2, 5, 2, 3);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.STONE_SWORD));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        return effects;
    }

    @Override
    public Kit getKit() {
        return Kit.SPIDER;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
