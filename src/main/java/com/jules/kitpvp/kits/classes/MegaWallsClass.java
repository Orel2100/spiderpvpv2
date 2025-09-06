package com.jules.kitpvp.kits.classes;

import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.kits.KitStat;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeCategory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public abstract class MegaWallsClass extends KitClass {

    public MegaWallsClass(String name, String[] lore, int cost, ItemStack icon, Upgrade... upgrades) {
        super(name, lore, cost, icon, upgrades);
    }

    public abstract Map<UpgradeCategory, List<Upgrade>> getUpgrades();

    public abstract KitStat getKitStat();
}
