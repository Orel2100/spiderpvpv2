package com.jules.kitpvp.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import com.jules.kitpvp.kits.ClassType;
import com.jules.kitpvp.kits.Kit;
import com.jules.kitpvp.kits.KitClass;
import com.jules.kitpvp.player.MPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopGUI implements InventoryHolder {

    private final Inventory inventory;

    public ShopGUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 9 * 3, "Shop");
        initializeItems(player);
    }

    private void initializeItems(Player player) {
        MPlayer mPlayer = MPlayer.getMPlayer(player.getUniqueId());

        // Normal Kits
        int normalKitsOwned = 0;
        int normalKitsTotal = 0;
        int normalKitsCost = 0;
        for (Kit kit : Kit.values()) {
            if (kit.getClassType() == ClassType.NORMAL) {
                normalKitsTotal++;
                if (mPlayer.hasKit(kit)) {
                    normalKitsOwned++;
                } else {
                    try {
                        normalKitsCost += kit.getKitClass().newInstance().getPrice();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ItemStack normalKits = new ItemStack(Material.IRON_SWORD);
        ItemMeta normalKitsMeta = normalKits.getItemMeta();
        normalKitsMeta.setDisplayName(ChatColor.GREEN + "Normal Kits");
        List<String> normalLore = new ArrayList<>();
        normalLore.add(ChatColor.GRAY + "Click to view normal kits.");
        normalLore.add(" ");
        normalLore.add(ChatColor.GRAY + "Progress: " + ChatColor.GREEN + normalKitsOwned + "/" + normalKitsTotal);
        if (normalKitsOwned < normalKitsTotal) {
            normalLore.add(ChatColor.GRAY + "Cost to unlock all: " + ChatColor.GOLD + normalKitsCost);
        }
        normalKitsMeta.setLore(normalLore);
        normalKits.setItemMeta(normalKitsMeta);
        inventory.setItem(11, normalKits);

        // Hero Kits
        int heroKitsOwned = 0;
        int heroKitsTotal = 0;
        int heroKitsCost = 0;
        for (Kit kit : Kit.values()) {
            if (kit.getClassType() == ClassType.HERO) {
                heroKitsTotal++;
                if (mPlayer.hasKit(kit)) {
                    heroKitsOwned++;
                } else {
                    try {
                        heroKitsCost += kit.getKitClass().newInstance().getPrice();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ItemStack heroKits = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta heroKitsMeta = heroKits.getItemMeta();
        heroKitsMeta.setDisplayName(ChatColor.AQUA + "Hero Kits");
        List<String> heroLore = new ArrayList<>();
        heroLore.add(ChatColor.GRAY + "Click to view hero kits.");
        heroLore.add(" ");
        heroLore.add(ChatColor.GRAY + "Progress: " + ChatColor.GREEN + heroKitsOwned + "/" + heroKitsTotal);
        if (heroKitsOwned < heroKitsTotal) {
            heroLore.add(ChatColor.GRAY + "Cost to unlock all: " + ChatColor.GOLD + heroKitsCost);
        }
        heroKitsMeta.setLore(heroLore);
        heroKits.setItemMeta(heroKitsMeta);
        inventory.setItem(15, heroKits);


        ItemStack gatheringUpgrades = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta gatheringUpgradesMeta = gatheringUpgrades.getItemMeta();
        gatheringUpgradesMeta.setDisplayName(ChatColor.GOLD + "Gathering Upgrades");
        gatheringUpgradesMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to view gathering upgrades."));
        gatheringUpgrades.setItemMeta(gatheringUpgradesMeta);
        inventory.setItem(13, gatheringUpgrades);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
