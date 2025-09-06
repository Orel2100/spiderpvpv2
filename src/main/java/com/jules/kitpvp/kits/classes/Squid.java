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

public class Squid extends MegaWallsClass {

    public Squid() {
        super(
                "Squid",
                new String[]{"A slippery kit that can blind", "its enemies with ink."},
                1500,
                new ItemStackCreator(Material.INK_SAC, "§1Squid").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(1, 2, 4, 2, 2);
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
        effects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
        return effects;
    }

    @Override
    public Kit getKit() {
        return Kit.SQUID;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
