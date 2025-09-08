package com.jules.kitpvp.gui;

import com.jules.kitpvp.kits.KitStat;
import com.jules.kitpvp.kits.Upgrade;
import com.jules.kitpvp.kits.UpgradeCategory;
import com.jules.kitpvp.kits.classes.MegaWallsClass;
import com.jules.kitpvp.player.MPlayer;
import com.jules.kitpvp.util.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpgradeGUI implements InventoryHolder {

    private final Inventory inv;
    private final MPlayer mPlayer;
    private final MegaWallsClass kit;

    public UpgradeGUI(MPlayer mPlayer) {
        this.mPlayer = mPlayer;
        if (!(mPlayer.getKitClass() instanceof MegaWallsClass)) {
            throw new IllegalArgumentException("Cannot open MegaWalls upgrade GUI for a non-MegaWalls class.");
        }
        this.kit = (MegaWallsClass) mPlayer.getKitClass();
        this.inv = Bukkit.createInventory(this, 54, "Upgrades for " + kit.getName());
        setupGUI();
    }

    private void setupGUI() {
        // Fill borders with glass panes
        ItemStack blackPane = new ItemStackCreator(Material.BLACK_STAINED_GLASS_PANE, " ").build();
        for (int i = 0; i < 54; i++) {
            if (i < 10 || i > 43 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, blackPane);
            }
        }

        // Set up upgrades
        Map<UpgradeCategory, List<Upgrade>> upgrades = kit.getUpgrades();
        for (UpgradeCategory category : upgrades.keySet()) {
            inv.setItem(category.getRow() * 9 + 1, new ItemStackCreator(getCategoryMaterial(category), "§a" + category.getName()).build());
            List<Upgrade> categoryUpgrades = upgrades.get(category);
            for (int i = 0; i < categoryUpgrades.size(); i++) {
                Upgrade upgrade = categoryUpgrades.get(i);
                int currentLevel = mPlayer.getUpgradeLevel(upgrade);
                for (int tier = 1; tier <= upgrade.getMaxLevel(); tier++) {
                    int slot = category.getRow() * 9 + 2 + (tier - 1);
                    inv.setItem(slot, createUpgradeItem(upgrade, tier, currentLevel));
                }
            }
        }

        // Set up stats and prestige
        setupStats();
        setupPrestige();

        // Add gathering upgrades button
        inv.setItem(45, new ItemStackCreator(Material.GOLDEN_PICKAXE, "§aGathering Upgrades").addLoreLine("§7Click to view gathering upgrades.").build());
    }

    private ItemStack createUpgradeItem(Upgrade upgrade, int tier, int currentLevel) {
        boolean unlocked = tier <= currentLevel;
        boolean nextUnlock = tier == currentLevel + 1;
        boolean canAfford = mPlayer.getCoins() >= upgrade.getCost(tier);

        Material material;
        String name;
        List<String> lore = kit.getLoreForUpgrade(upgrade, tier);
        ItemStackCreator creator;

        if (unlocked) {
            material = Material.GREEN_STAINED_GLASS_PANE;
            name = "§a" + upgrade.getName() + " " + tier;
            lore.add(" ");
            lore.add("§aUNLOCKED");
            creator = new ItemStackCreator(material, name).setDurability((short) 5);
            creator.addEnchantment(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else if (nextUnlock) {
            material = upgrade.getMaterial(tier);
            name = (canAfford ? "§a" : "§c") + upgrade.getName() + " " + tier;
            lore.add(" ");
            lore.add("§7Cost: §6" + upgrade.getCost(tier));
            if (canAfford) {
                lore.add("§aClick to purchase!");
            } else {
                lore.add("§cYou cannot afford this!");
            }
            creator = new ItemStackCreator(material, name);
        } else {
            material = Material.RED_STAINED_GLASS_PANE;
            name = "§c" + upgrade.getName() + " " + tier;
            lore.add(" ");
            lore.add("§7Cost: §6" + upgrade.getCost(tier));
            lore.add("§cLocked!");
            creator = new ItemStackCreator(material, name).setDurability((short) 14);
        }

        creator.setLore(lore);
        return creator.build();
    }

    private void setupStats() {
        KitStat stats = kit.getKitStat();
        inv.setItem(48, new ItemStackCreator(Material.DIAMOND_SWORD, "§cDamage: " + stats.getDamage() + "/5").build());
        inv.setItem(49, new ItemStackCreator(Material.DIAMOND_CHESTPLATE, "§aDefense: " + stats.getDefense() + "/5").build());
        inv.setItem(50, new ItemStackCreator(Material.FEATHER, "§bMobility: " + stats.getMobility() + "/5").build());
        inv.setItem(51, new ItemStackCreator(Material.NETHER_STAR, "§dEnergy: " + stats.getEnergy() + "/5").build());
        inv.setItem(52, new ItemStackCreator(Material.BOOK, "§eDifficulty: " + stats.getDifficulty() + "/5").build());
    }

    private void setupPrestige() {
        // TODO: Implement prestige system
        inv.setItem(53, new ItemStackCreator(Material.DIAMOND, "§dPrestige").addLoreLine("§cComing soon!").build());
    }

    private Material getCategoryMaterial(UpgradeCategory category) {
        switch (category) {
            case ABILITY: return Material.NETHER_STAR;
            case PASSIVE_1: return Material.FEATHER;
            case PASSIVE_2: return Material.TNT;
            case KIT: return Material.DIAMOND_SWORD;
            default: return Material.STONE;
        }
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
