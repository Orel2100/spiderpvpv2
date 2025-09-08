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

public class Pirate extends MegaWallsClass {

    public Pirate() {
        super(
                "Pirate",
                new String[]{"A swashbuckling kit that excels", "at sea and has a powerful cannon."},
                8000,
                new ItemStackCreator(Material.TNT, "§cPirate").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(4, 2, 3, 3, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStackCreator(Material.IRON_SWORD, "§cPirate Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.PIRATE;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
