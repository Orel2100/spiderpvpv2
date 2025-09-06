package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitStat;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeCategory;
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

public class Golem extends MegaWallsClass {

    public Golem() {
        super(
                "Golem",
                new String[] {
                        "A very tanky kit.",
                        "Use your ability to deal",
                        "damage to nearby enemies."
                },
                3000,
                new ItemStackCreator(Material.IRON_BLOCK, "§bGolem").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 5, 1, 2, 1);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.IRON_SWORD));
        items.add(new ItemStack(Material.IRON_HELMET));
        items.add(new ItemStack(Material.IRON_CHESTPLATE));
        items.add(new ItemStack(Material.IRON_LEGGINGS));
        items.add(new ItemStack(Material.IRON_BOOTS));
        items.add(new ItemStack(Material.GOLDEN_APPLE, 2));
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        List<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
        effects.add(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
        return effects;
    }

    @Override
    public Kit getKit() {
        return Kit.GOLEM;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
