package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.*;
import com.jules.kitpvp.util.ItemStackCreator;
import com.jules.kitpvp.util.KitUtils;
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
        items.add(new ItemStackCreator(Material.STONE_SWORD, "§aHunter Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.BOW, "§aHunter Bow").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.ARROW, "§aHunter Arrow").setAmount(32).setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_HELMET, "§aHunter Helmet").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_CHESTPLATE, "§aHunter Chestplate").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_LEGGINGS, "§aHunter Leggings").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        items.add(new ItemStackCreator(Material.LEATHER_BOOTS, "§aHunter Boots").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
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
