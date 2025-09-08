package com.jules.kitpvp.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ShopGUI implements InventoryHolder {

    private final Inventory inventory;

    public ShopGUI() {
        this.inventory = Bukkit.createInventory(this, 9 * 3, "Shop");
        initializeItems();
    }

    private void initializeItems() {
        ItemStack normalKits = new ItemStack(Material.IRON_SWORD);
        ItemMeta normalKitsMeta = normalKits.getItemMeta();
        normalKitsMeta.setDisplayName(ChatColor.GREEN + "Normal Kits");
        normalKitsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to view normal kits."));
        normalKits.setItemMeta(normalKitsMeta);
        inventory.setItem(12, normalKits);

        ItemStack heroKits = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta heroKitsMeta = heroKits.getItemMeta();
        heroKitsMeta.setDisplayName(ChatColor.AQUA + "Hero Kits");
        heroKitsMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to view hero kits."));
        heroKits.setItemMeta(heroKitsMeta);
        inventory.setItem(14, heroKits);

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
