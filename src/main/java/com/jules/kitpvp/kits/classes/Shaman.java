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

public class Shaman extends MegaWallsClass {

    public Shaman() {
        super(
                "Shaman",
                new String[]{"A mystical kit that uses the", "power of nature to its advantage."},
                6000,
                new ItemStackCreator(Material.VINE, "§2Shaman").build()
        );
    }

    @Override
    public KitStat getKitStat() {
        return new KitStat(2, 3, 3, 4, 4);
    }

    @Override
    public List<ItemStack> getStartingItems(Player p) {
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStackCreator(Material.STONE_SWORD, "§2Shaman Sword").setLore(KitUtils.KIT_ITEM_LORE_LIST).build());
        return items;
    }

    @Override
    public List<PotionEffect> getPassiveEffects(Player p) {
        return new ArrayList<>();
    }

    @Override
    public Kit getKit() {
        return Kit.SHAMAN;
    }

    @Override
    public Map<UpgradeCategory, List<Upgrade>> getUpgrades() {
        return new HashMap<>();
    }
}
